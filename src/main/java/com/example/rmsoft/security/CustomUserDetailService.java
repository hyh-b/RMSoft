package com.example.rmsoft.security;

import com.example.rmsoft.dto.MemberDto;
import com.example.rmsoft.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberDto memberDto = memberService.memberInformation(username);
        if(memberDto == null) {
            throw new UsernameNotFoundException("아이디가 존재하지 않습니다. "+ username);
        }
        
        CustomUserDetails customUserDetails = new CustomUserDetails(memberDto);

        return customUserDetails;
    }
}
