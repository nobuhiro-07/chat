package com.example.domain.user.service;

import com.example.domain.user.model.ChatGroup;

public interface ChatGroupService {


    public ChatGroup create(ChatGroup chatGroup);

    public ChatGroup findById(Integer id);
}
