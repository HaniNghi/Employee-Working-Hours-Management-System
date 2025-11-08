package fi.haagahelia.working_hours_management.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fi.haagahelia.working_hours_management.domain.Employee;
import fi.haagahelia.working_hours_management.domain.EmployeeRepository;
import fi.haagahelia.working_hours_management.domain.ManagerRepository;
import fi.haagahelia.working_hours_management.domain.WorkHour;
import fi.haagahelia.working_hours_management.domain.WorkHourRepository;
import fi.haagahelia.working_hours_management.service.WorkHoursService;

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

    @RequestMapping("/workhour")
    public String workhour(Model model) {
        List<WorkHour> workHours = workHoursService.getAllWorkHours();

    Map<Employee, Map<String, WorkHour>> workMap = new LinkedHashMap<>();

        // Group all WorkHour entries by each employee
        // Convert List<WorkHour> into Map<String, WorkHour>
        // key = day of the week as a string ("MONDAY", "TUESDAY", ...)
        // value = the corresponding WorkHour object
        workHours.stream()
        .collect(Collectors.groupingBy(WorkHour::getEmployee))
        .forEach((employee, whList) -> {
            Map<String, WorkHour> dayMap = whList.stream()
                    .collect(Collectors.toMap(
                            w -> w.getDate().getDayOfWeek().name(), // key = day name
                            w -> w
                    ));
            workMap.put(employee, dayMap);
        });

        model.addAttribute("workMap", workMap);
        return "workhour";
    }

    @RequestMapping(value = { "/addemployee" })
    public String addEmployee(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("managers", managerRepository.findAll());
        model.addAttribute("workhour", new WorkHour());
        return "addemployee";
    }

    @RequestMapping(value = { "/save" }, method = RequestMethod.POST)
    public String save(Employee employee) {
        employeeRepository.save(employee);
        return "redirect:workhour";
    }

}
