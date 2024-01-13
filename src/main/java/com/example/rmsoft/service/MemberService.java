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

}
