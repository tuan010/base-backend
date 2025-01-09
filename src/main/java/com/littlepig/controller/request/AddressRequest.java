package com.littlepig.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Builder
@Setter
public class AddressRequest implements Serializable {
//    CREATE TABLE public.tbl_address (
//            id bigserial NOT NULL,
//            apartment_number varchar(255) NULL,
//    floor varchar(255) NULL,
//    building varchar(255) NULL,
//    street_number varchar(255) NULL,
//    street varchar(255) NULL,
//    city varchar(255) NULL,
//    country varchar(255) NULL,
//    address_type int4 NULL,
//    user_id int8 NULL,
//    created_at timestamp(6) NULL DEFAULT now(),
//    updated_at timestamp(6) NULL DEFAULT now(),
//    CONSTRAINT tbl_address_pkey PRIMARY KEY (id)
//);

    private String apartmentNumber;
    private String floor;
    private String building;
    private String streetNumber;
    private String street;
    private String city;
    private String country;
    private Integer addressType;
}
