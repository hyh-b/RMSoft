package com.example.rmsoft.service;

import com.example.rmsoft.dto.MemberDto;
import com.example.rmsoft.mapper.MemberMapper;
import com.example.rmsoft.security.CustomUserDetailService;
import com.example.rmsoft.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberMapper memberMapper;

    private final PasswordEncoder passwordEncoder;

    /*회원가입 메서드*/
    public void signupMember(MemberDto member) {
        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encodedPassword);
        memberMapper.signupMember(member);
    }
    /* 아아디 중복 환인 메서드*/
    public boolean idCheck(String memberId) {
        return memberMapper.idCheck(memberId);
    }

    /* 로그인 시도한 유저 정보 */
    public MemberDto memberInformation(String member_id) {
        return  memberMapper.memberInformation(member_id);
    }

    /* 아이디 찾기 메서드*/
    public List<String> findId(String email) {
        return memberMapper.findId(email);
    }

    /* 비밀번호 찾기 메서드*/
    public boolean findPassword(String memberId, String email) {
        return memberMapper.findPassword(memberId, email);
    }

    /* 비밀번호 재설정 메서드*/
    public void resetPassword(String memberId, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        memberMapper.resetPassword(memberId, encodedPassword);
    }
}
