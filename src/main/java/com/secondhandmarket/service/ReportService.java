package com.secondhandmarket.service;

import com.secondhandmarket.dto.report.ReportRequest;
import com.secondhandmarket.enums.UserStatus;
import com.secondhandmarket.exception.AppException;
import com.secondhandmarket.model.RefreshToken;
import com.secondhandmarket.model.Report;
import com.secondhandmarket.model.Review;
import com.secondhandmarket.model.User;
import com.secondhandmarket.repository.*;
import com.secondhandmarket.security.SecurityUtil;
import com.secondhandmarket.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
    private final ReviewRepository reviewRepository;

    @Transactional
    public void post(ReportRequest request) {
        User accused = securityUtil.getCurrentUser();

        User defendant = userRepository.findById(request.getDefendant_id())
                .orElseThrow(() -> new AppException( HttpStatus.BAD_REQUEST ,"Defendant does not exist","report-e-01"));

        if(!orderRepository.existsBySellerAndBuyer(defendant,accused)){
            throw new AppException(HttpStatus.BAD_REQUEST,"No right to report","report-e-02");
        }
        Review review = reviewRepository.findById(request.getReviewId())
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,"Review does not exist","report-e-03"));

        review.setIsReport(true);

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
        reviewRepository.save(review);
        //GUI MAIL
        emailService.sendEmailReport(defendant.getEmail(), report);
    }
}
