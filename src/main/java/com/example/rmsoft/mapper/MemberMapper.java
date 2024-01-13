package com.example.rmsoft.mapper;

import com.example.rmsoft.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    void signupMember(MemberDto memberDto);

    boolean idCheck(String memberId);

    MemberDto memberInformation(String member_id);

}
