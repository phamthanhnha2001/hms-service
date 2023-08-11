package com.hms.demo.service.impl;

import com.hms.demo.model.Authority;
import com.hms.demo.repository.AuthorityRepository;
import com.hms.demo.service.AuthorityService;
import com.hms.demo.service.utils.MappingHelper;
import com.hms.demo.web.dto.request.AuthorityReq;
import com.hms.demo.web.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {
    private final AuthorityRepository authorityRepository;
    private final MappingHelper mappingHelper;
    // Tạo một đối tượng quyền mới dựa trên thông tin từ AuthorityReq
    @Override
    public void createAuthority(AuthorityReq authorityReq) {
        authorityRepository.save(mappingHelper.map(authorityReq, Authority.class));
    }
    // Lấy danh sách tất cả các quyền
    @Override
    public List<Authority> getAuthorities() {
        return authorityRepository.findAll();
    }
    // Cập nhật thông tin một quyền dựa trên authorityId và thông tin từ AuthorityReq
    @Override
    public void updateAuthority(Integer authorityId, AuthorityReq authorityReq) {
        // Tìm quyền dựa trên authorityId
        var authority = authorityRepository.findById(authorityId)
                .orElseThrow(() -> new EntityNotFoundException(Authority.class.getName(), authorityId.toString()));
        // Cập nhật thông tin quyền từ authorityReq
        mappingHelper.copyProperties(authorityReq, authority);
        authorityRepository.save(authority);
    }

    //Xóa một quyền dựa trên authorityId
    @Override
    public void deleteAuthority(Integer authorityId) {
        authorityRepository.deleteById(authorityId);
    }
}
