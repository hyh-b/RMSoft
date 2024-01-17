package com.example.rmsoft.mapper;

import com.example.rmsoft.dto.DashboardResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DashboardMapper {

    List<DashboardResponseDto> dashboardResponse(String MemberId);
}
