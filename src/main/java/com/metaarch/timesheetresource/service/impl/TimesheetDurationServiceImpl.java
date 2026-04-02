package com.metaarch.timesheetresource.service.impl;

import com.metaarch.timesheetresource.dto.TimeSheetDayDto;
import com.metaarch.timesheetresource.dto.TimeSheetEntryDto;
import com.metaarch.timesheetresource.dto.TimesheetDurationDto;
import com.metaarch.timesheetresource.entity.TimeSheetDay;
import com.metaarch.timesheetresource.entity.TimeSheetEntry;
import com.metaarch.timesheetresource.entity.TimesheetDuration;
import com.metaarch.timesheetresource.exception.ResourceNotFoundException;
import com.metaarch.timesheetresource.repository.TimesheetDurationRepository;
import com.metaarch.timesheetresource.service.TimesheetDurationService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TimesheetDurationServiceImpl implements TimesheetDurationService {

  private final TimesheetDurationRepository durationRepository;

  public TimesheetDurationServiceImpl(TimesheetDurationRepository durationRepository) {
    this.durationRepository = durationRepository;
  }

  @Override
  public TimesheetDurationDto upsertDuration(TimesheetDurationDto dto) {
    TimesheetDuration duration;
    if (dto.getId() == null) {
      duration = new TimesheetDuration();
      duration.setUserId(requireCurrentUserId());
    } else {
      duration = durationRepository.findById(dto.getId())
        .orElseThrow(() -> new ResourceNotFoundException("Timesheet duration not found"));
      if (!duration.getUserId().equals(requireCurrentUserId())) {
        throw new ResourceNotFoundException("Timesheet duration not found");
      }
    }

    duration.setTotalHours(dto.getTotalHours());
    duration.setBillableHours(dto.getBillableHours());
    duration.setNonBillableHours(dto.getNonBillableHours());

    if (dto.getDays() != null) {
      updateDays(duration, dto.getDays());
    }

    TimesheetDuration saved = durationRepository.save(duration);
    return toDto(saved);
  }

  @Override
  public TimesheetDurationDto getDuration(Long durationId) {
    TimesheetDuration duration = durationRepository.findById(durationId)
      .orElseThrow(() -> new ResourceNotFoundException("Timesheet duration not found"));
    if (!duration.getUserId().equals(requireCurrentUserId())) {
      throw new ResourceNotFoundException("Timesheet duration not found");
    }
    return toDto(duration);
  }

  @Override
  public List<TimesheetDurationDto> getDurationsForCurrentUser() {
    String userId = requireCurrentUserId();
    return durationRepository.findByUserId(userId).stream()
      .map(this::toDto)
      .collect(Collectors.toList());
  }

  private void updateDays(TimesheetDuration duration, List<TimeSheetDayDto> dayDtos) {
    Map<Long, TimeSheetDay> existingDays = duration.getDays().stream()
      .filter(day -> day.getId() != null)
      .collect(Collectors.toMap(TimeSheetDay::getId, day -> day));

    List<TimeSheetDay> updatedDays = new ArrayList<>();

    for (TimeSheetDayDto dayDto : dayDtos) {
      TimeSheetDay day;
      if (dayDto.getId() != null) {
        day = existingDays.get(dayDto.getId());
        if (day == null) {
          throw new ResourceNotFoundException("Timesheet day not found: " + dayDto.getId());
        }
        applyDayFields(day, dayDto);
        if (dayDto.getEntries() != null) {
          updateEntries(day, dayDto.getEntries());
        }
      } else {
        day = toEntity(dayDto, duration);
      }
      day.setDuration(duration);
      updatedDays.add(day);
    }

    duration.getDays().clear();
    duration.getDays().addAll(updatedDays);
  }

  private void updateEntries(TimeSheetDay day, List<TimeSheetEntryDto> entryDtos) {
    Map<Long, TimeSheetEntry> existingEntries = day.getEntries().stream()
      .filter(entry -> entry.getId() != null)
      .collect(Collectors.toMap(TimeSheetEntry::getId, entry -> entry));

    List<TimeSheetEntry> updatedEntries = new ArrayList<>();

    for (TimeSheetEntryDto entryDto : entryDtos) {
      TimeSheetEntry entry;
      if (entryDto.getId() != null) {
        entry = existingEntries.get(entryDto.getId());
        if (entry == null) {
          throw new ResourceNotFoundException("Timesheet entry not found: " + entryDto.getId());
        }
        applyEntryFields(entry, entryDto);
      } else {
        entry = toEntity(entryDto, day);
      }
      entry.setDay(day);
      updatedEntries.add(entry);
    }

    day.getEntries().clear();
    day.getEntries().addAll(updatedEntries);
  }

  private TimeSheetDay toEntity(TimeSheetDayDto dto, TimesheetDuration duration) {
    TimeSheetDay day = new TimeSheetDay();
    applyDayFields(day, dto);
    day.setDuration(duration);

    if (dto.getEntries() != null) {
      List<TimeSheetEntry> entries = dto.getEntries().stream()
        .map(entryDto -> toEntity(entryDto, day))
        .collect(Collectors.toList());
      day.getEntries().addAll(entries);
    }

    return day;
  }

  private TimeSheetEntry toEntity(TimeSheetEntryDto dto, TimeSheetDay day) {
    TimeSheetEntry entry = new TimeSheetEntry();
    applyEntryFields(entry, dto);
    entry.setDay(day);
    return entry;
  }

  private void applyDayFields(TimeSheetDay day, TimeSheetDayDto dto) {
    day.setTotalHoursPerDay(dto.getTotalHoursPerDay());
    day.setDate(dto.getDate());
  }

  private void applyEntryFields(TimeSheetEntry entry, TimeSheetEntryDto dto) {
    entry.setProject(dto.getProject());
    entry.setWorkType(dto.getWorkType());
    entry.setWorkMode(dto.getWorkMode());
    entry.setDuration(dto.getDuration());
    entry.setBillable(dto.isBillable());
    entry.setTaskNotes(dto.getTaskNotes());
  }

  private TimesheetDurationDto toDto(TimesheetDuration duration) {
    TimesheetDurationDto dto = new TimesheetDurationDto();
    dto.setId(duration.getId());
    dto.setUserId(duration.getUserId());
    dto.setTotalHours(duration.getTotalHours());
    dto.setBillableHours(duration.getBillableHours());
    dto.setNonBillableHours(duration.getNonBillableHours());

    List<TimeSheetDayDto> dayDtos = duration.getDays().stream()
      .map(this::toDto)
      .collect(Collectors.toList());
    dto.setDays(dayDtos);

    return dto;
  }

  private TimeSheetDayDto toDto(TimeSheetDay day) {
    TimeSheetDayDto dto = new TimeSheetDayDto();
    dto.setId(day.getId());
    dto.setTotalHoursPerDay(day.getTotalHoursPerDay());
    dto.setDate(day.getDate());

    List<TimeSheetEntryDto> entryDtos = day.getEntries().stream()
      .map(this::toDto)
      .collect(Collectors.toList());
    dto.setEntries(entryDtos);

    return dto;
  }

  private TimeSheetEntryDto toDto(TimeSheetEntry entry) {
    TimeSheetEntryDto dto = new TimeSheetEntryDto();
    dto.setId(entry.getId());
    dto.setProject(entry.getProject());
    dto.setWorkType(entry.getWorkType());
    dto.setWorkMode(entry.getWorkMode());
    dto.setDuration(entry.getDuration());
    dto.setBillable(entry.isBillable());
    dto.setTaskNotes(entry.getTaskNotes());
    return dto;
  }

  private String requireCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
      throw new IllegalArgumentException("Authenticated user is required");
    }
    return authentication.getName();
  }
}
