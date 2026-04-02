package com.metaarch.timesheetresource.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.metaarch.timesheetresource.dto.TimeSheetDayDto;
import com.metaarch.timesheetresource.dto.TimeSheetEntryDto;
import com.metaarch.timesheetresource.dto.TimesheetDurationDto;
import com.metaarch.timesheetresource.service.TimeSheetDayService;
import com.metaarch.timesheetresource.service.TimeSheetEntryService;
import com.metaarch.timesheetresource.service.TimesheetDurationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/timesheets")
public class TimesheetController {

  private final TimesheetDurationService durationService;
  private final TimeSheetDayService dayService;
  private final TimeSheetEntryService entryService;

  public TimesheetController(TimesheetDurationService durationService,
                             TimeSheetDayService dayService,
                             TimeSheetEntryService entryService) {
    this.durationService = durationService;
    this.dayService = dayService;
    this.entryService = entryService;
  }

  @PutMapping
  public TimesheetDurationDto upsertDuration(@RequestBody TimesheetDurationDto dto) {
    return durationService.upsertDuration(dto);
  }

  @GetMapping
  public List<TimesheetDurationDto> getDurations() {
    return durationService.getDurationsForCurrentUser();
  }

  @GetMapping("/{durationId}")
  public TimesheetDurationDto getDuration(@PathVariable Long durationId) {
    return durationService.getDuration(durationId);
  }

  @PutMapping("/{durationId}/days")
  public TimeSheetDayDto upsertDay(@PathVariable Long durationId,
                                   @RequestBody TimeSheetDayDto dto) {
    return dayService.upsertDay(durationId, dto);
  }

  @GetMapping("/{durationId}/days")
  public List<TimeSheetDayDto> getDays(@PathVariable Long durationId) {
    return dayService.getDays(durationId);
  }

  @Operation(
    summary = "Get user by ID",
    description = "Fetch user details using user ID"
)
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "User found"),
    @ApiResponse(responseCode = "404", description = "User not found")
})
  @GetMapping("/{durationId}/days/{dayId}")
  public TimeSheetDayDto getDay(@PathVariable Long durationId,
                                @PathVariable Long dayId) {
    return dayService.getDay(durationId, dayId);
  }

  @PutMapping("/days/{dayId}/entries")
  public TimeSheetEntryDto upsertEntry(@PathVariable Long dayId,
                                       @RequestBody TimeSheetEntryDto dto) {
    return entryService.upsertEntry(dayId, dto);
  }

  @GetMapping("/days/{dayId}/entries")
  public List<TimeSheetEntryDto> getEntries(@PathVariable Long dayId) {
    return entryService.getEntries(dayId);
  }

  @GetMapping("/days/{dayId}/entries/{entryId}")
  public TimeSheetEntryDto getEntry(@PathVariable Long dayId,
                                    @PathVariable Long entryId) {
    return entryService.getEntry(dayId, entryId);
  }
}
