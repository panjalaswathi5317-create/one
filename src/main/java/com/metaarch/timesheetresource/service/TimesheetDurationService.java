package com.metaarch.timesheetresource.service;

import com.metaarch.timesheetresource.dto.TimesheetDurationDto;
import java.util.List;

public interface TimesheetDurationService {
  TimesheetDurationDto upsertDuration(TimesheetDurationDto dto);

  TimesheetDurationDto getDuration(Long durationId);

  List<TimesheetDurationDto> getDurationsForCurrentUser();
}
