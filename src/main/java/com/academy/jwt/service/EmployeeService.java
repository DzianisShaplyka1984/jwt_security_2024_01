package com.academy.jwt.service;

import com.academy.jwt.dto.EmployeeDto;
import java.util.List;

public interface EmployeeService {
  List<EmployeeDto> findAll();
}
