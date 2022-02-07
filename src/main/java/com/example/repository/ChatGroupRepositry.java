package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.domain.user.model.ChatGroup;



public interface ChatGroupRepositry  extends JpaRepository<ChatGroup, String> {

    public ChatGroup findById(Integer id);
}
