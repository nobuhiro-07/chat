package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.domain.user.model.MUser;
import com.example.domain.user.service.UserService;


@Controller
public class TopPageController {

    @Autowired
    private UserService userService;


    /** TOPページ */
    @GetMapping("/")
    public String showTop(Model model,
            @AuthenticationPrincipal UserDetails userDetails) {

        // ユーザー名に紐づくユーザー情報を取得
        MUser loginUser = userService.getLoginUser(userDetails.getUsername());
        // Modelに登録
        model.addAttribute("user", loginUser);

        // ユーザー一覧画面を表示
        return "user/toppage";
    }
}
