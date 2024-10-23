package com.jumper.jit.service;

import com.jumper.jit.dto.*;
import com.jumper.jit.model.Article;
import org.springframework.data.domain.Page;

import java.io.IOException;
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
     * 保存文章(添加顶级节点/子节点/单体文章)
     * @return 文章
     */
    Article add(Article article);
    Article updateSingle(Article article);


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
     * 不限专题:把current节点作为孩子插入target的最后
     * @param currenId 当前节点Id
     * @param targetId 目标节点Id
     * @param isSubject 目标节点是主题么 true 是 false 非主题
     */
    void insertNodeAsChild(Integer currenId,Integer targetId,boolean isSubject);

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
    List<ArticleAndParentDTO> findArticlesWithParentAndSubject(String title);

    /**
     * 保存上传文件
     * @param fileDTO 接收容器
     * @return FileDTO
     */
    FileDTO saveFile(FileDTO fileDTO) throws IOException;
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
     * 删除文章,同时修改序号
     */
    void delAndUpdateOrderNum();

}
