package com.hms.demo.repository;

import com.hms.demo.model.ServicesOfMedicalTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicesOfMedicalTestRepository extends JpaRepository<ServicesOfMedicalTest, Integer> {
    // Tìm dịch vụ của phiếu chỉ định
    List<ServicesOfMedicalTest> findByMedicalTest_Id(Integer medicalTestId);
}
