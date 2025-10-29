package com.api.api.controller;

import com.api.api.dto.DashboardDTO;
import com.api.api.service.serviceInterface.DashboardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin
public class DashboardController {

  private final DashboardService dashboardService;

  public DashboardController(DashboardService dashboardService) {
    this.dashboardService = dashboardService;
  }

  @GetMapping
  public DashboardDTO getDashboardData() {
    return dashboardService.getDashboardData();
  }
}
