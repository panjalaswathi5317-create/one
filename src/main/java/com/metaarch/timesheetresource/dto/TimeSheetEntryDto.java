package com.metaarch.timesheetresource.dto;

import com.metaarch.timesheetresource.entity.ProjectType;
import com.metaarch.timesheetresource.entity.WorkMode;
import com.metaarch.timesheetresource.entity.WorkType;

public class TimeSheetEntryDto {

  private Long id;
  private ProjectType project;
  private WorkType workType;
  private WorkMode workMode;
  private String duration;
  private boolean billable;
  private String taskNotes;

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
}
