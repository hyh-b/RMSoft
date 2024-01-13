package com.example.rmsoft.security;

import com.example.rmsoft.dto.MemberDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
public class CustomUserDetails implements UserDetails {
    private MemberDto dto;
    public CustomUserDetails(MemberDto dto) {
        this.dto = dto;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+dto.getRole()));
    }

    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public String getPassword() {
        return dto.getPassword();
    }

    @Override
    public String getUsername() {
        return dto.getMemberId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
