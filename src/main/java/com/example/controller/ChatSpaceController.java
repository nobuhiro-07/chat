package com.example.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.user.model.Chat;
import com.example.domain.user.model.ChatGroup;
import com.example.domain.user.model.MUser;
import com.example.domain.user.service.ChatGroupService;
import com.example.domain.user.service.ChatService;
import com.example.domain.user.service.UserService;
import com.example.form.ChatForm;
import com.example.form.GroupOrder;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ChatSpaceController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatGroupService chatGroupService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    // チャットスペース画面を表示
    @RequestMapping("/chatSpace/{chatGroupId:.+}")
    public String showChatSpace(@ModelAttribute ChatForm chatForm,
            @PathVariable("chatGroupId") Integer chatGroupId,
            Model model) {

        chatForm.setChatGroupId(chatGroupId);

        ChatGroup chatGroup = chatGroupService.findById(chatGroupId);
        String chatGroupName = chatGroup.getName();

        model.addAttribute("chatGroupName", chatGroupName);

        // ユーザー一覧を取得
        List<MUser> userList = userService.getChatCreateUser(chatGroupId);
        model.addAttribute("userList", userList);

        // 投稿一覧を取得
        List<Chat> chatList = chatService.getChatsByChatGroupId(chatGroupId);
        model.addAttribute("chatList", chatList);
        return "chatSpace/list";
    }

    /** チャットグループ登録処理 */
    @PostMapping("/chatSpace/post")
    public String postChat(Model model, Locale locale,
            @ModelAttribute @Validated(GroupOrder.class) ChatForm chatForm,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails,
            HttpServletRequest request) {

        Integer chatGroupId = chatForm.getChatGroupId();
        // 入力チェック結果
        if (bindingResult.hasErrors()) {
            // NG:チャットスペース画面に戻ります
            return showChatSpace(chatForm, chatGroupId, model);
        }

        log.info(chatForm.toString());

        // formをChatクラスに変換
        Chat chat = modelMapper.map(chatForm, Chat.class);
        chat.setChatGroup(chatGroupService.findById(chatGroupId));

        // 現在日時を取得
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        chat.setCreateDate(currentTime); // 登録日時を設定
        chat.setUpdateDate(currentTime); // 更新日時を設定

        // ユーザー名に紐づくユーザー情報を取得
        MUser loginUser = userService.getLoginUser(userDetails.getUsername());
        chat.setCreateUser(loginUser); // 登録ユーザーを設定
        chat.setUpdateUser(loginUser); // 更新ユーザーを設定
        // チャットのメッセージを登録
        chatService.create(chat);


        return "redirect:/chatSpace/" + chatGroupId;
    }

    /** データベース関連の例外処理 */
    @ExceptionHandler(DataAccessException.class)
    public String dataAccessExceptionHandler(DataAccessException e, Model model) {

        // 空文字をセット
        model.addAttribute("error", "");

        // メッセージをModelに登録
        model.addAttribute("message", "NewGroupControllerで例外が発生しました");

        // HTTPのエラーコード（500）をModelに登録
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);

        return "error";
    }

    /** その他の例外処理 */
    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e, Model model) {

        // 空文字をセット
        model.addAttribute("error", "");

        // メッセージをModelに登録
        model.addAttribute("message", "NewGroupControllerで例外が発生しました");

        // HTTPのエラーコード（500）をModelに登録
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);

        return "error";
    }

}
