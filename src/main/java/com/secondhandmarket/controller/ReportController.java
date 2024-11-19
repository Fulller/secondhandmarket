package com.secondhandmarket.controller;

import com.secondhandmarket.dto.api.ApiResponse;
import com.secondhandmarket.dto.report.ReportRequest;
import com.secondhandmarket.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {
    final private ReportService reportService;
    //post to cao
    @PostMapping("/post")
    ResponseEntity<ApiResponse> post(@RequestBody ReportRequest request){
        reportService.post(request);
        ApiResponse apiResponse = ApiResponse.builder()
                .code("report-s-01")
                .message("report success")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


}
