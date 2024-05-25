package com.example.controller;

import com.example.domain.User;
import com.example.form.UserForm;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private HttpSession session;

    @GetMapping("")
    public String index(Model model) {
        UUID token = UUID.randomUUID();
        List<UUID> tokenList = (List<UUID>) session.getAttribute("tokenList");
        if (tokenList == null) {
            tokenList = new ArrayList<>();
        }
        tokenList.add(token);
        model.addAttribute("token", token);
        session.setAttribute("tokenList", tokenList);
        return "index";
    }

    @PostMapping("/sendName")
    public String sendName(UserForm form, Model model){
        User user = new User();
        user.setName(form.getName());
        List<UUID> sessionTokenList = (List<UUID>) session.getAttribute("tokenList");

        if (sessionTokenList.contains(form.getToken())){
            System.out.println("if文が実行されました");
            sessionTokenList.remove(form.getToken());
            System.out.println("データベースに" + form.getName() + "さんを格納しました");
            model.addAttribute("result", "送信成功!");
        }else{
            model.addAttribute("result", "送信失敗!すでに送信済みのリクエストです。");
        }

        System.out.println("resultページに遷移します");
        return "result";
    }
}
