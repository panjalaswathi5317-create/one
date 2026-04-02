package com.metaarch.timesheetresource.repository;

import com.metaarch.timesheetresource.entity.TimesheetDuration;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimesheetDurationRepository extends JpaRepository<TimesheetDuration, Long> {
  List<TimesheetDuration> findByUserId(String userId);
}
