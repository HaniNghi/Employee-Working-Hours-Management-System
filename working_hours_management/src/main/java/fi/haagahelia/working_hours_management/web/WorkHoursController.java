package fi.haagahelia.working_hours_management.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import fi.haagahelia.working_hours_management.domain.EmployeeRepository;
import fi.haagahelia.working_hours_management.domain.ManagerRepository;

@Controller
public class WorkHoursController {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ManagerRepository managerRepository;

    public WorkHoursController(EmployeeRepository employeeRepository, ManagerRepository managerRepository) {
        this.employeeRepository = employeeRepository;
        this.managerRepository = managerRepository;
    }

    @RequestMapping("/worklist")
    public String employeeList(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        return "worklist";
    }

}
