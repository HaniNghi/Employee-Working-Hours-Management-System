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
        System.out.println("updating employee" + employee.getId() + "name" + employee.getFirstName());
        employeeRepository.save(employee);
        return "redirect:worklist";
    }

    @RequestMapping(value = { "/modify/{id}" }, method = RequestMethod.GET)
    public String modifyWorkHoursForm(@PathVariable("id") Long employeeId, Model model) {
        Employee employee = employeeRepository.findById(employeeId).orElse(new Employee());

        List<WorkHour> workHours = workHourRepository.findByEmployeeId(employeeId);

        model.addAttribute("employee", employee);
        model.addAttribute("workHours", workHours);

        return "modify";
    }

    @RequestMapping(value = {"/modify/{id}"}, method = RequestMethod.POST)
    public String modifyWorkHoursSubmit(@PathVariable("id") Long employeeId,
            @ModelAttribute("workHours") List<WorkHour> workHours) {
        Employee employee = employeeRepository.findById(employeeId).orElse(new Employee());

        for (WorkHour wh : workHours) {
            wh.setEmployee(employee);
            workHourRepository.save(wh);
        }
        return "redirect:/workhour";
    }
}
