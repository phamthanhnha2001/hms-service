package com.hms.demo.service.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.AbstractCondition;
import org.modelmapper.Condition;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class MappingHelper {

    @Autowired
    private ModelMapper modelMapper;
    // Chuyển đổi danh sách đối tượng từ một kiểu khác sang một kiểu khác bằng cách sử dụng ModelMapper
    public <T, H> List<T> mapList(List<H> inputData, Class<T> clazz) {
        return CollectionUtils.isEmpty(inputData) ?
                Collections.emptyList() :
                inputData.stream()
                        .map(i -> modelMapper.map(i, clazz))
                        .collect(Collectors.toList());
    }
    // Chuyển đổi một đối tượng từ kiểu này sang kiểu khác bằng ModelMapper
    public <T, H> T map(H inputData, Class<T> clazz) {
        return modelMapper.map(inputData, clazz);
    }
    // Chuyển đổi và ánh xạ thuộc tính từ nguồn tới đích nếu nguồn không null và không rỗng
    public <T, H> void mapIfSourceNotNullAndStringNotBlank(T source, H destination) {
        modelMapper.getConfiguration()
                .setPropertyCondition(isStringNotBlank());
        modelMapper.map(source, destination);
    }
    // Sao chép các thuộc tính từ đối tượng nguồn đến đối tượng đích
    public <T, H> void copyProperties(T source, H destination) {
        modelMapper.map(source, destination);
    }
    // Xác định điều kiện cho việc kiểm tra xem một giá trị có là một chuỗi rỗng hay không
    private Condition<Object, Object> isStringNotBlank() {
        return new AbstractCondition<>() {
            @Override
            public boolean applies(MappingContext<Object, Object> context) {
                if (context.getSource() instanceof String) {
                    return StringUtils.isNotBlank(String.valueOf(context.getSource()));
                }
                return Objects.nonNull(context.getSource());
            }
        };
    }

}
