package fi.haagahelia.working_hours_management.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface WorkHourRepository extends CrudRepository<WorkHour, Long> {
    List<WorkHour> findByEmployeeId(Long employeeId);
}
