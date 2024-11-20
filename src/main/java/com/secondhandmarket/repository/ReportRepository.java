package com.secondhandmarket.repository;

import com.secondhandmarket.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, String> {
    List<Report> findByDefendantId(String defendantId);
}
