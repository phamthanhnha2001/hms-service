package com.hms.demo.service.impl;

import com.hms.demo.model.MedicalDepartment;
import com.hms.demo.model.Services;
import com.hms.demo.repository.MedicalDepartmentRepository;
import com.hms.demo.repository.ServicesRepository;
import com.hms.demo.service.ServicesService;
import com.hms.demo.service.utils.MappingHelper;
import com.hms.demo.web.dto.ServicesDto;
import com.hms.demo.web.dto.request.ServicesReq;
import com.hms.demo.web.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ServicesServiceImpl implements ServicesService {
    private final ServicesRepository servicesRepository;
    private final MedicalDepartmentRepository departmentRepository;
    private final MappingHelper mappingHelper;
    // Lấy ra toàn bộ danh sách dịch vụ
    @Override
    public List<ServicesDto> getAllServices() {
        return servicesRepository.findAll().stream()
                .map(e -> mappingHelper.map(e, ServicesDto.class))
                .collect(Collectors.toList());
    }
    // Lấy ra thông tin dịch vụ dựa vào id
    @Override
    public ServicesDto getServicesById(Integer id) {
        var res = servicesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Services.class.getName()
                        , id.toString()));
        return mappingHelper.map(res, ServicesDto.class);
    }
    // Tạo một dịch vụ mới dựa vào thông tin từ ServiceReq
    @Override
    public void createServices(ServicesReq servicesReq) {
        var department = departmentRepository.findById(servicesReq.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException(MedicalDepartment.class.getName()
                        , servicesReq.getDepartmentId().toString()));
        var res = mappingHelper.map(servicesReq, Services.class);
        res.setDepartment(department);
        servicesRepository.save(res);
    }
    // Cập nhật thông tin dịch vụ dựa vào id và thông tin từ serviceReq
    @Override
    public void updateServices(Integer id, ServicesReq servicesReq) {
        var Services = servicesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Services.class.getName()
                        , id.toString()));
        var department = departmentRepository.findById(servicesReq.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException(MedicalDepartment.class.getName()
                        , servicesReq.getDepartmentId().toString()));
        mappingHelper.copyProperties(servicesReq, Services);
        Services.setDepartment(department);
        servicesRepository.save(Services);
    }
    // Xóa dịch vụ dựa vào id
    @Override
    public void removeServices(Integer id) {
        servicesRepository.deleteById(id);
    }
    // Lấy ra danh sách dịch vụ mà 1 bộ phận thực hiện dựa vào departmentId
    @Override
    public List<ServicesDto> getServicesByDepartment(Integer departmentId) {
        return servicesRepository.findByDepartment_Id(departmentId)
                .stream().map(e -> mappingHelper.map(e, ServicesDto.class))
                .collect(Collectors.toList());
    }
}
