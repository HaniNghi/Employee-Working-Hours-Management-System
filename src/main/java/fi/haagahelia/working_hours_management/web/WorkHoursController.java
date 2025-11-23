package fi.haagahelia.working_hours_management.web;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import fi.haagahelia.working_hours_management.domain.Employee;
import fi.haagahelia.working_hours_management.domain.EmployeeRepository;
import fi.haagahelia.working_hours_management.domain.ManagerRepository;
import fi.haagahelia.working_hours_management.domain.WorkHour;
import fi.haagahelia.working_hours_management.domain.WorkHourRepository;
import fi.haagahelia.working_hours_management.service.WorkHoursService;

@Controller
public class WorkHoursController {

    private final PasswordEncoder passwordEncoder;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private WorkHourRepository workHourRepository;
    @Autowired
    private WorkHoursService workHoursService;

    private boolean isManager() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"));
    }

    private String currentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public WorkHoursController(EmployeeRepository employeeRepository, ManagerRepository managerRepository,
            WorkHourRepository workHourRepository, WorkHoursService workHoursService, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.managerRepository = managerRepository;
        this.workHourRepository = workHourRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // display all employees information
    @RequestMapping("/worklist")
    public String employeeList(Model model) {
        if (!isManager()) {
            throw new AccessDeniedException("You do not have permission to access the employee list!");
        }
        model.addAttribute("employees", employeeRepository.findAll());
        return "worklist";
    }
    // display employees work hours
    @RequestMapping(value = { "/workhour" })
    public String workhour(Model model) {
        List<WorkHour> workHours = workHoursService.getAllWorkHours();

        Map<Employee, Map<LocalDate, WorkHour>> workMap = new LinkedHashMap<>();

        if (isManager()) {
            // Manager can see all employees work hours
            workHours.stream()
                    .collect(Collectors.groupingBy(WorkHour::getEmployee,
                            () -> new TreeMap<>(Comparator.comparing(Employee::getId)),
                            Collectors.toList()))
                    .forEach((employee, whList) -> {
                        Map<LocalDate, WorkHour> dayMap = whList.stream()
                                .collect(Collectors.toMap(
                                        WorkHour::getDate,
                                        w -> w,
                                        (w1, w2) -> w1,
                                        LinkedHashMap::new));
                        workMap.put(employee, dayMap);
                    });
        } else {
            // Employee can only see his/her work hours
            Employee emp = employeeRepository.findByUsername(currentUsername()).orElseThrow();
            List<WorkHour> empHours = workHours.stream()
                    .filter(wh -> wh.getEmployee().getId().equals(emp.getId()))
                    .toList();

            Map<LocalDate, WorkHour> dayMap = empHours.stream()
                    .collect(Collectors.toMap(
                            WorkHour::getDate,
                            w -> w,
                            (w1, w2) -> w1,
                            LinkedHashMap::new));
            workMap.put(emp, dayMap);
        }

        List<LocalDate> allDates = workHours.stream()
                .map(WorkHour::getDate)
                .distinct()
                .sorted()
                .toList();

        model.addAttribute("workMap", workMap);
        model.addAttribute("dates", allDates);
        return "workhour";
    }
    // add a new employee to company
    @RequestMapping(value = { "/addemployee" })
    public String addEmployee(Model model) {
        if (!isManager()) {
            throw new AccessDeniedException("You do not have permission to add employees!");
        }
        model.addAttribute("employee", new Employee());
        model.addAttribute("managers", managerRepository.findAll());
        model.addAttribute("workhour", new WorkHour());
        return "addemployee";
    }
    // save new employee information
    @RequestMapping(value = { "/save" }, method = RequestMethod.POST)
    public String save(Employee employee, @RequestParam("date") String date,
            @RequestParam("checkIn") String checkIn,
            @RequestParam("checkOut") String checkOut) {
        if (!isManager()) {
            throw new AccessDeniedException("You do not have permission to add employees!");
        }
        // Encode password before saving
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        Employee savedEmployee = employeeRepository.save(employee);
        WorkHour workHour = new WorkHour();
        workHour.setEmployee(savedEmployee);
        workHour.setDate(LocalDate.parse(date));
        workHour.setCheckIn(LocalTime.parse(checkIn));
        workHour.setCheckOut(LocalTime.parse(checkOut));

        workHourRepository.save(workHour);
        return "redirect:workhour";
    }
    // delete employee that dont need anymore
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteEmployee(@PathVariable("id") Long id, Model model) {
        employeeRepository.deleteById(id);
        return "redirect:../worklist";
    }
    // edit employee's information
    @RequestMapping(value = { "/edit/{id}" }, method = RequestMethod.GET)
    public String editEmployee(@PathVariable("id") Long employeeId, Model model) {
        // Employee employee = employeeRepository.findById(employeeId).orElse(new
        // Employee());
        // System.out.println("employee data " + employee.getId() + " name: " +
        // employee.getFirstName());
        model.addAttribute("employee", employeeRepository.findById(employeeId));
        model.addAttribute("managers", managerRepository.findAll());
        return "edit";
    }
    // save employee information that editted
    @RequestMapping(value = { "/update" }, method = RequestMethod.POST)
    public String updateEmployee(Employee employee) {
        employeeRepository.save(employee);
        return "redirect:worklist";
    }
    // edit time schedule (work hours)
    @RequestMapping(value = "/editworkhour/{id}", method = RequestMethod.GET)
    public String editWorkHour(@PathVariable("id") Long id, Model model) {
        WorkHour workHour = workHourRepository.findById(id).orElseThrow();
        // employee can only change his/her time schedule
        if (!isManager() && !workHour.getEmployee().getUsername().equals(currentUsername())) {
            throw new AccessDeniedException("You do not have permission to edit this calendar!");
        }
        model.addAttribute("workhour", workHour);
        // manager can edit time of all employees
        if (isManager()) {
            model.addAttribute("employees", employeeRepository.findAll());
        }
        return "editworkhour";
    }

    // save work hours just editted
    @RequestMapping(value = "/updateworkhour", method = RequestMethod.POST)
    public String updateWorkHour(@ModelAttribute WorkHour workHour,
            @RequestParam(value = "employee", required = false) Long employeeId) {
        if (!isManager()) {
            Employee emp = employeeRepository.findByUsername(currentUsername()).orElseThrow();
            workHour.setEmployee(emp);
        } else {
            workHour.setEmployee(employeeRepository.findById(employeeId).orElse(null));
        }
        workHourRepository.save(workHour);
        return "redirect:/workhour";
    }

    // add new work hour
    @RequestMapping(value = "/addworkhour", method = RequestMethod.GET)
    public String addWorkHour(@RequestParam("employeeId") Long employeeId,
            @RequestParam("date") String date,
            Model model) {

        WorkHour wh = new WorkHour();
        wh.setDate(LocalDate.parse(date));
        wh.setEmployee(employeeRepository.findById(employeeId).orElse(null));

        model.addAttribute("workhour", wh);
        return "addworkhour";
    }

    // save new work hour
    @RequestMapping(value = "/saveworkhour", method = RequestMethod.POST)
    public String saveWorkHour(@ModelAttribute WorkHour workhour,
            @RequestParam("employeeId") Long employeeId) {

        workhour.setEmployee(employeeRepository.findById(employeeId).orElse(null));
        workHourRepository.save(workhour);

        return "redirect:/workhour";
    }

    // add new day that doesnt have work hours yet
    @RequestMapping(value = "/addday", method = RequestMethod.GET)
    public String addWorkDay(@RequestParam("employeeId") Long employeeId, Model model) {
        model.addAttribute("employeeId", employeeId);
        return "addday";
    }

    // save new day just added
    @RequestMapping(value = "/createday", method = RequestMethod.POST)
    public String createEmptyDay(@RequestParam("employeeId") Long employeeId,
            @RequestParam("date") String date) {
        if (!isManager()
                && !employeeRepository.findById(employeeId).orElseThrow().getUsername().equals(currentUsername())) {
            throw new AccessDeniedException("You do not have permission to add days for this employee!");
        }
        WorkHour workHour = new WorkHour();
        workHour.setEmployee(employeeRepository.findById(employeeId).orElse(null));
        workHour.setDate(LocalDate.parse(date));
        workHour.setCheckIn(null); // null time
        workHour.setCheckOut(null); // null time

        workHourRepository.save(workHour);

        return "redirect:/workhour";
    }

}
