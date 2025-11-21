package fi.haagahelia.working_hours_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import fi.haagahelia.working_hours_management.domain.WorkHour;
import fi.haagahelia.working_hours_management.domain.WorkHourRepository;

@Service
public class WorkHoursService {
    private final WorkHourRepository workHourRepository;

    public WorkHoursService(WorkHourRepository workHourRepository){
        this.workHourRepository = workHourRepository;
    }

    public List<WorkHour> getWorkHourForEmployee(Long employeeId) {
        return workHourRepository.findByEmployeeId(employeeId);
    }

    public List<WorkHour> getAllWorkHours() {
        return (List<WorkHour>) workHourRepository.findAll();
    }


}
