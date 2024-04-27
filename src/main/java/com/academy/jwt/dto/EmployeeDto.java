package com.academy.jwt.dto;

import lombok.Data;

@Data
public class EmployeeDto {
  private String name;
  private String email;
  private Integer year;
  private Integer salary;
}
