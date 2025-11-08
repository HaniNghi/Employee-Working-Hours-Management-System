package fi.haagahelia.working_hours_management.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import fi.haagahelia.working_hours_management.domain.EmployeeRepository;
import fi.haagahelia.working_hours_management.domain.ManagerRepository;
import fi.haagahelia.working_hours_management.domain.WorkHour;
import fi.haagahelia.working_hours_management.domain.WorkHourRepository;
import fi.haagahelia.working_hours_management.service.WorkHoursService;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WorkHoursController {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private WorkHourRepository workHourRepository;
    @Autowired
    private WorkHoursService workHoursService;

    public WorkHoursController(EmployeeRepository employeeRepository, ManagerRepository managerRepository,
            WorkHourRepository workHourRepository, WorkHoursService workHoursService) {
        this.employeeRepository = employeeRepository;
        this.managerRepository = managerRepository;
        this.workHourRepository = workHourRepository;
    }

    @RequestMapping("/worklist")
    public String employeeList(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        return "worklist";
    }

    @GetMapping("/workhour")
    @ResponseBody
    public List<WorkHour> getAllWorkHours() {
        return workHoursService.getAllWorkHours();
    }
}
