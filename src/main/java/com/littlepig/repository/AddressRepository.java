package com.littlepig.repository;

import com.littlepig.model.AddressEntity;
import com.littlepig.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    AddressEntity findByUserIdAndAddressType(Long userId, Integer AddressType);

}
