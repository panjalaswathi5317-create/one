package com.metaarch.timesheetresource.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TimeSheetDayDto {

  private Long id;
  private BigDecimal totalHoursPerDay;
  private LocalDate date;
  private List<TimeSheetEntryDto> entries = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public BigDecimal getTotalHoursPerDay() {
    return totalHoursPerDay;
  }

  public void setTotalHoursPerDay(BigDecimal totalHoursPerDay) {
    this.totalHoursPerDay = totalHoursPerDay;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public List<TimeSheetEntryDto> getEntries() {
    return entries;
  }

  public void setEntries(List<TimeSheetEntryDto> entries) {
    this.entries = entries;
  }
}
