package com.jumper.jit.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class TM {
    @NotNull
    private Long id;
    @Length(min = 5,max = 50)
    private String name;
    @Min(19)
    @Max(100)
    private Integer age;
    private LocalDateTime date;

}
