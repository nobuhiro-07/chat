package com.example.form;


import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class ChatGroupForm {


    private Integer id;

    @NotBlank(groups = ValidGroup1.class)
    @Length(min = 0, max = 100, groups = ValidGroup2.class)
    private String name;

    private String url;
}
