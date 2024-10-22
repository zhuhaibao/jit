package com.jumper.jit.service.impl;

import com.jumper.jit.aspect.DbException;
import com.jumper.jit.dto.SimpleSubjectDTO;
import com.jumper.jit.dto.SubjectDTO;
import com.jumper.jit.model.Subject;
import com.jumper.jit.repository.SubjectRepository;
import com.jumper.jit.service.SubjectService;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Transactional
@Service
public class SubjectServiceImpl implements SubjectService {
    private SubjectRepository dao;

    @Autowired
    public void setDao(SubjectRepository dao) {
        this.dao = dao;
    }

    @Override
    public Subject findById(Integer id) {
        return dao.findById(id).orElseThrow(()->new DbException("id="+id));
    }

    @Override
    public Subject save(Subject subject) {
        return dao.save(subject);
    }

    @Override
    public Subject updateSubject(SubjectDTO subject) {
        Optional<Subject> optional = dao.findById(subject.getId());
        Subject returnO = optional.orElseThrow(()->new DbException(subject));
        if(subject.getRemark()!=null)returnO.setRemark(subject.getRemark());
        if(subject.getSubjectTitle()!=null) returnO.setSubjectTitle(subject.getSubjectTitle());
        return dao.save(returnO);
    }

    @Override
    public void delete(Integer id) {
        if(dao.existsById(id))dao.deleteById(id);
        else throw new DbException("subject[id]="+id);
    }

    @Override
    public Page<Subject> findSubjectBy(SubjectDTO dto) {
        //构造查询条件
        Specification<Subject> spec = ((root, q, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(dto.getKeyword()!=null && !dto.getKeyword().isEmpty()){
                predicates.add(cb.like(root.get("subjectTitle"),"%"+dto.getKeyword()+"%"));
            }

            List<Order> orders = new ArrayList<>();
            if(dto.getOrderByCreate() == null){
                orders.add(cb.desc(root.get("createdAt")));
            }else{
                orders.add(dto.getOrderByUpdate()==SubjectDTO.Order.ASC?cb.asc(root.get("createdAt")):cb.desc(root.get("createdAt")));
            }
            if(dto.getOrderByUpdate() == null){
                orders.add(cb.desc(root.get("updatedAt")));
            }else{
                orders.add(dto.getOrderByUpdate()==SubjectDTO.Order.ASC?cb.asc(root.get("updatedAt")):cb.desc(root.get("updatedAt")));
            }

            return Objects.requireNonNull(q).orderBy(orders).where(predicates.toArray(new Predicate[0])).getRestriction();
        });
        //构造分页
        int pageNo = dto.getPageNo()==null?0:dto.getPageNo();
        int pageSize = dto.getPageSize() == null?10:dto.getPageSize();
        PageRequest pageRequest = PageRequest.of(pageNo,pageSize);
        return dao.findAll(spec,pageRequest);
    }

    @Override
    public Integer updateSubjectArticleSum(Integer id, Integer num) {
        return dao.setSubjectArticleSum(id,num);
    }


    @Override
    public List<SimpleSubjectDTO> findAllSimpleSubject() {
        return dao.queryAllByIdNotNullOrderByCreatedAtDescUpdatedAtDesc();
    }
/****************************下面是导航操作*****************************/
    @Override
    public List<Subject> findByNavigation(boolean navigation,String keyword) {
        List<Subject> l;
        if(keyword!=null && !keyword.isEmpty()){
            l = dao.findSubjectByNavigationAndSubjectTitleContainingIgnoreCase(navigation,keyword);
        }else{
            l = dao.findSubjectByNavigation(navigation);
        }
        if(navigation){//导航要排序
            Collections.sort(l);
        }
        return l;
    }

    @Override
    public void moveTo(Integer id, Integer targetId) {
        Subject current = dao.findById(id).orElseThrow(()->new DbException("id="+id));
        Subject target = dao.findById(targetId).orElseThrow(()->new DbException("targetId="+targetId));
        if(current.getOrderNum()<target.getOrderNum()){//往后
            dao.setSubjectOrderNumBetween(-1,current.getOrderNum()+1,target.getOrderNum());
            dao.setSubjectOrderNum(current.getId(),target.getOrderNum());
        }else{//往前
            dao.setSubjectOrderNumBetween(1,target.getOrderNum()+1,current.getOrderNum()-1);
            dao.setSubjectOrderNum(current.getId(),target.getOrderNum()+1);
        }
    }

    @Override
    public void deleteNavigation(Integer id) {
        Subject current = dao.findById(id).orElseThrow(()->new DbException("id="+id));
        dao.updateSubjectByNavigation(null,false,id);//修改为非导航
        dao.setOrderNumAfter(-1,current.getOrderNum());
    }

    @Override
    public Subject addNavigation(Integer id) {
        Integer count = dao.findNavigationCount();
        dao.setSubjectAsNavigation(id,count+1);
        return dao.findById(id).orElseThrow();
    }
}
