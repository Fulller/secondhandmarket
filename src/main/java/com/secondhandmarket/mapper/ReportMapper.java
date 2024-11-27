package com.secondhandmarket.mapper;

import com.secondhandmarket.dto.report.ReportResponse;
import com.secondhandmarket.model.Report;
import com.secondhandmarket.model.User;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ReportMapper {
    ReportResponse.UserDTO toUserDTO(User user);
    List<ReportResponse> toReportResponse(List<Report> reports);
}
