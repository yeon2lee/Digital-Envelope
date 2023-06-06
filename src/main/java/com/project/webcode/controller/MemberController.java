package com.project.webcode.controller;

import com.project.webcode.domain.KeyGenerateReq;
import com.project.webcode.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/keys/new")
    public String createForm(Model model) {
        model.addAttribute("keyForm", new KeyGenerateReq());
        return "key/createKeyForm";
    }

    @PostMapping("/keys/new")
    public String create(@Valid KeyGenerateReq keyForm, BindingResult result) {
        if (result.hasErrors()) {
            return "key/createKeyForm";
        }
        KeyGenerateReq keyGenerateReq = new KeyGenerateReq(keyForm.getName(), keyForm.getSecretKeyPath(), keyForm.getPublicKeyPath(), keyForm.getPrivateKeyPath());
        memberService.saveKeys(keyGenerateReq);
        return "redirect:/";
    }
}
