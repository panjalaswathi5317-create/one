package com.metaarch.timesheetresource.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "timesheet_entry")
public class TimeSheetEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private ProjectType project;

  @Enumerated(EnumType.STRING)
  private WorkType workType;

  @Enumerated(EnumType.STRING)
  private WorkMode workMode;

  private String duration;

  private boolean billable;

  private String taskNotes;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "day_id", nullable = false)
  private TimeSheetDay day;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ProjectType getProject() {
    return project;
  }

  public void setProject(ProjectType project) {
    this.project = project;
  }

  public WorkType getWorkType() {
    return workType;
  }

  public void setWorkType(WorkType workType) {
    this.workType = workType;
  }

  public WorkMode getWorkMode() {
    return workMode;
  }

  public void setWorkMode(WorkMode workMode) {
    this.workMode = workMode;
  }

  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  public boolean isBillable() {
    return billable;
  }

  public void setBillable(boolean billable) {
    this.billable = billable;
  }

  public String getTaskNotes() {
    return taskNotes;
  }

  public void setTaskNotes(String taskNotes) {
    this.taskNotes = taskNotes;
  }

  public TimeSheetDay getDay() {
    return day;
  }

  public void setDay(TimeSheetDay day) {
    this.day = day;
  }
}
