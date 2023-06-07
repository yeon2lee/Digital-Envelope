package com.project.webcode.controller;

import com.project.webcode.domain.*;
import com.project.webcode.service.EnvelopeService;
import com.project.webcode.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class EnvelopeController {

    private final EnvelopeService envelopeService;
    private final MemberService memberService;

    @GetMapping("/envelopes/new")
    public String createForm(Model model) {
        model.addAttribute("envelopeForm", new EnvelopeReq());
        List<Member> members = envelopeService.findMembers();
        model.addAttribute("members", members);
        return "envelope/createEnvelopeForm";
    }

    @PostMapping("/envelopes/new")
    public String create(@Valid EnvelopeReq envelopeForm, BindingResult result) {
        if (result.hasErrors()) {
            return "envelope/createEnvelopeForm";
        }
        EnvelopeReq envelopeReq = new EnvelopeReq(envelopeForm.getSender(), envelopeForm.getReceiver(), envelopeForm.getMessage());
        envelopeService.createEnvelope(envelopeReq);
        return "redirect:/";
    }

    @GetMapping("/envelopes")
    public String findEnvelope(Model model) {
        return "envelope/searchEnvelopeForm";
    }

    @PostMapping( "/envelopes")
    public String openEnvelope(@RequestParam("name") String name, Model model) throws NoSuchAlgorithmException, InvalidKeySpecException {

        EnvelopeRes envelopeRes = envelopeService.openEnvelope(name);
        model.addAttribute("envelopeRes", envelopeRes);

        return "envelope/envelopes";
    }

}
