package com.littlepig.service.impl;

import com.littlepig.common.enums.UserStatus;
import com.littlepig.controller.request.UserCreationRequest;
import com.littlepig.controller.request.UserPasswordRequest;
import com.littlepig.controller.request.UserUpdateRequest;
import com.littlepig.controller.response.UserResponse;
import com.littlepig.exception.ResourceNotFoundException;
import com.littlepig.model.AddressEntity;
import com.littlepig.model.UserEntity;
import com.littlepig.repository.AddressRepository;
import com.littlepig.repository.UserRepository;
import com.littlepig.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j(topic = "USER-SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

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
        log.info("Changing password for user: {}", request);
        // find user
        UserEntity user = getUserEntityById(request.getId());
        if(request.getPassword().equals(request.getConfirmPassword())){
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        userRepository.save(user);
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
        log.info("Deleting user: {}", id);
        UserEntity userEntity = getUserEntityById(id);

        userEntity.setStatus(UserStatus.INACTIVE);

        userRepository.save(userEntity);
        log.info("Deleted user: {}", userEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserUpdateRequest request) {
        log.info("Updating user: {}", request);
        //Get user by id
       UserEntity userEntity =  getUserEntityById(request.getId());

        // set data
        userEntity.setFirstName(request.getFirstName());
        userEntity.setLastName(request.getLastName());
        userEntity.setUserName(request.getUserName());
        userEntity.setGender(request.getGender());
        userEntity.setBirthday(request.getBirthday());
        userEntity.setEmail(request.getEmail());
        userEntity.setPhone(request.getPhone());

        userRepository.save(userEntity);

        log.info("updated user: {}", request);

        // save address
        List<AddressEntity> addresses = new ArrayList<>();
        request.getAddresses().forEach( addressRequest -> {
            AddressEntity addressEntity = addressRepository.findByUserIdAndAddressType(userEntity.getId(),
                    addressRequest.getAddressType());
            if(addressEntity == null){
                //insert new address
                addressEntity = new AddressEntity();
            }

            //update existing address entity
            addressEntity.setApartmentNumber(addressRequest.getApartmentNumber());
            addressEntity.setFloor(addressRequest.getFloor());
            addressEntity.setBuilding(addressRequest.getBuilding());
            addressEntity.setStreetNumber(addressRequest.getStreetNumber());
            addressEntity.setStreet(addressRequest.getStreet());
            addressEntity.setCity(addressRequest.getCity());
            addressEntity.setCountry(addressRequest.getCountry());
            addressEntity.setAddressType(addressRequest.getAddressType());
            addressEntity.setUserId(userEntity.getId());
            addresses.add(addressEntity);
        });
        //save address list
        addressRepository.saveAll(addresses);
    }

    private UserEntity getUserEntityById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
