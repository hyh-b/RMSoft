package com.example.rmsoft.web;

import com.example.rmsoft.jwtToken.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping("/")
    public String index(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            String memberName = authentication.getName();
            model.addAttribute("memberName",memberName);
        }
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(){
        return "dashboard";
    }

    @GetMapping("/serviceApplication")
    public String serviceApplication(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            String memberName = authentication.getName();
            model.addAttribute("memberName",memberName);
        }
        return "service-application";
    }

    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }

    @GetMapping("/signin")
    public String signin(){
        return "signin";
    }

    @GetMapping("/resetPassword")
    public String resetPassword(@RequestParam String token, Model model) {
        String memberId = jwtTokenUtil.validateTokenAndGetMemberId(token);
        if (memberId == null) {
            return "redirect:/signin?expiredPage";
        }
        return "reset-password";
    }
}
