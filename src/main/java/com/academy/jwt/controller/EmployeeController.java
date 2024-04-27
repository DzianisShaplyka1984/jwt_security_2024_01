package com.academy.jwt.controller;

import com.academy.jwt.dto.EmployeeDto;
import com.academy.jwt.service.EmployeeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmployeeController {
  private final EmployeeService employeeService;

  @GetMapping(value = "/employees")
  public List<EmployeeDto> getAll() {
    return employeeService.findAll();
  }
}
