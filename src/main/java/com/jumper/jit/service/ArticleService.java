package com.jumper.jit.service;

import com.jumper.jit.dto.ArticleDTO;
import com.jumper.jit.dto.SimpleArticleWithContentDTO;
import com.jumper.jit.dto.SimpleArticleWithoutContentDTO;
import com.jumper.jit.dto.SubjectDTO;
import com.jumper.jit.model.Article;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ArticleService {

    /**
     * 查询树状文章标题列表(排除内容)根据主题sid
     * @param sid 主题
     * @return 文章列表
     */
    List<SimpleArticleWithoutContentDTO> findArticleTree(Integer sid);

    /**
     * 查询全部字段并进行级联查询
     * @param id  id
     * @return 完整文章
     */
    Article findById(Integer id);

    /**
     * 不进行级联查询,仅返回所需部分字段
     * @param id id
     * @return 带内容的简单article
     */
    SimpleArticleWithContentDTO getSimpleWithContentById(Integer id);

    /**
     * 查询所有主题以及主题的树状文章列表
     * @return 树状列表
     */
    List<SubjectDTO> findAllSubjectArticles();

    /**
     * 保存文章(添加顶级节点/子节点/单体文章)
     * @return 文章
     */
    Article add(Article article);

    Article updateContent(Article article);
    /**
     * 删除文章
     */
    void delete(Integer id);

    /**
     * 修改文章标题
     */
    void updateTitle(Integer id, String title);

    /**
     * 同一个专题内:移动节点到目标节点后
     * @param currenId 当前节点Id
     * @param targetId 目标节点Id
     */
    void moveTo(Integer currenId,Integer targetId);


    /**
     * 修改文章父节点为专题,同时会修改相同pid下的同级文章的序号;在专题最后添加一条顶级节点
     * @param id 文章id
     * @param pid 上级文章id
     * @param targetSubjectId 目标节点id
     */
    void updatePidToSubject(Integer id, Integer pid,Integer targetSubjectId);
    /**
     * 根据ID查询文章,附带主题
     * @param id 文章id
     * @return 文章
     */
    Article findWithSubject(Integer id);

    /**
     * 根据pid查询文章列表(排除内容)
     * @param pid 上级文章id
     * @return 文章列表
     */
    List<Article> findArticleByPid(Integer pid);


    /**
     * 多条件分页查询文章列表(排除内容),附带主题
     * @param dto 多条件查询参数
     * @return ArticleDTO
     */
    Page<ArticleDTO> findArticles(ArticleDTO dto);

    /**
     * 根据标题名模糊搜索文章,同时附带父节点和主题
     * @param title 文章标题
     * @return 文章列表
     */
    List<ArticleDTO> findArticlesWithParentAndSubject(String title);
    /**
     * 删除文章,同时修改序号
     */
    void delAndUpdateOrderNum();

}
