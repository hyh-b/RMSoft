package com.example.rmsoft.web;

import com.example.rmsoft.dto.MemberDto;
import com.example.rmsoft.jwtToken.JwtTokenUtil;
import com.example.rmsoft.service.EmailService;
import com.example.rmsoft.service.MemberService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    private final EmailService emailService;

    private final JwtTokenUtil jwtTokenUtil;

    //회원가입 api
    @PostMapping("/api/member/signup")
    public ResponseEntity<String> signupMember(@RequestBody MemberDto member) {

        memberService.signupMember(member);

        return ResponseEntity.ok("회원가입에 성공하였습니다.");
    }
    //아이디 중복 확인 api
    @GetMapping("/api/member/id/exists")
    public boolean idCheck(@RequestParam String memberId) {
        return memberService.idCheck(memberId);
    }

    // 아이디 찾기 api
    @GetMapping("/api/member/id")
    public List<String> findId(@RequestParam String email) {
        return memberService.findId(email);
    }

    // 비밀번호 찾기 api
    @PostMapping("/api/member/password/recovery")
    public ResponseEntity<?> findPassword(@RequestParam String memberId, @RequestParam String email) throws MessagingException {
        boolean checkIdEmail = memberService.findPassword(memberId, email);
        if(!checkIdEmail){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 사용자 정보입니다");
        }

        emailService.sendPasswordResetEmail(memberId, email);
        return ResponseEntity.ok("비밀번호 재설정 이메일을 전송했습니다");
    }

    // 비밀번호 재설정 api
    @PatchMapping("/api/member/password/reset")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String,Object> requestBody) {
        String token = (String) requestBody.get("token");
        String password = (String) requestBody.get("password");

        String memberId = jwtTokenUtil.validateTokenAndGetMemberId(token);
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청입니다");
        }

        memberService.resetPassword(memberId, password);
        return ResponseEntity.ok("비밀번호가 재설정되었습니다");
    }

}
