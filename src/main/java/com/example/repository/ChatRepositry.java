package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.domain.user.model.Chat;



public interface ChatRepositry  extends JpaRepository<Chat, String> {

    @Query("select c"
            + " from Chat c"
            + " where c.chatGroup.id = :chatGroupId"
            + " order by c.createDate")
    public List<Chat> getChatsByChatGroupId(@Param("chatGroupId")Integer chatGroupId);
}
