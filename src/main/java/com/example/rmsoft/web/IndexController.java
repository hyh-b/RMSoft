package com.example.rmsoft.web;

import com.example.rmsoft.dto.ChatMessageDto;
import com.example.rmsoft.dto.DashboardResponseDto;
import com.example.rmsoft.dto.MemberDto;
import com.example.rmsoft.jwtToken.JwtTokenUtil;
import com.example.rmsoft.security.CustomUserDetailService;
import com.example.rmsoft.service.ChatService;
import com.example.rmsoft.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final JwtTokenUtil jwtTokenUtil;
    private final DashboardService dashboardService;
    private final ChatService chatService;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @GetMapping("/")
    public String index(Model model){
        MemberDto memberDto = customUserDetailService.getMemberDto();
        model.addAttribute("memberName", memberDto.getName());
        model.addAttribute("memberId", memberDto.getMemberId());

        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model){
        MemberDto memberDto = customUserDetailService.getMemberDto();
        model.addAttribute("memberName", memberDto.getName());

        List<DashboardResponseDto> dashboardResponseDtoList= dashboardService.dashboardResponse();
        model.addAttribute("dashboardResponseDto", dashboardResponseDtoList);

        return "dashboard";
    }

    @GetMapping("/serviceApplication")
    public String serviceApplication(Model model){
        MemberDto memberDto = customUserDetailService.getMemberDto();
        model.addAttribute("memberName", memberDto.getName());

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

    @GetMapping("/admin")
    public String admin(Model model){
        MemberDto memberDto = customUserDetailService.getMemberDto();
        model.addAttribute("memberId", memberDto.getMemberId());

        List<ChatMessageDto> chatList = chatService.getChatList(memberDto.getMemberId());
        model.addAttribute("chatList", chatList);

        return "admin";
    }

}
