package com.jumper.jit.service;

import com.jumper.jit.aspect.ResultData;
import com.jumper.jit.dto.*;
import com.jumper.jit.model.Article;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface ArticleService {

    /**
     * 查询树状文章标题列表(排除内容)根据主题sid
     *
     * @param sid 主题
     * @return 文章列表
     */
    List<SimpleArticleWithoutContentDTO> findArticleTree(Integer sid);

    /**
     * 查询树状文章标题列表(排除内容)根据主题sid
     *
     * @param sid    主题
     * @param status 发布状态
     * @return 文章列表
     */
    List<SimpleArticleWithoutContentDTO> findArticleTree(Integer sid, Integer status);

    /**
     * 查询所有可发布(status>0)的树状文章标题列表(排除内容)根据主题sid
     *
     * @param sid 主题
     * @return 文章列表
     */
    List<SimpleArticleWithoutContentDTO> findAllPublishableArticleTree(Integer sid);

    /**
     * 查询全部字段并进行级联查询
     *
     * @param id id
     * @return 完整文章
     */
    Article findById(Integer id);

    /**
     * 不进行级联查询,仅返回所需部分字段
     *
     * @param id id
     * @return 带内容的简单article
     */
    SimpleArticleWithContentDTO getSimpleWithContentById(Integer id);

    /**
     * 查询主题下所有已发布状态的文章
     *
     * @param sid    主题id
     * @param status 状态
     * @return 文章列表
     */
    List<SimpleArticleWithContentDTO> findAllWithContentBySidAndStatus(Integer sid, Integer status);

    /**
     * 查询主题下所有可以发布的文章(status>0)
     *
     * @param sid 主题id
     * @return 文章列表
     */
    List<SimpleArticleWithContentDTO> findAllPublishableWithContentBySid(Integer sid);


    /**
     * 仅限添加文章(添加顶级节点/子节点/单体文章)
     *
     * @return 文章
     */
    Article add(Article article);

    Article updateSingle(Article article);


    Article updateContent(Article article);

    /**
     * 删除文章
     */
    Article delete(Integer id);

    /**
     * 修改文章标题
     */
    void updateTitle(Integer id, String title);

    /**
     * 修改文章英文标题
     */
    void updateEnName(Integer id, String enName);

    /**
     * 修改关键词
     */
    void updateArticleKeyword(Integer id, String articleKeyword);

    /**
     * 同一个专题内:移动节点到目标节点后
     *
     * @param currenId 当前节点Id
     * @param targetId 目标节点Id
     */
    void moveTo(Integer currenId, Integer targetId);

    /**
     * 不限专题:把current节点作为孩子插入target的最后
     *
     * @param currenId  当前节点Id
     * @param targetId  目标节点Id
     * @param isSubject 目标节点是主题么 true 是 false 非主题
     */
    void insertNodeAsChild(Integer currenId, Integer targetId, boolean isSubject);

    /**
     * 多条件分页查询文章列表(排除内容),附带主题
     *
     * @param dto 多条件查询参数
     * @return ArticleDTO
     */
    Page<ArticleDTO> findArticles(ArticleDTO dto);

    /**
     * 根据标题名模糊搜索文章,同时附带父节点和主题
     *
     * @param title 文章标题
     * @return 文章列表
     */
    List<ArticleAndParentDTO> findArticlesWithParentAndSubject(String title);

    /**
     * 保存上传文件
     *
     * @param fileDTO 接收容器
     * @return FileDTO
     */
    FileDTO saveFile(FileDTO fileDTO) throws IOException;

    void updateStatus(Integer id, Integer status, LocalDateTime publishedAt);

    void updateBatchStatus(Integer status, LocalDateTime publishedAt, List<Integer> ids);

    /**
     * 查询所有单体文章
     *
     * @param status 状态
     * @return 发布的单体文章列表
     */
    List<SimpleArticleWithoutContentDTO> findAllSingleArticleByStatus(Integer status);

    List<SimpleArticleWithContentDTO> findAllSingleArticleWithContentByStatus(Integer status);


    /**
     * 过滤主题sid列表中"不存在已经发布的顶级文章节点的sid"
     *
     * @param sids   主题id列表
     * @param status 目前只有已发布状态的需求,status=2
     * @return boolean
     */
    List<Integer> filterSidsNotExistsDeployedTopNode(List<Integer> sids, Integer status);


    //保存单体文章只修改发布状态
    void saveAndUpdateSingleStatus(Article article) throws IOException;

    //保存专题文章,只修改发布状态
    void saveAndUpdateSubjectArticleStatus(Article article) throws IOException;

    ResultData<Article> checkEnName(String enName, Integer sid);
}
