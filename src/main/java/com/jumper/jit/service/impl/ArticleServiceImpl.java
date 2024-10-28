package com.jumper.jit.service.impl;

import com.jumper.jit.aspect.AutoDeploy;
import com.jumper.jit.aspect.DbException;
import com.jumper.jit.dto.*;
import com.jumper.jit.model.Article;
import com.jumper.jit.model.Subject;
import com.jumper.jit.repository.ArticleAndParentRepository;
import com.jumper.jit.repository.ArticleRepository;
import com.jumper.jit.repository.ArticleRepositoryPage;
import com.jumper.jit.repository.SubjectRepository;
import com.jumper.jit.service.ArticleService;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional()
public class ArticleServiceImpl implements ArticleService {
    private SubjectRepository subjectRepository;
    private ArticleRepository repository;
    private ArticleRepositoryPage repositoryForPage;
    private ArticleAndParentRepository articleAndParentRepository;

    @Autowired
    public void setRepository(ArticleRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setRepositoryForPage(ArticleRepositoryPage repositoryForPage) {
        this.repositoryForPage = repositoryForPage;
    }

    @Autowired
    public void setSubjectRepository(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Autowired
    public void setArticleAndParentRepository(ArticleAndParentRepository articleAndParentRepository) {
        this.articleAndParentRepository = articleAndParentRepository;
    }

    @Override
    public Article findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new DbException("id=" + id));
    }

    @Override
    public SimpleArticleWithContentDTO getSimpleWithContentById(Integer id) {
        return repository.getArticleById(id);
    }

    @Override
    public List<SimpleArticleWithContentDTO> findAllWithContentBySidAndStatus(Integer sid, Integer status) {
        return repository.findAllBySidAndStatus(sid, status);
    }

    @Override
    public List<SimpleArticleWithoutContentDTO> findArticleTree(Integer sid) {
        List<SimpleArticleWithoutContentDTO> list = repository.findArticlesWithoutContent(sid);
        return processSortList(list);
    }

    @Override
    public List<SimpleArticleWithoutContentDTO> findArticleTree(Integer sid, Integer status) {
        List<SimpleArticleWithoutContentDTO> list = repository.findArticlesWithoutContent(sid, status);
        return processSortList(list);
    }

    private List<SimpleArticleWithoutContentDTO> processSortList(List<SimpleArticleWithoutContentDTO> list) {
        List<SimpleArticleWithoutContentDTO> sorted = new ArrayList<>();
        Map<Integer, SimpleArticleWithoutContentDTO> index = new HashMap<>();
        list.forEach(a -> index.put(a.getId(), a));
        list.forEach(a -> {
            if (a.getPid() == null) {
                sorted.add(a);//顶级
            } else {//非顶级
                List<SimpleArticleWithoutContentDTO> children = index.get(a.getPid()).getChildren();
                if (children != null) children.add(a);
                else {
                    children = new ArrayList<>();
                    children.add(a);
                    index.get(a.getPid()).setChildren(children);
                }
            }
        });
        return sortAll(sorted);
    }

    private List<SimpleArticleWithoutContentDTO> sortAll(List<SimpleArticleWithoutContentDTO> l) {
        Collections.sort(l);
        l.forEach(e -> {
            if (e.getChildren() != null) {
                sortAll(e.getChildren());
            }
        });
        return l;
    }

    @Override
    public Article add(Article article) {
        if (article.getPid() == null && article.getSid() != null) {//专题下顶级文章
            Integer maxOrderNum = repository.findTopLevelArticleCountBySid(article.getSid());
            article.setOrderNum(maxOrderNum == null ? 1 : maxOrderNum + 1);
        }
        if (article.getPid() != null && article.getSid() != null) {//专题下子节点
            Integer count = repository.findChildrenCountByPid(article.getPid());
            article.setOrderNum(count == null ? 1 : count + 1);
        }
        if (article.getSid() != null) {//如果是专题文章则文章数+1
            subjectRepository.setSubjectArticleSum(article.getSid(), 1);
        }

        //因为关联的是Subject,sid不会自动更新
        if (article.getSid() != null) article.setSubject(Subject.builder().id(article.getSid()).build());
        //因为因为关联的是parent,pid不会自动更新
        if (article.getPid() != null) article.setArticle(Article.builder().id(article.getPid()).build());

        if (article.getContent() != null && !article.getContent().isEmpty()) {
            article.setStatus(Article.Status.SAVE_CONTENT.getCode());
        }
        repository.save(article);
        return article;
    }

