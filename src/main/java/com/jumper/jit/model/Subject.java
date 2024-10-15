package com.jumper.jit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;


@Entity
@DynamicUpdate
@Data
@AllArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Length(min = 5,max = 50,message = "字符数介于5与50之间")
    private String subjectTitle;
    private int articleSum;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Subject() {

    }

    @PrePersist
    public void beforeSave(){
        createdAt = LocalDateTime.now();
    }
}
