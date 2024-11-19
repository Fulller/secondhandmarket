package com.secondhandmarket.service;

import com.secondhandmarket.dto.report.ReportRequest;
import com.secondhandmarket.enums.UserStatus;
import com.secondhandmarket.exception.AppException;
import com.secondhandmarket.model.Product;
import com.secondhandmarket.model.RefreshToken;
import com.secondhandmarket.model.Report;
import com.secondhandmarket.model.User;
import com.secondhandmarket.repository.*;
import com.secondhandmarket.security.SecurityUtil;
import com.secondhandmarket.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import ognl.Token;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final SecurityUtil securityUtil;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ReportRepository reportRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailService emailService;

    public void post(ReportRequest request) {
        User accused = securityUtil.getCurrentUser();

        User defendant = userRepository.findById(request.getDefendant_id())
                .orElseThrow(() -> new AppException( HttpStatus.BAD_REQUEST ,"Defendant does not exist","report-e-01"));

        if(!orderRepository.existsBySellerAndBuyer(defendant,accused)){
            throw new AppException(HttpStatus.BAD_REQUEST,"No right to report","report-e-02");
        }

        Report report = Report.builder()
                .reportedAt(LocalDateTime.now())
                .accused(accused)
                .defendant(defendant)
                .phone(request.getPhone())
                .reason(request.getReason())
                .build();
        defendant.setReported(defendant.getReported() + 1);
        if(defendant.getReported() >= CommonUtil.getMaxReported() ){
            defendant.setStatus(UserStatus.LOCKED);
            RefreshToken token = refreshTokenRepository.findByUserId(defendant.getId()).orElse(null);
            if(token != null){
                token.setToken(null);
            }
            userRepository.save(defendant);
        }
        reportRepository.save(report);
        //GUI MAIL
        emailService.sendEmailReport(defendant.getEmail(), report);
    }
}
