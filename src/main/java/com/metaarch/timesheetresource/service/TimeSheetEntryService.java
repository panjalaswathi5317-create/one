package com.metaarch.timesheetresource.service;

import com.metaarch.timesheetresource.dto.TimeSheetEntryDto;
import java.util.List;

public interface TimeSheetEntryService {
  TimeSheetEntryDto upsertEntry(Long dayId, TimeSheetEntryDto dto);

  TimeSheetEntryDto getEntry(Long dayId, Long entryId);

  List<TimeSheetEntryDto> getEntries(Long dayId);
}
