package com.metaarch.timesheetresource.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metaarch.timesheetresource.dto.TimeSheetEntryDto;
import com.metaarch.timesheetresource.entity.TimeSheetDay;
import com.metaarch.timesheetresource.entity.TimeSheetEntry;
import com.metaarch.timesheetresource.exception.ResourceNotFoundException;
import com.metaarch.timesheetresource.repository.TimeSheetDayRepository;
import com.metaarch.timesheetresource.repository.TimeSheetEntryRepository;
import com.metaarch.timesheetresource.service.TimeSheetEntryService;

@Service
@Transactional
public class TimeSheetEntryServiceImpl implements TimeSheetEntryService {

  private final TimeSheetEntryRepository entryRepository;
  private final TimeSheetDayRepository dayRepository;

  public TimeSheetEntryServiceImpl(TimeSheetEntryRepository entryRepository,
                                   TimeSheetDayRepository dayRepository) {
    this.entryRepository = entryRepository;
    this.dayRepository = dayRepository;
  }

  @Override
  public TimeSheetEntryDto upsertEntry(Long dayId, TimeSheetEntryDto dto) {
    TimeSheetDay day = dayRepository.findById(dayId)
      .orElseThrow(() -> new ResourceNotFoundException("Timesheet day not found"));

    ensureOwner(day);

    TimeSheetEntry entry;
    if (dto.getId() == null) {
      entry = new TimeSheetEntry();
      entry.setDay(day);
    } else {
      entry = entryRepository.findById(dto.getId())
        .orElseThrow(() -> new ResourceNotFoundException("Timesheet entry not found"));
      if (!entry.getDay().getId().equals(day.getId())) {
        throw new ResourceNotFoundException("Timesheet entry does not belong to day");
      }
    }

    applyEntryFields(entry, dto);

    TimeSheetEntry saved = entryRepository.save(entry);
    return toDto(saved);
  }

  @Override
  public TimeSheetEntryDto getEntry(Long dayId, Long entryId) {
    TimeSheetDay day = dayRepository.findById(dayId)
      .orElseThrow(() -> new ResourceNotFoundException("Timesheet day not found"));
    ensureOwner(day);

    TimeSheetEntry entry = entryRepository.findById(entryId)
      .orElseThrow(() -> new ResourceNotFoundException("Timesheet entry not found"));

    if (!entry.getDay().getId().equals(day.getId())) {
      throw new ResourceNotFoundException("Timesheet entry not found");
    }

    return toDto(entry);
  }

  @Override
  public List<TimeSheetEntryDto> getEntries(Long dayId) {
    TimeSheetDay day = dayRepository.findById(dayId)
      .orElseThrow(() -> new ResourceNotFoundException("Timesheet day not found"));
    ensureOwner(day);

    return entryRepository.findByDayId(dayId).stream()
      .map(this::toDto)
      .collect(Collectors.toList());
  }

  private void applyEntryFields(TimeSheetEntry entry, TimeSheetEntryDto dto) {
    entry.setProject(dto.getProject());
    entry.setWorkType(dto.getWorkType());
    entry.setWorkMode(dto.getWorkMode());
    entry.setDuration(dto.getDuration());
    entry.setBillable(dto.isBillable());
    entry.setTaskNotes(dto.getTaskNotes());
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

  private void ensureOwner(TimeSheetDay day) {
    String currentUserId = requireCurrentUserId();
    if (!day.getDuration().getUserId().equals(currentUserId)) {
      throw new ResourceNotFoundException("Timesheet day not found");
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
