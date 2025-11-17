package fi.haagahelia.working_hours_management;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import fi.haagahelia.working_hours_management.domain.Employee;
import fi.haagahelia.working_hours_management.domain.EmployeeRepository;
import fi.haagahelia.working_hours_management.domain.Manager;
import fi.haagahelia.working_hours_management.domain.ManagerRepository;
import fi.haagahelia.working_hours_management.domain.WorkHour;
import fi.haagahelia.working_hours_management.domain.WorkHourRepository;

@SpringBootApplication
public class WorkingHoursManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkingHoursManagementApplication.class, args);
    }

    // demo data to apllication
    @Bean
    public CommandLineRunner demo(EmployeeRepository employeeRepository, ManagerRepository managerRepository,
            WorkHourRepository workHourRepository, PasswordEncoder passwordEncoder) {

        return (args) -> {
            Manager manager1 = new Manager("Anna", "Johnson", "anna.johnson@company.com", "anna", passwordEncoder.encode("pass1234"));
            Manager manager2 = new Manager("John", "Smith", "john.smith@company.com", "john", passwordEncoder.encode("pass1234"));
            Manager manager3 = new Manager("Maria", "Davis", "maria.davis@company.com", "maria", passwordEncoder.encode("pass1234"));

            managerRepository.save(manager1);
            managerRepository.save(manager2);
            managerRepository.save(manager3);

            employeeRepository.save(new Employee("David", "Brown", "libraray2021@gmail.com", manager1, "david", passwordEncoder.encode("1234")));
            employeeRepository.save(new Employee("Linda", "Miller", "linda.miller@company.com", manager1, "linda",  passwordEncoder.encode("1234")));
            employeeRepository.save(new Employee("Thomas", "Wilson", "thomas.wilson@company.com", manager1, "thomas", passwordEncoder.encode("1234")));
            employeeRepository.save(new Employee("Kevin", "Taylor", "kevin.taylor@company.com", manager2, "kevin", passwordEncoder.encode("1234")));
            employeeRepository.save(new Employee("Emily", "Moore", "emily.moore@company.com", manager2, "emily", passwordEncoder.encode("1234")));
            employeeRepository.save(new Employee("Sophia", "White", "sophia.white@company.com", manager3, "sophia", passwordEncoder.encode("1234")));
            employeeRepository.save(new Employee("James", "Harris", "james.harris@company.com", manager3, "james", passwordEncoder.encode("1234")));
            employeeRepository.save(new Employee("Robert", "Clark", "robert.clark@company.com", manager1, "robert", passwordEncoder.encode("1234")));
            employeeRepository.save(new Employee("William", "Lewis", "william.lewis@company.com", manager2, "william", passwordEncoder.encode("1234")));
            employeeRepository.save(new Employee("Olivia", "Hall", "olivia.hall@company.com", manager3, "olivia", passwordEncoder.encode("1234")));
            employeeRepository.save(new Employee("Daniel", "Young", "daniel.young@company.com", manager1, "daniel", passwordEncoder.encode("1234")));
            employeeRepository.save(new Employee("Ava", "King", "ava.king@company.com", manager2, "ava", passwordEncoder.encode("1234")));
            employeeRepository.save(new Employee("Noah", "Wright", "noah.wright@company.com", manager3, "noah", passwordEncoder.encode("1234")));
            employeeRepository.save(new Employee("Mia", "Scott", "mia.scott@company.com", manager1, "mia", passwordEncoder.encode("1234")));
            employeeRepository.save(new Employee("Ethan", "Adams", "ethan.adams@company.com", manager2, "ethan", passwordEncoder.encode("1234")));
            employeeRepository.save(new Employee("Grace", "Baker", "grace.baker@company.com", manager3, "grace", passwordEncoder.encode("1234")));
            employeeRepository.save(new Employee("Lucas", "Nelson", "lucas.nelson@company.com", manager1, "lucas", passwordEncoder.encode("1234")));
            employeeRepository.save(new Employee("Isabella", "Carter", "isabella.carter@company.com", manager2, "isabella", passwordEncoder.encode("1234")));
            employeeRepository.save(new Employee("Henry", "Mitchell", "henry.mitchell@company.com", manager3, "henry", passwordEncoder.encode("1234")));
            employeeRepository.save(new Employee("Ella", "Perez", "ella.perez@company.com", manager1, "ella", passwordEncoder.encode("1234")));


            Random random = new Random();

            // create dummy data for work hours
            for (long employeeId = 1; employeeId <= 20; employeeId++) {
                LocalDate date = LocalDate.of(2025, 11, 3);
                for (int i = 0; i < 5; i++) { // 5 days work
                    int startHour = 8 + random.nextInt(4); // random start time 8am-11am
                    LocalTime startTime = LocalTime.of(startHour, random.nextBoolean() ? 0 : 30); // 0 or 30 mins
                    LocalTime endTime = startTime.plusHours(8);

                    workHourRepository.save(new WorkHour(
                            date,
                            startTime,
                            endTime,
                            employeeRepository.findById(employeeId).get()));

                    date = date.plusDays(1);
                }
            }
            ;
        };
    }
}
