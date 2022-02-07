package com.example.domain.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.user.model.Chat;
import com.example.domain.user.service.ChatService;
import com.example.repository.ChatRepositry;

@Service
@Primary
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepositry repository;

    /** チャットグループ登録 */
    @Transactional
    @Override
    public void create(Chat chat) {

        repository.save(chat);
    }

    /** チャットグループのメッセージ取得 */
    public List<Chat> getChatsByChatGroupId(Integer chatGroupId){
        return repository.getChatsByChatGroupId(chatGroupId);
    }
}
