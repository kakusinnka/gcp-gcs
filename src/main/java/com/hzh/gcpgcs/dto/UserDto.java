package com.hzh.gcpgcs.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class UserDto {
    String name;
    String id;
    StudentDto studentDto;
}
