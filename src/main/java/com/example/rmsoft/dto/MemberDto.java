package com.example.rmsoft.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {
    private String memberId;
    private String password;
    private String name;
    private String email;
    private String address;
    private String phone;
    private String createDate;
    private String role;

}
