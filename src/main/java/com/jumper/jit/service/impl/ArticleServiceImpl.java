package com.jumper.jit.service.impl;

import com.jumper.jit.aspect.DbException;
import com.jumper.jit.dto.*;
import com.jumper.jit.model.Article;
import com.jumper.jit.model.Subject;
import com.jumper.jit.repository.ArticleRepository;
import com.jumper.jit.repository.ArticleRepositoryPage;
import com.jumper.jit.repository.SubjectRepository;
import com.jumper.jit.service.ArticleService;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional()
public class ArticleServiceImpl implements ArticleService {
    private SubjectRepository subjectRepository;
    private ArticleRepository repository;
    private ArticleRepositoryPage repositoryForPage;

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

    @Override
    public Article findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new DbException("id=" + id));
    }

    @Override
    public SimpleArticleWithContentDTO getSimpleWithContentById(Integer id) {
        return repository.getArticleById(id);
    }

    @Override
    public List<SimpleArticleWithoutContentDTO> findArticleTree(Integer sid) {
        List<SimpleArticleWithoutContentDTO> list = repository.findArticlesWithoutContent(sid);
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

    private List<SubjectArticleDTO> sortAllSubject(List<SubjectArticleDTO> l) {
        Collections.sort(l);
        l.forEach(e -> {
            if (e.getChildren() != null) {
                sortAllSubject(e.getChildren());
            }
        });
        return l;
    }

    @Override
    public List<SubjectDTO> findAllSubjectArticles() {
        List<SubjectArticleDTO> subjectArticleDTOS = repositoryForPage.findAllSubjectsWithArticles();
        Map<Integer, SubjectDTO> subjectMap = new HashMap<>();//所有主题
        subjectArticleDTOS.forEach(a -> {//筛选所有主题
            if (subjectMap.get(a.getSid()) == null) {
                subjectMap.put(a.getSid(), SubjectDTO.builder().id(a.getSid()).subjectTitle(a.getSubjectTitle()).build());
            }
        });
        subjectArticleDTOS.forEach(a -> {//为每个主题填充文章列表
            List<SubjectArticleDTO> children = subjectMap.get(a.getSid()).getArticles();
            if (children != null) {
                children.add(a);
            } else {
                children = new ArrayList<>();
                children.add(a);
                subjectMap.get(a.getSid()).setArticles(children);
            }
        });
        //排列每个主题中的文章列表
        subjectMap.forEach((sid, subject) -> {
            if (subject.getArticles() == null || subject.getArticles().isEmpty()) return;
            List<SubjectArticleDTO> sorted = new ArrayList<>();
            Map<Integer, SubjectArticleDTO> index = new HashMap<>();
            subject.getArticles().forEach(a -> index.put(a.getId(), a));
            subject.getArticles().forEach(a -> {
                if (a.getPid() == null) {
                    sorted.add(a);//顶级
                } else {//非顶级
                    List<SubjectArticleDTO> children = index.get(a.getPid()).getChildren();
                    if (children != null) children.add(a);
                    else {
                        children = new ArrayList<>();
                        children.add(a);
                        index.get(a.getPid()).setChildren(children);
                    }
                }
            });
            subject.setArticles(sortAllSubject(sorted));
        });
        return new ArrayList<>(subjectMap.values());
    }

    @Override
    public Article save(Article article) {
        if (article.getPid() == null && article.getSid() != null) {//专题下顶级文章
            Integer maxOrderNum = repository.findTopLevelArticleCountBySid(article.getSid());
            article.setOrderNum(maxOrderNum + 1);
        }
        if (article.getPid() != null && article.getSid() != null) {//专题下子节点
            Integer count = repository.findChildrenCountByPid(article.getPid());
            article.setOrderNum(count + 1);
        }
        if (article.getSid() != null) {//如果是专题则文章数+1
            subjectRepository.setSubjectArticleSum(article.getSid(), 1);
        }

        if (article.getSid() != null) article.setSubject(Subject.builder().id(article.getSid()).build());
        if (article.getPid() != null) article.setArticle(Article.builder().id(article.getPid()).build());
        if (article.getContent() != null) {
            article.setStatus(1);
        }
        repository.save(article);
        return article;
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
    public Article saveContent(Article article) {
        Optional<Article> optional = repository.findById(article.getId());
        Article dbArticle = optional.orElseThrow(() -> new DbException("id=" + article.getId()));
        dbArticle.setContent(article.getContent());
        repository.updateArticleContent(article.getId(), article.getContent());
        return dbArticle;
    }

    @Override
    public void moveTo(Integer id, Integer targetId) {
        Article current = repository.findById(id).orElseThrow();//拖拽当前节点
        Integer pid = current.getPid();
        Article target = repository.findById(targetId).orElseThrow();//拖拽去的目标节点
        /*
         * 如果拖到父节点后面,则该节点成为父节点第一个节点,序号变为1,同时父节点的第一个节点和current之间的节点序号+1
         */
        if (targetId.equals(pid)) {
            repository.addAllOrderNumByOneBetween(pid, 1, current.getOrderNum() - 1, 1);
            repository.setOrderNum(id, 1);
        } else if (target.getPid().equals(pid)) {//目标节点是同级子节点
            /*
             * 向前拖:如果拖到子节点后面(子节点序号为orderNum),则序号变为子节点的序号orderNum+1,
             * 同时子节点序号orderNum后面的节点和当前节点的旧序号之间的节点序号+1
             * */
            if (current.getOrderNum() > target.getOrderNum()) {
                repository.addAllOrderNumByOneBetween(pid, target.getOrderNum() + 1, current.getOrderNum() - 1, 1);
                repository.setOrderNum(id, target.getOrderNum() + 1);
            } else {//向后拖拽,则当前节点和目标节点之间(包括目标节点)的orderNum-1,当前节点orderNum变为目标节点的orderNum
                repository.addAllOrderNumByOneBetween(pid, current.getOrderNum() + 1, target.getOrderNum(), -1);
                repository.setOrderNum(id, target.getOrderNum());
            }
        } else {//如果拖到了父节点外的节点后面且该节点序号为orderNum,则orderNum后的所有节点+1,同时插入该节点为orderNum+1;最后修改pid
            repository.addAllOrderNumByOneAfter(target.getPid(), target.getOrderNum());
            repository.updatePidAndOrderNum(id, target.getPid(), target.getOrderNum() + 1);
//            //统计移动的文章数量,[대부분 시간 없다]
//            int descendantCount = countDescendant(current.getId());
//            //修改专题文章数量
//            subjectService.updateSubjectArticleSum(current.getSid(),-(descendantCount+1));
//            subjectService.updateSubjectArticleSum(target.getSid(),+(descendantCount+1));
        }
    }
//    private Integer countDescendant(Integer pid){
//        AtomicInteger sum = new AtomicInteger();
//        List<Article> children = repository.findByPid(pid);
//        sum.addAndGet(children.size());
//        children.forEach(sub->{
//            if(repository.existsByPid(sub.getId())){
//                sum.addAndGet(countDescendant(sub.getId()));
//            }
//        });
//        return sum.get();
//    }

    @Override
    public void updatePid(Integer id, Integer pid, Integer targetId) {
        Article target = repository.findById(targetId).orElseThrow();
        repository.addAllOrderNumByOneAfter(target.getPid(), target.getOrderNum());
        repository.updatePidAndOrderNum(id, target.getPid(), target.getOrderNum() + 1);
    }

    @Override
    public void updatePidToSubject(Integer id, Integer pid, Integer targetSubjectId) {

    }

    @Override
    public Article findWithSubject(Integer id) {
        return null;
    }

    @Override
    public List<Article> findArticleByPid(Integer pid) {
        return List.of();
    }

    @Override
    public Page<ArticleDTO> findArticles(ArticleDTO dto) {
        Specification<ArticleDTO> spec = ((root, q, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (dto.getSubjectTitle() != null) {
                predicates.add(cb.like(root.get("title"), "%" + dto.getTitle() + "%"));
            }
            assert q != null;
            q.multiselect(root.get("id"), root.get("title"), root.get("pid"), root.get("createdAt")
                    , root.get("updatedAt"), root.get("subject"));
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
    public List<ArticleDTO> findArticlesWithParentAndSubject(String title) {
        return repositoryForPage.findByTitleContainingIgnoreCaseAndSidIsNotNullOrderBySidAscPidAscOrderNumAsc(title);
    }

    @Override
    public void delAndUpdateOrderNum() {

    }
}
