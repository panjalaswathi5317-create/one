package com.metaarch.timesheetresource.repository;

import com.metaarch.timesheetresource.entity.TimeSheetDay;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSheetDayRepository extends JpaRepository<TimeSheetDay, Long> {
  List<TimeSheetDay> findByDurationId(Long durationId);
}
