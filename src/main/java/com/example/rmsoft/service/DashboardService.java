package com.example.rmsoft.service;

import com.example.rmsoft.dto.DashboardResponseDto;
import com.example.rmsoft.dto.MemberDto;
import com.example.rmsoft.mapper.DashboardMapper;
import com.example.rmsoft.security.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DashboardService {

    private final DashboardMapper dashboardMapper;
    private final CustomUserDetailService customUserDetailService;
    // 대시보드 구성 요소
    public List<DashboardResponseDto> dashboardResponse() {
        MemberDto memberDto = customUserDetailService.getMemberDto();
        return dashboardMapper.dashboardResponse(memberDto.getMemberId());
    }
}
