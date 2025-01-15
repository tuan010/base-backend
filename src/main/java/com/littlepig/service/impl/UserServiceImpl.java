package com.littlepig.service.impl;

import com.littlepig.common.enums.UserStatus;
import com.littlepig.controller.request.UserCreationRequest;
import com.littlepig.controller.request.UserPasswordRequest;
import com.littlepig.controller.request.UserUpdateRequest;
import com.littlepig.controller.response.UserPageResponse;
import com.littlepig.controller.response.UserResponse;
import com.littlepig.exception.InvalidDataException;
import com.littlepig.exception.ResourceNotFoundException;
import com.littlepig.model.AddressEntity;
import com.littlepig.model.UserEntity;
import com.littlepig.repository.AddressRepository;
import com.littlepig.repository.UserRepository;
import com.littlepig.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j(topic = "USER-SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserPageResponse findAll(String keyword, String sort, int page, int size) {
        log.info("findAll start with: keyword={}, sort={}, page={}, size={}", keyword, sort, page, size);
        // Sorting
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "id");
        if (StringUtils.hasLength(sort)) {
            // goi search method
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");// tencot:asc|desc
            Matcher matcher = pattern.matcher(sort);
            if (matcher.find()) {
                String column = matcher.group(1);
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    order = new Sort.Order(Sort.Direction.ASC, column);
                } else {
                    order = new Sort.Order(Sort.Direction.DESC, column);
                }
            }
        }

        // Xu ly truong hop FE muon bat dau voi page  = 1
        int pageNo = 0;
        if (page > 0) {
            pageNo = page - 1;
        }

        // Paging
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(order));
        Page<UserEntity> entityPage;
        if (StringUtils.hasLength(keyword)) {
            keyword = "%" + keyword.toLowerCase() + "%";
            // goi search method
            entityPage = userRepository.findByKeyword(keyword, pageable);

        } else {
            entityPage = userRepository.findAll(pageable);
        }
        log.info("findAll end");
        return getUserPageResponse(page, size, entityPage);
    }

    private static UserPageResponse getUserPageResponse(int page, int size, Page<UserEntity> entityPage) {
        log.info("Convert User Entity Page");
        //page no, page size, list data
        List<UserResponse> responseList = entityPage.stream().map(
                entity -> UserResponse.builder()
                        .id(entity.getId())
                        .phone(entity.getPhone())
                        .firstName(entity.getFirstName())
                        .lastName(entity.getLastName())
                        .gender(entity.getGender())
                        .birthday(entity.getBirthday())
                        .userName(entity.getUserName())
                        .phone(entity.getPhone())
                        .email(entity.getEmail())
                        .build()
        ).toList();

        UserPageResponse response = new UserPageResponse();
        response.setPageNo(page);
        response.setPageSize(size);
        response.setTotalElements(entityPage.getTotalElements());
        response.setTotalPages(entityPage.getTotalPages());
        response.setUsers(responseList);
        return response;
    }

    @Override
    public UserResponse findById(Long id) {
        log.info("Get user by id: {}", id);
        UserEntity entity = getUserEntityById(id);
        return UserResponse.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .gender(entity.getGender())
                .birthday(entity.getBirthday())
                .phone(entity.getPhone())
                .build();
    }

    @Override
    public UserResponse findByUsername(String userName) {
        return null;
    }

    @Override
    public UserResponse findByEmail(String email) {
        return null;
    }

    @Override
    public void changePassword(UserPasswordRequest request) {
        log.info("Changing password for user: {}", request);
        // find user
        UserEntity user = getUserEntityById(request.getId());
        if (request.getPassword().equals(request.getConfirmPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        userRepository.save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(UserCreationRequest request) {
        log.info("Saving user: {}", request);

        UserEntity existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser != null){
            throw new InvalidDataException("Email already exist. Please try again!");
        }

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
        UserEntity userEntity = getUserEntityById(request.getId());

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
        request.getAddresses().forEach(addressRequest -> {
            AddressEntity addressEntity = addressRepository.findByUserIdAndAddressType(userEntity.getId(),
                    addressRequest.getAddressType());
            if (addressEntity == null) {
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

    private UserEntity getUserEntityById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
