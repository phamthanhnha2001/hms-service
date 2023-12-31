package com.hms.demo.repository;

import com.hms.demo.model.MedicineOfPrescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineOfPrescriptionRepository extends JpaRepository<MedicineOfPrescription, Integer> {
    // Tìm thuốc của đơn thuốc
    List<MedicineOfPrescription> findByPrescription_Id(Integer prescriptionId);
}