    @Override
    public Article updateSingle(Article article) {
        Integer id = article.getId();
        Article dbArticle = repository.findById(article.getId()).orElseThrow(() -> new DbException("id=" + id));
        dbArticle.setTitle(article.getTitle());
        dbArticle.setEnName(article.getEnName());
        dbArticle.setContent(article.getContent());
        //如果已经发布
        if (dbArticle.getStatus().equals(Article.Status.PUBLISHED.getCode())) {
            dbArticle.setStatus(Article.Status.MODIFIED_UNPUBLISHED.getCode());
        } else if (dbArticle.getStatus() < 2) {
            dbArticle.setStatus(Article.Status.SAVE_CONTENT.getCode());
        }
        repository.save(dbArticle);
        if (article.getSid() == null && article.getPid() == null) {//单体文章
            repository.save(dbArticle);
        } else if (article.getPid() == null) {//把文章插入到主题下
            this.insertNodeAsChild(id, article.getSid(), true);
        } else {//把文章插入到主题的子节点下
            this.insertNodeAsChild(id, article.getPid(), false);
        }
        return dbArticle;
    }

    @Override
    public void delete(Integer id) {
        SimpleArticleWithoutContentDTO dto = repository.getArticleWithoutContentById(id);
        if (dto == null) throw new DbException("article id = " + id);
        repository.delById(id);
        AtomicInteger counter = new AtomicInteger(1);
        if (repository.existsByPid(id)) {//如果存在子元素
            //需要循环删除,擦
            List<SimpleArticleWithoutContentDTO> children = repository.findArticlesWithoutContentByPid(id);
            children.forEach(a -> {
                counter.getAndAdd(1);
                deleteRecursion(a.getId(), counter);
            });
        }
        //修改orderNum,同级别大于它的orderNum-1;
        if (dto.getPid() == null && dto.getSid() != null) {
            repository.setAllOrderNumAfterCurrentNodeBySid(dto.getSid(), dto.getOrderNum(), -1);
        }
        if (dto.getPid() != null && dto.getSid() != null) {
            repository.setAllOrderNumAfterCurrentNodeByPid(dto.getPid(), dto.getOrderNum(), -1);
        }
        //最后修改总文章数量
        if (dto.getSid() != null) {
            subjectRepository.setSubjectArticleSum(dto.getSid(), -(counter.get()));
        }

    }

    public void deleteRecursion(Integer id, AtomicInteger counter) {
        repository.delById(id);
        if (repository.existsByPid(id)) {
            List<SimpleArticleWithoutContentDTO> children = repository.findArticlesWithoutContentByPid(id);
            children.forEach(a -> {
                counter.getAndAdd(1);
                deleteRecursion(a.getId(), counter);
            });
        }
    }


    @Override
    public void updateTitle(Integer id, String title) {
        repository.updateArticleTitle(id, title);
    }

    @Override
    public void updateEnName(Integer id, String enName) {
        repository.updateArticleEnName(id, enName);
    }


    @AutoDeploy(desc = "检查文章是否已经发布,如果是发布过的文章,内容修改后自动发布")
    @Override
    public Article updateContent(Article article) {
        Article dbResult = repository.findById(article.getId()).orElseThrow(() -> new DbException("article id=" + article.getId()));
        dbResult.setContent(article.getContent());
        dbResult.setEnName(article.getEnName());
        if (dbResult.getStatus().equals(Article.Status.NO_CONTENT.getCode())) {
            dbResult.setStatus(Article.Status.SAVE_CONTENT.getCode());
        }
        if (dbResult.getStatus().equals(Article.Status.PUBLISHED.getCode())) {
            dbResult.setStatus(Article.Status.MODIFIED_UNPUBLISHED.getCode());
        }
        repository.save(dbResult);
        return dbResult;
    }

