package com.example.form;



import java.sql.Timestamp;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.example.domain.user.model.MUser;

import lombok.Data;

@Data
public class ChatForm {

    private Integer id;

    private Integer chatGroupId;

    @NotBlank(groups = ValidGroup1.class)
    @Length(min = 0, max = 200, groups = ValidGroup2.class)
    private String content;

    private Timestamp createDate;

    private MUser createUser;

}
