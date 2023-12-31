package com.hms.demo.service.impl;

import com.hms.demo.model.MedicalDepartment;
import com.hms.demo.repository.MedicalDepartmentRepository;
import com.hms.demo.service.MedicalDepartmentService;
import com.hms.demo.service.utils.MappingHelper;
import com.hms.demo.web.dto.request.MedicalDepartmentReq;
import com.hms.demo.web.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MedicalDepartmentServiceImpl implements MedicalDepartmentService {
    private final MedicalDepartmentRepository medicalDepartmentRepository;
    private final MappingHelper mappingHelper;
    // Lấy danh sách tất cả các bộ phận y tế
    @Override
    public List<MedicalDepartment> getListMedicalDepartment() {
        return medicalDepartmentRepository.findAll();
    }
    // Lấy thông tin một bộ phận y tế dựa trên id
    @Override
    public MedicalDepartment getMedicalDepartment(Integer id) {
        return medicalDepartmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MedicalDepartment.class.getName()
                        , id.toString()));
    }
    // Tạo một phận y tế mới dựa trên thông tin từ MedicalDepartmentReq
    @Override
    public void createMedicalDepartment(MedicalDepartmentReq medicalDepartmentReq) {
        var res = mappingHelper.map(medicalDepartmentReq, MedicalDepartment.class);
        medicalDepartmentRepository.save(res);
    }
    // Cập nhật thông tin bộ phận y tế dựa trên id và thông tin từ MedicalDepartmentReq
    @Override
    public void updateMedicalDepartment(Integer id, MedicalDepartmentReq medicalDepartmentReq) {
        var MedicalDepartment = medicalDepartmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MedicalDepartment.class.getName()
                        , id.toString()));
        mappingHelper.mapIfSourceNotNullAndStringNotBlank(medicalDepartmentReq, MedicalDepartment);
        medicalDepartmentRepository.save(MedicalDepartment);
    }
    // Xóa bộ phận y tế dựa trên id
    @Override
    public void removeMedicalDepartment(Integer id) {
        medicalDepartmentRepository.deleteById(id);
    }
}
