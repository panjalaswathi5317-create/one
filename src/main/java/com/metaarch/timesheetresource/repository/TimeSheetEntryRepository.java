package com.metaarch.timesheetresource.repository;

import com.metaarch.timesheetresource.entity.TimeSheetEntry;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSheetEntryRepository extends JpaRepository<TimeSheetEntry, Long> {
  List<TimeSheetEntry> findByDayId(Long dayId);
}
