package com.hms.demo.service.impl;

import com.hms.demo.model.Position;
import com.hms.demo.repository.PositionRepository;
import com.hms.demo.service.PositionService;
import com.hms.demo.service.utils.MappingHelper;
import com.hms.demo.web.dto.request.PositionReq;
import com.hms.demo.web.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {
    private final PositionRepository positionRepository;
    private final MappingHelper mappingHelper;
    // Lấy danh sách tất cả các vị trí
    @Override
    public List<Position> getAllPosition() {
        return positionRepository.findAll();
    }
    // Lấy thông tin vị trí làm việc dựa vào PositionId
    @Override
    public Position getPositionById(Integer id) {
        return positionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Position.class.getName()
                        , id.toString()));
    }
    // Tạo mới 1 vị trí làm việc dựa trên thông tin từ positionReq
    @Override
    public void createPosition(PositionReq positionReq) {
        // Ánh xạ thông tin từ positionReq thành một đối tượng Position
        var res = mappingHelper.map(positionReq, Position.class);
        positionRepository.save(res);
    }
    // Cập nhật thông tin về vị trí làm việc dựa trên id và thông tin từ PositionReq
    @Override
    public void updatePosition(Integer id, PositionReq positionReq) {
        // Tìm một vị trí làm việc dựa trên id và cập nhật thông tin từ positionReq
        var Position = positionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Position.class.getName()
                        , id.toString()));
        mappingHelper.copyProperties(positionReq, Position);
        positionRepository.save(Position);
    }
    // Xóa thông tin vị trí làm việc dựa trên id
    @Override
    public void removePosition(Integer id) {
        positionRepository.deleteById(id);
    }
}
