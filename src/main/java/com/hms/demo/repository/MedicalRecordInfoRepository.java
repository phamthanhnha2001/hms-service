package com.hms.demo.repository;

import com.hms.demo.model.MedicalRecordInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicalRecordInfoRepository extends JpaRepository<MedicalRecordInformation, Integer> {
    // Tìm kiếm thông tin hồ sơ khám bệnh dựa vào mã hồ sơ (MedicalRecordId)
    Optional<MedicalRecordInformation> findByMedicalRecord_Id(Integer medicalRecordId);
}
