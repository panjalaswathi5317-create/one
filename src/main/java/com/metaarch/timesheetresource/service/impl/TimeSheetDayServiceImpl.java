package com.metaarch.timesheetresource.service.impl;

import com.metaarch.timesheetresource.dto.TimeSheetDayDto;
import com.metaarch.timesheetresource.dto.TimeSheetEntryDto;
import com.metaarch.timesheetresource.entity.TimeSheetDay;
import com.metaarch.timesheetresource.entity.TimeSheetEntry;
import com.metaarch.timesheetresource.entity.TimesheetDuration;
import com.metaarch.timesheetresource.exception.ResourceNotFoundException;
import com.metaarch.timesheetresource.repository.TimeSheetDayRepository;
import com.metaarch.timesheetresource.repository.TimesheetDurationRepository;
import com.metaarch.timesheetresource.service.TimeSheetDayService;
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
public class TimeSheetDayServiceImpl implements TimeSheetDayService {

  private final TimeSheetDayRepository dayRepository;
  private final TimesheetDurationRepository durationRepository;

  public TimeSheetDayServiceImpl(TimeSheetDayRepository dayRepository,
                                 TimesheetDurationRepository durationRepository) {
    this.dayRepository = dayRepository;
    this.durationRepository = durationRepository;
  }

  @Override
  public TimeSheetDayDto upsertDay(Long durationId, TimeSheetDayDto dto) {
    TimesheetDuration duration = durationRepository.findById(durationId)
      .orElseThrow(() -> new ResourceNotFoundException("Timesheet duration not found"));

    ensureOwner(duration);

    TimeSheetDay day;
    if (dto.getId() == null) {
      day = new TimeSheetDay();
      day.setDuration(duration);
    } else {
      day = dayRepository.findById(dto.getId())
        .orElseThrow(() -> new ResourceNotFoundException("Timesheet day not found"));
      if (!day.getDuration().getId().equals(duration.getId())) {
        throw new ResourceNotFoundException("Timesheet day does not belong to duration");
      }
    }

    applyDayFields(day, dto);
    if (dto.getEntries() != null) {
      updateEntries(day, dto.getEntries());
    }

    TimeSheetDay saved = dayRepository.save(day);
    return toDto(saved);
  }

  @Override
  public TimeSheetDayDto getDay(Long durationId, Long dayId) {
    TimesheetDuration duration = durationRepository.findById(durationId)
      .orElseThrow(() -> new ResourceNotFoundException("Timesheet duration not found"));
    ensureOwner(duration);

    TimeSheetDay day = dayRepository.findById(dayId)
      .orElseThrow(() -> new ResourceNotFoundException("Timesheet day not found"));

    if (!day.getDuration().getId().equals(duration.getId())) {
      throw new ResourceNotFoundException("Timesheet day not found");
    }

    return toDto(day);
  }

  @Override
  public List<TimeSheetDayDto> getDays(Long durationId) {
    TimesheetDuration duration = durationRepository.findById(durationId)
      .orElseThrow(() -> new ResourceNotFoundException("Timesheet duration not found"));
    ensureOwner(duration);

    return dayRepository.findByDurationId(durationId).stream()
      .map(this::toDto)
      .collect(Collectors.toList());
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

  private void ensureOwner(TimesheetDuration duration) {
    String currentUserId = requireCurrentUserId();
    if (!duration.getUserId().equals(currentUserId)) {
      throw new ResourceNotFoundException("Timesheet duration not found");
    }
  }

  private String requireCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
      throw new IllegalArgumentException("Authenticated user is required");
    }
    return authentication.getName();
  }
}
