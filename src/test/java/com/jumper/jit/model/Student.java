package com.jumper.jit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Student {
    private long id;
    private String name;
    private int age;
}
