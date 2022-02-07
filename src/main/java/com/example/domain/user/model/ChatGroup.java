package com.example.domain.user.model;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="chat_group")
public class ChatGroup {

    @Id
    @Column(name = "id", nullable = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @ManyToOne
    @JoinColumn(name = "create_username", nullable = false)
    private MUser createUser;

    @Column(name = "update_date", nullable = false)
    private Timestamp updateDate;

    @ManyToOne
    @JoinColumn(name = "update_username", nullable = false)
    private MUser updateUser;

    @Column(name = "is_delete", nullable = true)
    private boolean isDelete;

}
