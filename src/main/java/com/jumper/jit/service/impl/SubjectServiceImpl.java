package com.jumper.jit.service.impl;

import com.jumper.jit.aspect.DbException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Transactional
@Service
public class SubjectServiceImpl implements SubjectService {
    private SubjectRepository dao;

    @Autowired
    public void setDao(SubjectRepository dao) {
        this.dao = dao;
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
            if(dto.getSubjectTitle()!=null){
                predicates.add(cb.like(root.get("subjectTitle"),"%"+dto.getSubjectTitle()+"%"));
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
}
