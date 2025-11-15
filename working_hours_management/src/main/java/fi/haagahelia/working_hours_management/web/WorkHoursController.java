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

import org.h2.engine.Mode;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = { "/workhour" })
    public String workhour(Model model) {
        List<WorkHour> workHours = workHoursService.getAllWorkHours();

        Map<Employee, Map<LocalDate, WorkHour>> workMap = new LinkedHashMap<>();

        // group work hours by employee
        workHours.stream()
                .collect(Collectors.groupingBy(WorkHour::getEmployee,
                        () -> new TreeMap<>(Comparator.comparing(Employee::getId)), // sorting by id
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

        // collect dates data from WorkHour to a list
        List<LocalDate> allDates = workHours.stream()
                .map(WorkHour::getDate)
                .distinct()
                .sorted()
                .toList();

        model.addAttribute("workMap", workMap);
        model.addAttribute("dates", allDates);

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
    public String save(Employee employee, @RequestParam("date") String date,
            @RequestParam("checkIn") String checkIn,
            @RequestParam("checkOut") String checkOut) {
        Employee savedEmployee = employeeRepository.save(employee);
        WorkHour workHour = new WorkHour();
        workHour.setEmployee(savedEmployee);
        workHour.setDate(LocalDate.parse(date));
        workHour.setCheckIn(LocalTime.parse(checkIn));
        workHour.setCheckOut(LocalTime.parse(checkOut));

        workHourRepository.save(workHour);
        return "redirect:workhour";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteEmployee(@PathVariable("id") Long id, Model model) {
        employeeRepository.deleteById(id);
        return "redirect:../worklist";
    }

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

    @RequestMapping(value = { "/update" }, method = RequestMethod.POST)
    public String updateEmployee(Employee employee) {
        employeeRepository.save(employee);
        return "redirect:worklist";
    }

    @RequestMapping(value = "/editworkhour/{id}", method = RequestMethod.GET)
    public String editWorkHour(@PathVariable("id") Long id, Model model) {
        Optional<WorkHour> workHour = workHourRepository.findById(id);
        if (workHour.isEmpty()) {
            return "redirect:/workhour";
        }
        model.addAttribute("workhour", workHour.get());
        model.addAttribute("employees", employeeRepository.findAll());
        return "editworkhour";
    }

    @RequestMapping(value = "/updateworkhour", method = RequestMethod.POST)
    public String updateWorkHour(@ModelAttribute WorkHour workHour,
            @RequestParam("employee") Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        workHour.setEmployee(employee);
        workHourRepository.save(workHour);
        return "redirect:/workhour";
    }

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

    @RequestMapping(value = "/saveworkhour", method = RequestMethod.POST)
    public String saveWorkHour(@ModelAttribute WorkHour workhour,
            @RequestParam("employeeId") Long employeeId) {

        workhour.setEmployee(employeeRepository.findById(employeeId).orElse(null));
        workHourRepository.save(workhour);

        return "redirect:/workhour";
    }

}
