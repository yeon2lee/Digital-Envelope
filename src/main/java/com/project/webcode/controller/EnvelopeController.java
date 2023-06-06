package com.project.webcode.controller;

import com.project.webcode.domain.EnvelopeDto;
import com.project.webcode.service.EnvelopeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class EnvelopeController {

    private final EnvelopeService envelopeService;

    @GetMapping("/envelopes/new")
    public String createForm(Model model) {
        model.addAttribute("envelopeForm", new EnvelopeDto());
        return "envelope/createEnvelopeForm";
    }

    @PostMapping("/envelopes/new")
    public void create() {

    }

    @GetMapping("/envelopes")
    public void findEnvelope() {

    }


}
