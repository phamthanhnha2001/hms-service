package com.hms.demo.service.impl;

import com.hms.demo.model.MedicalRecord;
import com.hms.demo.model.MedicalTest;
import com.hms.demo.model.Services;
import com.hms.demo.model.ServicesOfMedicalTest;
import com.hms.demo.repository.MedicalRecordRepository;
import com.hms.demo.repository.MedicalTestRepository;
import com.hms.demo.repository.ServicesOfMedicalTestRepository;
import com.hms.demo.repository.ServicesRepository;
import com.hms.demo.service.MedicalTestService;
import com.hms.demo.service.utils.MappingHelper;
import com.hms.demo.web.dto.MedicalTestDto;
import com.hms.demo.web.dto.ServicesDto;
import com.hms.demo.web.dto.ServicesOfMedicalTestDto;
import com.hms.demo.web.dto.request.MedicalTestReq;
import com.hms.demo.web.dto.request.ServicesOfMedicalTestReq;
import com.hms.demo.web.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MedicalTestServiceImpl implements MedicalTestService {
    private final MedicalTestRepository medicalTestRepository;
    private final ServicesRepository servicesRepository;
    private final ServicesOfMedicalTestRepository servicesOfMedicalTestRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final MappingHelper mappingHelper;
    // Tạo phiếu chỉ định mới dựa trên thông tin từ medicalTestReq
    @Override
    public void createMedicalTest(MedicalTestReq medicalTestReq) {
        // Nếu đã có phiếu chỉ định cho hồ sơ khám bệnh này, xóa dữ liệu đi
        if (medicalTestRepository.findByMedicalRecord_Id(medicalTestReq.getMedicalRecordId()).isPresent()) {
            deleteOldData(medicalTestReq);
        }
        // Lấy thông tin hồ sơ khám bệnh liên quan
        var medicalRecord = medicalRecordRepository.findById(medicalTestReq.getMedicalRecordId())
                .orElseThrow(() -> new EntityNotFoundException(MedicalRecord.class.getName(),
                        medicalTestReq.getMedicalRecordId().toString()));
        // Tạo mới phiếu chỉ định và lưu vào hồ sơ khám bệnh
        var medicalTest = mappingHelper.map(medicalTestReq, MedicalTest.class);
        medicalTest.setMedicalRecord(medicalRecord);
        medicalTest.setTime(LocalDateTime.now());
        medicalTestRepository.save(medicalTest);
        // Xử lý và lưu thông tin dịch vụ có liên quan đến phiếu chỉ định
        List<ServicesOfMedicalTest> services = new ArrayList<>();
        float totalPrice = 0F;
        for (ServicesOfMedicalTestReq item : medicalTestReq.getServices()) {
            var serviceOfTest = mappingHelper.map(item, ServicesOfMedicalTest.class);
            var service = servicesRepository.findById(item.getServiceId())
                    .orElseThrow(() -> new EntityNotFoundException(Services.class.getName(), item.getServiceId().toString()));
            serviceOfTest.setMedicalTest(medicalTest);
            serviceOfTest.setServices(service);
            services.add(serviceOfTest);
            totalPrice += service.getPrice() * item.getQuantity();
        }
        servicesOfMedicalTestRepository.saveAll(services);
        // Cập nhật tổng giá trị cho phiếu xét nghiệm
        medicalTest.setTotalPrice(totalPrice);
        medicalTestRepository.save(medicalTest);
    }
    // Lấy thông tin phiếu chỉ định dựa vào medicalRecordId
    @Override
    public MedicalTestDto getByMedicalRecord(Integer medicalRecordId) {
        var medicalTest = medicalTestRepository.findByMedicalRecord_Id(medicalRecordId)
                .orElseThrow(() -> new EntityNotFoundException(MedicalTest.class.getName()
                        , medicalRecordId.toString()));
        var res = mappingHelper.map(medicalTest, MedicalTestDto.class);
        res.setMedicalRecordId(medicalRecordId);

        List<ServicesOfMedicalTestDto> servicesRes = new ArrayList<>();
        servicesOfMedicalTestRepository.findByMedicalTest_Id(medicalTest.getId())
                .forEach(e -> {
                    var service = mappingHelper.map(e, ServicesOfMedicalTestDto.class);
                    service.setServiceDto(mappingHelper.map(e.getServices(), ServicesDto.class));
                    servicesRes.add(service);
                });
        res.setServices(servicesRes);
        return res;
    }
    // Xóa dữ liệu cũ của một phiếu chỉ định
    private void deleteOldData(MedicalTestReq medicalTestReq) {
        var medicalTest = medicalTestRepository.findByMedicalRecord_Id(medicalTestReq.getMedicalRecordId())
                .orElseThrow(() -> new EntityNotFoundException(MedicalTest.class.getName()
                        , medicalTestReq.getMedicalRecordId().toString()));
        var services = servicesOfMedicalTestRepository.findByMedicalTest_Id(medicalTest.getId());
        servicesOfMedicalTestRepository.deleteAll(services);
        medicalTestRepository.delete(medicalTest);
    }
}
