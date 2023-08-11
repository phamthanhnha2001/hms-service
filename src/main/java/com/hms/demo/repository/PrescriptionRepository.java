package com.hms.demo.repository;

import com.hms.demo.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
    // Tìm đơn thuốc dựa vào MedicalRecordId
    Optional<Prescription> findByMedicalRecord_Id(Integer medicalRecordId);
}
