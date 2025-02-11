package com.littlepig.repository;

import com.littlepig.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(value = "SELECT u FROM UserEntity u WHERE u.status='ACTIVE' and u.type ='USER'" +
            "and (lower(u.firstName)  like  lower(:keyword)  or lower(u.lastName)  like lower(:keyword) or lower(u.phone) like lower(:keyword) " +
            " or lower(u.username) like lower(:keyword) or lower(u.email) like lower(:keyword))")
    Page<UserEntity> findByKeyword (String keyword, Pageable pageable);

    UserEntity findByEmail(String email);

    UserEntity findByUsername(String userName);
}
