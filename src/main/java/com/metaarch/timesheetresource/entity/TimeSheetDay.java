package com.metaarch.timesheetresource.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "timesheet_day")
public class TimeSheetDay {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private BigDecimal totalHoursPerDay;

  private LocalDate date;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "duration_id", nullable = false)
  private TimesheetDuration duration;

  @OneToMany(mappedBy = "day", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TimeSheetEntry> entries = new ArrayList<>();

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

  public TimesheetDuration getDuration() {
    return duration;
  }

  public void setDuration(TimesheetDuration duration) {
    this.duration = duration;
  }

  public List<TimeSheetEntry> getEntries() {
    return entries;
  }

  public void setEntries(List<TimeSheetEntry> entries) {
    this.entries = entries;
  }
}
