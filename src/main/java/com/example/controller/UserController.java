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

        System.out.println("GETリクエストを受け取りました");
        System.out.println("新しいトークンを発行し、セッションスコープに格納します");
        System.out.println("発行されたトークン:" + token);
        System.out.println("発行されたトークンをフォームのhiddenフィールドに設定します");
        System.out.println("---------");
        return "index";
    }

    @PostMapping("/sendName")
    public String sendName(UserForm form, Model model){
        User user = new User();
        user.setName(form.getName());
        List<UUID> sessionTokenList = (List<UUID>) session.getAttribute("tokenList");

        System.out.println("POSTリクエストを受け取りました");
        System.out.println("送信されたフォームのトークン: " + form.getToken());

        if (sessionTokenList.contains(form.getToken())){
            System.out.println("トークンがセッションスコープのトークンリストに存在します");
            System.out.println("削除前のセッションスコープのトークンリスト" + sessionTokenList);
            System.out.println("セッションスコープのトークンリストからフォームのトークンと一致するものを削除");

            sessionTokenList.remove(form.getToken());

            System.out.println("削除後のセッションスコープのトークンリスト" + sessionTokenList);
            System.out.println(form.getName() + " さんをデータベースに保存しました");
            model.addAttribute("result", "送信成功!");
        }else{
            System.out.println("トークンがセッションスコープのリストに存在しません");
            model.addAttribute("result", "送信失敗!すでに送信済みのリクエストです。");
        }

        System.out.println("resultページに遷移します");
        System.out.println("---------");
        return "result";
    }
}
