package com.littlepig.service.impl;

import com.littlepig.common.enums.UserStatus;
import com.littlepig.controller.request.UserCreationRequest;
import com.littlepig.controller.request.UserPasswordRequest;
import com.littlepig.controller.request.UserUpdateRequest;
import com.littlepig.controller.response.UserResponse;
import com.littlepig.model.AddressEntity;
import com.littlepig.model.UserEntity;
import com.littlepig.repository.AddressRepository;
import com.littlepig.repository.UserRepository;
import com.littlepig.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j(topic = "USER-SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Override
    public List<UserResponse> findAll() {
        return null;
    }

    @Override
    public UserResponse findById(Long id) {
        return null;
    }

    @Override
    public UserResponse findByUsername(String userName) {
        return null;
    }

    @Override
    public UserResponse findBYEmail(String email) {
        return null;
    }

    @Override
    public void changePassword(UserPasswordRequest request) {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(UserCreationRequest request) {
        log.info("Saving user: {}", request);
        UserEntity user = UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .birthday(request.getBirthday())
                .email(request.getEmail())
                .phone(request.getPhone())
                .userName(request.getUserName())
                .type(request.getType())
                .status(UserStatus.NONE)
                .password("bypassForPassword")
                .build();
        userRepository.save(user);
        log.info("User saved: {}", user);

        if (user.getId() != null) {
            log.info("user id: {}", user.getId());
//            System.out.println(10/0); test transaction
            List<AddressEntity> addresses = new ArrayList<>();
            request.getAddresses().forEach(addressRequest -> {
                AddressEntity addressEntity = new AddressEntity();
                addressEntity.setApartmentNumber(addressRequest.getApartmentNumber());
                addressEntity.setFloor(addressRequest.getFloor());
                addressEntity.setBuilding(addressRequest.getBuilding());
                addressEntity.setStreetNumber(addressRequest.getStreetNumber());
                addressEntity.setStreet(addressRequest.getStreet());
                addressEntity.setCity(addressRequest.getCity());
                addressEntity.setCountry(addressRequest.getCountry());
                addressEntity.setAddressType(addressRequest.getAddressType());
                addressEntity.setUserId(user.getId());
                addresses.add(addressEntity);
            });
            log.info("Saving addresses: {}", addresses);
            addressRepository.saveAll(addresses);
        }
        return user.getId();

    }

    @Override
    public void delete(Long id) {

    }
}
