package com.hms.demo.repository;

import com.hms.demo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer>, JpaSpecificationExecutor<Account> {
    // Tìm một tài khoản dựa trên địa chỉ email
    Optional<Account> findOneByEmail(String email);
    // Tìm một tài khoản dựa trên tên đăng nhập
    Optional<Account> findOneByUsername(String username);
    // Kiểm tra xem có tài khoản nào đã tồn tại với tên đăng nhập hoặc email đã cho hay không
    Boolean existsByUsernameOrEmail(String username, String email);
}
