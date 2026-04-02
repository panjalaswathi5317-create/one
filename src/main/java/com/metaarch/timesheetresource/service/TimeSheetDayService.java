package com.metaarch.timesheetresource.service;

import java.util.List;

import com.metaarch.timesheetresource.dto.TimeSheetDayDto;

public interface TimeSheetDayService {
  TimeSheetDayDto upsertDay(Long durationId, TimeSheetDayDto dto);

  TimeSheetDayDto getDay(Long durationId, Long dayId);

  List<TimeSheetDayDto> getDays(Long durationId);
}