    @Override
    public void moveTo(Integer id, Integer targetId) {
        SimpleArticleWithoutContentDTO current = repository.getArticleWithoutContentById(id);//拖拽当前节点
        SimpleArticleWithoutContentDTO target = repository.getArticleWithoutContentById(targetId);//拖拽去的目标节点
        /*
         * 如果拖到父节点后面,则该节点成为父节点第一个节点,序号变为1,同时父节点的第一个节点和current之间的节点序号+1
         */
        if (current.getPid() != null && targetId.equals(current.getPid())) {
            repository.setAllOrderNumByBetween(current.getPid(), 1, current.getOrderNum() - 1, 1);
            repository.setOrderNum(id, 1);
        } else if (target.getPid() != null && target.getPid().equals(current.getPid())) {//目标节点是同级子节点
            /*
             * 向前拖:如果拖到子节点后面(子节点序号为orderNum),则序号变为子节点的序号orderNum+1,
             * 同时子节点序号orderNum后面的节点和当前节点的旧序号之间的节点序号+1
             * */
            if (current.getOrderNum() > target.getOrderNum()) {
                repository.setAllOrderNumByBetween(current.getPid(), target.getOrderNum() + 1, current.getOrderNum() - 1, 1);
                repository.setOrderNum(id, target.getOrderNum() + 1);
            } else {//向后拖拽,则当前节点和目标节点之间(包括目标节点)的orderNum-1,当前节点orderNum变为目标节点的orderNum
                repository.setAllOrderNumByBetween(current.getPid(), current.getOrderNum() + 1, target.getOrderNum(), -1);
                repository.setOrderNum(id, target.getOrderNum());
            }
        } else if (current.getPid() != null) {//如果拖到了父节点外的节点后面
            if (target.getPid() != null) {
                repository.setAllOrderNumByAfter(target.getPid(), target.getOrderNum(), 1);
                repository.updatePidAndOrderNum(id, target.getPid(), target.getOrderNum() + 1);
                //同时修改原来的父节点的序号
                repository.setAllOrderNumByAfter(current.getPid(), current.getOrderNum(), -1);
            } else {//非顶级节点到顶级节点移动
                repository.setAllOrderNumByAfter(current.getPid(), current.getOrderNum(), -1);
                //目标节点处理
                repository.setAllOrderNumAfterCurrentNodeBySid(target.getSid(), target.getOrderNum(), 1);
                //修改当前节点pid
                repository.updatePidAndOrderNum(current.getId(), target.getPid(), target.getOrderNum() + 1);
            }
        } else {//移动的是顶级节点
            //如果目标节点顶级节点
            if (target.getPid() == null) {
                //如果是向前移动
                if (current.getOrderNum() > target.getOrderNum()) {
                    repository.setAllOrderNumBetweenBySid(current.getSid(), 1, current.getOrderNum() + 1, target.getOrderNum() - 1);
                    repository.setOrderNum(current.getId(), target.getOrderNum() + 1);
                } else {//如果是向后移动
                    repository.setAllOrderNumBetweenBySid(current.getSid(), -1, current.getOrderNum() + 1, target.getOrderNum());
                    repository.setOrderNum(current.getId(), target.getOrderNum());
                }
            } else {//如果目标节点非顶级节点
                //本级节点修改
                repository.setAllOrderNumAfterCurrentNodeBySid(current.getSid(), current.getOrderNum(), -1);
                //目标节点修改
                repository.setAllOrderNumByAfter(target.getPid(), target.getOrderNum(), 1);
                repository.updatePidAndOrderNum(current.getId(), target.getPid(), target.getOrderNum() + 1);
            }
        }
    }

    @Override
    public void insertNodeAsChild(Integer currentId, Integer targetId, boolean isSubject) {
        /*
         * 1 前端已经排除自身和自身的父节点,此处只考虑其它父节点
         * 2 由于是插入到目标节点的所有子节点之后,所以不用考虑目标节点序号问题
         */
        SimpleArticleWithoutContentDTO current = repository.getArticleWithoutContentById(currentId);
        //如果目标节点是主题
        if (isSubject) {
            //如果本来存在于此主题下,并且是顶级节点,则不做处理
            if (targetId.equals(current.getSid()) && current.getPid() == null) return;
            //修改文章自身sid,orderNum,pid置空
            Integer orderNum = repository.findTopLevelArticleCountBySid(targetId);
            repository.updateArticleAsChildToSubject(currentId, targetId, orderNum + 1);

            //修改子孙节点sid,同时返回计数
            AtomicInteger counter = new AtomicInteger(1);
            if (current.getSid() != null && !current.getSid().equals(targetId)) {
                updateDescendantRecursionById(currentId, targetId, counter);
            }

            //修改自身主题的文章数量-1,排除单体文章,排除相同主题
            if (current.getSid() != null && !current.getSid().equals(targetId)) {
                subjectRepository.setSubjectArticleSum(current.getSid(), -counter.get());
            }
            //修改目标主题文章数量+1,只要不是一个主题
            if (!targetId.equals(current.getSid())) {
                subjectRepository.setSubjectArticleSum(targetId, counter.get());
            }
        } else {
            SimpleArticleWithoutContentDTO target = repository.getArticleWithoutContentById(targetId);
            //修改自身sid,pid,orderNum
            Integer orderNum = repository.findArticleCountByPid(targetId);
            repository.updateArticleAsChildToNode(currentId, target.getSid(), targetId, orderNum + 1);

            //修改子孙节点sid,同时返回计数
            AtomicInteger counter = new AtomicInteger(1);
            if (current.getSid() != null && !current.getSid().equals(target.getSid())) {
                updateDescendantRecursionById(currentId, target.getSid(), counter);
            }

            //修改自身主题的文章数量-1,排除单体文章,排除相同主题
            if (current.getSid() != null && !current.getSid().equals(target.getSid())) {
                subjectRepository.setSubjectArticleSum(current.getSid(), -counter.get());
            }
            //修改目标主题文章数量+1,只要不是一个主题
            if (!target.getSid().equals(current.getSid())) {
                subjectRepository.setSubjectArticleSum(targetId, counter.get());
            }
        }
        //修改自身原来同级节点序号orderNum
        boolean isTopLevel = current.getPid() == null;
        if (isTopLevel) {
            repository.setAllOrderNumAfterCurrentNodeBySid(current.getSid(), current.getOrderNum(), -1);
        } else {
            repository.setAllOrderNumAfterCurrentNodeByPid(current.getPid(), current.getOrderNum(), -1);
        }
    }

