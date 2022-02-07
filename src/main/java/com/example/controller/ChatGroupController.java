package com.example.controller;

import java.sql.Timestamp;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.domain.user.model.ChatGroup;
import com.example.domain.user.model.MUser;
import com.example.domain.user.service.ChatGroupService;
import com.example.domain.user.service.UserService;
import com.example.form.ChatGroupForm;
import com.example.form.GroupOrder;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/chatGroup")
@Slf4j
public class ChatGroupController {

    @Autowired
    private ChatGroupService chatGroupService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    // グループ作成画面を表示
    @GetMapping("/new")
    public String getNewGroup(@ModelAttribute ChatGroupForm form) {
        return "chatGroup/new";
    }

    /** チャットグループ登録処理 */
    @PostMapping("/create")
    public String createGroup(Model model, Locale locale,
            @ModelAttribute @Validated(GroupOrder.class) ChatGroupForm form,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails,
            HttpServletRequest request) {

        // 入力チェック結果
        if (bindingResult.hasErrors()) {
            // NG:チャットグループ登録画面に戻ります
            return getNewGroup(form);
        }

        log.info(form.toString());

        // formをChatGroupクラスに変換
        ChatGroup chatGroup = modelMapper.map(form, ChatGroup.class);

        // 現在日時を取得
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        chatGroup.setCreateDate(currentTime); // 登録日時を設定
        chatGroup.setUpdateDate(currentTime); // 更新日時を設定

        // ユーザー名に紐づくユーザー情報を取得
        MUser loginUser = userService.getLoginUser(userDetails.getUsername());
        chatGroup.setCreateUser(loginUser); // 登録ユーザーを設定
        chatGroup.setUpdateUser(loginUser); // 更新ユーザーを設定
        // チャットグループ登録
        ChatGroup createResult = chatGroupService.create(chatGroup);


        HttpRequest req = new ServletServerHttpRequest(request);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpRequest(req);

        String url = builder.path("/chatSpace/"+createResult.getId()).build().toUri().toString();
        url = url.replace("/chatGroup/create", "");
        form.setUrl(url);

        return getNewGroup(form);
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
