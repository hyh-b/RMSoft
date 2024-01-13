package com.example.rmsoft.web;

import com.example.rmsoft.dto.MemberDto;
import com.example.rmsoft.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //회원가입 api
    @PostMapping("/api/signup")
    public ResponseEntity<String> signupMember(@RequestBody MemberDto member) {

        memberService.signupMember(member);

        return ResponseEntity.ok("회원가입에 성공하였습니다.");
    }
    //아이디 중복 확인 api
    @GetMapping("/api/signup/idCheck")
    public boolean idCheck(@RequestParam String memberId) {
        return memberService.idCheck(memberId);
    }
}
