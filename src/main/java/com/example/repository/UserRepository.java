package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.domain.user.model.MUser;

public interface UserRepository extends JpaRepository<MUser, String> {

    /** ログインユーザー検索 */
    @Query("select user"
            + " from MUser user"
            + " where userId = :userId"
            + " and isDelete=false")
    public MUser findLoginUser(@Param("userId") String userId);

    /** ユーザー更新 */
    @Modifying
    @Query("update MUser"
            + " set"
            + "   password = :password"
            + "   , userName = :userName"
            + " where"
            + "   userId = :userId")
    public Integer updateUser(@Param("userId") String userId,
            @Param("password") String password,
            @Param("userName") String userName);

    /** ユーザー更新 */
    @Modifying
    @Query("update MUser"
            + " set"
            + "   isDelete = true"
            + " where"
            + "   userId = :userId")
    public Integer deleteUser(@Param("userId") String userId);

    /** チャットグループに参加済みユーザー取得 */
    @Query("select user"
            + " from MUser user"
            + " where isDelete=false "
            + " AND exists("
            + " select chat"
            + " from Chat chat"
            + " where chat.chatGroup.id =:chatGroupId AND chat.createUser = user"
            + " )")
    public List<MUser> getChatCreateUser(@Param("chatGroupId") Integer chatGroupId);
}
