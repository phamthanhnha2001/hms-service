package com.hms.demo.repository;

import com.hms.demo.model.Services;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicesRepository extends JpaRepository<Services, Integer> {
    // Tìm kiếm bộ phận thực hiện dịch vụ
    List<Services> findByDepartment_Id(Integer departmentId);
}
