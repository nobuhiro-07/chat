package com.example.domain.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.user.model.ChatGroup;
import com.example.domain.user.service.ChatGroupService;
import com.example.repository.ChatGroupRepositry;

@Service
@Primary
public class ChatGroupServiceImpl implements ChatGroupService {

    @Autowired
    private ChatGroupRepositry repository;

    /** チャットグループ登録 */
    @Transactional
    @Override
    public ChatGroup create(ChatGroup chatGroup) {
        // insert
        ChatGroup createResult = repository.save(chatGroup);
        return createResult;
    }

    /** チャットグループ取得 */
    @Override
    public ChatGroup findById(Integer id) {

        ChatGroup chatGroup = repository.findById(id);
        return chatGroup;
    }
}
