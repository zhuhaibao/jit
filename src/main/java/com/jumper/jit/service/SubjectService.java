package com.jumper.jit.service;

import com.jumper.jit.dto.SubjectDTO;
import com.jumper.jit.model.Subject;
import org.springframework.data.domain.Page;


public interface SubjectService {
    Subject save(Subject subject);
    Subject updateSubject(SubjectDTO subject);
    void delete(Integer id);
    Page<Subject> findSubjectBy(SubjectDTO dto);
}