    /**
     * 父节点改变sid后,子节点跟随
     *
     * @param id      父节点
     * @param sid     主题id
     * @param counter 计数器
     */
    public void updateDescendantRecursionById(Integer id, final Integer sid, AtomicInteger counter) {
        if (repository.existsByPid(id)) {
            List<SimpleArticleWithoutContentDTO> children = repository.findArticlesWithoutContentByPid(id);
            repository.updateSidByPid(id, sid);//批量修改sid
            children.forEach(a -> {
                counter.getAndAdd(1);
                updateDescendantRecursionById(a.getId(), sid, counter);
            });
        }
    }

    @Override
    public Page<ArticleDTO> findArticles(ArticleDTO dto) {
        Specification<ArticleDTO> spec = ((root, q, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (dto.getKeyword() != null && !dto.getKeyword().isEmpty()) {
                predicates.add(cb.like(root.get("title"), "%" + dto.getKeyword() + "%"));
            }
            if (dto.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), dto.getStatus()));
            }
            assert q != null;
            List<Order> orders = new ArrayList<>();
            orders.add(cb.desc(root.get("createdAt")));
            orders.add(cb.desc(root.get("updatedAt")));

            return Objects.requireNonNull(q).orderBy(orders).where(predicates.toArray(new Predicate[0])).getRestriction();
        });
        int pageNo = dto.getPageNo() == null ? 0 : dto.getPageNo();
        int pageSize = dto.getPageSize() == null ? 10 : dto.getPageSize();
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        return repositoryForPage.findAll(spec, pageRequest);
    }

    @Override
    public List<ArticleAndParentDTO> findArticlesWithParentAndSubject(String title) {
        return articleAndParentRepository.findByTitleContainingIgnoreCase(title);
    }

    @Value("${upload.save-path}")
    private String savePath;
    @Value("${upload.base-url}")
    private String baseUrl;
    @Value("${upload.relative-path}")
    private String relativePath;
    @Value("${upload.article-pic}")
    private String articlePath;

    @Override
    public FileDTO saveFile(FileDTO fileDTO) throws IOException {
        MultipartFile[] files = fileDTO.getFiles();
        String[] names = new String[files.length];
        for (int i = 0; i < files.length; i += 1) {
            File file = new File(savePath + articlePath + files[i].getOriginalFilename());
            FileCopyUtils.copy(files[i].getBytes(), file);
            names[i] = files[i].getOriginalFilename();
        }
        fileDTO.setBaseurl(baseUrl);
        fileDTO.setPath(relativePath);
        fileDTO.setFileNames(Arrays.asList(names));
        fileDTO.setFiles(null);
        return fileDTO;
    }

    @Override
    public void updateStatus(Integer id, Integer status) {
        repository.updateStatus(id, status);
    }

    @Override
    public List<SimpleArticleWithoutContentDTO> findAllSingleArticleByStatus(Integer status) {
        return repository.getArticleAllSingleWithoutContentByStatus(status);
    }

    @Override
    public List<Integer> filterSidsNotExistsDeployedTopNode(List<Integer> sids, Integer status) {
        return repository.findSidsWithDeployedTopNode(sids, status);
    }
}
