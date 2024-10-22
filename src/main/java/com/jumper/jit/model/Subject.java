package com.jumper.jit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;


@Entity
@DynamicUpdate
@Data
@AllArgsConstructor
@Builder
public class Subject implements Comparable<Subject>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Length(min = 5,max = 50,message = "字符数介于5与50之间")
    private String subjectTitle;
    private int articleSum;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean navigation = false;
    private Integer orderNum;

    public Subject() {

    }

    @Override
    public int compareTo(Subject o) {
        return this.getOrderNum().compareTo(o.getOrderNum());
    }

    @PrePersist
    public void beforeSave(){
        createdAt = LocalDateTime.now();
    }
}
