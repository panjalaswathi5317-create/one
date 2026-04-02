package com.metaarch.timesheetresource.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TimesheetDurationDto {

  private Long id;
  private String userId;
  private BigDecimal totalHours;
  private BigDecimal billableHours;
  private BigDecimal nonBillableHours;
  private List<TimeSheetDayDto> days = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public BigDecimal getTotalHours() {
    return totalHours;
  }

  public void setTotalHours(BigDecimal totalHours) {
    this.totalHours = totalHours;
  }

  public BigDecimal getBillableHours() {
    return billableHours;
  }

  public void setBillableHours(BigDecimal billableHours) {
    this.billableHours = billableHours;
  }

  public BigDecimal getNonBillableHours() {
    return nonBillableHours;
  }

  public void setNonBillableHours(BigDecimal nonBillableHours) {
    this.nonBillableHours = nonBillableHours;
  }

  public List<TimeSheetDayDto> getDays() {
    return days;
  }

  public void setDays(List<TimeSheetDayDto> days) {
    this.days = days;
  }
}
