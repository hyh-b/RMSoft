package com.example.rmsoft.security;

import com.example.rmsoft.dto.MemberDto;
import com.example.rmsoft.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    // 로그인한 유저의 memberDto값
    public MemberDto getMemberDto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;

            return customUserDetails.getDto();
        }else {

            return new MemberDto();
        }
    }
}
