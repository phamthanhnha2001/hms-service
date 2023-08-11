package com.hms.demo.service.impl;

import com.hms.demo.model.Medicine;
import com.hms.demo.repository.MedicineRepository;
import com.hms.demo.service.MedicineService;
import com.hms.demo.service.utils.MappingHelper;
import com.hms.demo.web.dto.MedicineDto;
import com.hms.demo.web.dto.request.MedicineCriteria;
import com.hms.demo.web.dto.request.MedicineReq;
import com.hms.demo.web.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {
    // Khai báo các Repostitory và các tiện ích cần thiết
    private final MedicineRepository medicineRepository;
    private final MappingHelper mappingHelper;
    // Tạo mới thông tin 1 loại thuốc dựa trên medicineReq
    @Override
    public void createMedicine(MedicineReq medicineReq) {
        // Ánh xạ thông tin từ medicineReq thành một đối tượng Medicine và lưu vào cơ sở dữ liệu
        var medicine = mappingHelper.map(medicineReq, Medicine.class);
        medicineRepository.save(medicine);
    }
    // Lấy danh sách thông tin về loại thuốc dựa trên MedicineCriteria.
    @Override
    public List<MedicineDto> getListMedicine(MedicineCriteria medicineCriteria) {
        // Lấy tất cả loại thuốc từ cơ sở dữ liệu, ánh xạ chúng thành đối tượng MedicineDto và trả về danh sách
        return medicineRepository.findAll()
                .stream().map(e -> mappingHelper.map(e, MedicineDto.class))
                .collect(Collectors.toList());
    }
    // Lấy thông tin về một loại thuốc dựa trên medicineId
    @Override
    public MedicineDto getMedicine(Integer medicineId) {
        var medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new EntityNotFoundException(Medicine.class.getName()
                        , medicineId.toString()));
        return mappingHelper.map(medicine, MedicineDto.class);
    }
    // Cập nhật thông tin về một loại thuốc dựa trên medicineId và thông tin từ MedicineReq
    @Override
    public void updateMedicine(Integer medicineId, MedicineReq medicineReq) {
        var medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new EntityNotFoundException(Medicine.class.getName()
                        , medicineId.toString()));
        mappingHelper.copyProperties(medicineReq, medicine);
        medicineRepository.save(medicine);
    }
    // Xóa thông tin một loại thuốc
    @Override
    public void removeMedicine(Integer medicineId) {
        medicineRepository.deleteById(medicineId);
    }
}
