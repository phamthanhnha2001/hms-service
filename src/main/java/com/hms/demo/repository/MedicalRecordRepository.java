package com.hms.demo.repository;

import com.hms.demo.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Integer> {
    // Tìm hồ sơ khám bệnh dựa vào tên bác sĩ khám
    List<MedicalRecord> findByStaff_Account_Username(String doctorUsername);
    // Tìm hồ sơ khám bệnh dựa vào tên bệnh nhân
    List<MedicalRecord> findByPatient_Account_Username(String patientUsername);
}
