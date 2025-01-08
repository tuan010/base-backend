package com.littlepig.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SimpleTest {
    private Long id;
    private String name;
}