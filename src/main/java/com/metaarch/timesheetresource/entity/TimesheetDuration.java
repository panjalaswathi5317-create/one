package com.metaarch.timesheetresource.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "timesheet_duration")
public class TimesheetDuration {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private BigDecimal totalHours;

  private BigDecimal billableHours;

  private BigDecimal nonBillableHours;

  @Column(nullable = false)
  private String userId;

  @OneToMany(mappedBy = "duration", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TimeSheetDay> days = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public List<TimeSheetDay> getDays() {
    return days;
  }

  public void setDays(List<TimeSheetDay> days) {
    this.days = days;
  }
}
