package com.hms.demo.repository;

import com.hms.demo.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
    // Tìm một quyền dựa vào tên quyền
    Optional<Authority> findByName(String name);
}
