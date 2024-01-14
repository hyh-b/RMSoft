package com.example.rmsoft.mapper;

import com.example.rmsoft.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper {
    void signupMember(MemberDto memberDto);

    boolean idCheck(String memberId);

    MemberDto memberInformation(String member_id);

    List<String> findId(String email);

    boolean findPassword(@Param("memberId") String memberId, @Param("email") String email);

    void resetPassword(@Param("memberId") String memberId, @Param("password") String password);

}
