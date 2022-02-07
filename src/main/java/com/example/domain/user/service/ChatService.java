package com.example.domain.user.service;

import java.util.List;

import com.example.domain.user.model.Chat;

public interface ChatService {


    public void create(Chat chat);

    public List<Chat> getChatsByChatGroupId(Integer chatGroupId);
}
