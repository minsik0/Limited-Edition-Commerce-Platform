package com.sparta.userservice.user.repository;

import com.sparta.userservice.user.domain.User;
import com.sparta.userservice.user.domain.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID userId);

    Page<User> findAllByStatus(UserStatus status, Pageable pageable);
}
