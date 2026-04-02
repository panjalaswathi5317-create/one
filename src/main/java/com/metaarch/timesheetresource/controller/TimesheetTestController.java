package com.metaarch.timesheetresource.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/timesheet")
public class TimesheetTestController {

  @GetMapping("/test")
  public String test() {
    return "Timesheet resource secured endpoint";
  }
}
