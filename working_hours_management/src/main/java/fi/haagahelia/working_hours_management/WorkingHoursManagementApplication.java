package fi.haagahelia.working_hours_management;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fi.haagahelia.working_hours_management.domain.Employee;
import fi.haagahelia.working_hours_management.domain.EmployeeRepository;
import fi.haagahelia.working_hours_management.domain.Manager;
import fi.haagahelia.working_hours_management.domain.ManagerRepository;

@SpringBootApplication
public class WorkingHoursManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkingHoursManagementApplication.class, args);
	}

	// demo data to apllication
	@Bean
	public CommandLineRunner demo(EmployeeRepository employeeRepository, ManagerRepository managerRepository) {
		Manager manager1 = new Manager("Anna", "Johnson", "anna.johnson@company.com");
        Manager manager2 = new Manager("John", "Smith", "john.smith@company.com");
        Manager manager3 = new Manager("Maria", "Davis", "maria.davis@company.com");

        managerRepository.save(manager1);
        managerRepository.save(manager2);
        managerRepository.save(manager3);

		return (args) -> {
		employeeRepository.save(new Employee("David", "Brown", "david.brown@company.com", "EMP001", "2020-03-01", "Software Engineer", manager1));
        employeeRepository.save(new Employee("Linda", "Miller", "linda.miller@company.com", "EMP002", "2021-05-10", "QA Engineer", manager1));
        employeeRepository.save(new Employee("Thomas", "Wilson", "thomas.wilson@company.com", "EMP003", "2022-01-15", "DevOps Engineer", manager1));
        employeeRepository.save(new Employee("Kevin", "Taylor", "kevin.taylor@company.com", "EMP004", "2019-09-20", "Backend Developer", manager2));
        employeeRepository.save(new Employee("Emily", "Moore", "emily.moore@company.com", "EMP005", "2023-02-11", "Frontend Developer", manager2));
        employeeRepository.save(new Employee("Sophia", "White", "sophia.white@company.com", "EMP006", "2020-07-03", "UI/UX Designer", manager3));
        employeeRepository.save(new Employee("James", "Harris", "james.harris@company.com", "EMP007", "2021-10-08", "Business Analyst", manager3));
        employeeRepository.save(new Employee("Robert", "Clark", "robert.clark@company.com", "EMP008", "2022-03-12", "System Architect", manager1));
        employeeRepository.save(new Employee("William", "Lewis", "william.lewis@company.com", "EMP009", "2018-12-01", "Database Administrator", manager2));
        employeeRepository.save(new Employee("Olivia", "Hall", "olivia.hall@company.com", "EMP010", "2023-04-19", "Software Engineer", manager3));
        employeeRepository.save(new Employee("Daniel", "Young", "daniel.young@company.com", "EMP011", "2019-06-27", "QA Tester", manager1));
        employeeRepository.save(new Employee("Ava", "King", "ava.king@company.com", "EMP012", "2020-08-15", "Product Owner", manager2));
        employeeRepository.save(new Employee("Noah", "Wright", "noah.wright@company.com", "EMP013", "2021-01-10", "Project Manager", manager3));
        employeeRepository.save(new Employee("Mia", "Scott", "mia.scott@company.com", "EMP014", "2022-05-03", "Technical Writer", manager1));
        employeeRepository.save(new Employee("Ethan", "Adams", "ethan.adams@company.com", "EMP015", "2023-07-11", "Software Engineer", manager2));
        employeeRepository.save(new Employee("Grace", "Baker", "grace.baker@company.com", "EMP016", "2019-02-01", "System Analyst", manager3));
        employeeRepository.save(new Employee("Lucas", "Nelson", "lucas.nelson@company.com", "EMP017", "2020-12-09", "Data Analyst", manager1));
        employeeRepository.save(new Employee("Isabella", "Carter", "isabella.carter@company.com", "EMP018", "2021-06-24", "Cloud Engineer", manager2));
        employeeRepository.save(new Employee("Henry", "Mitchell", "henry.mitchell@company.com", "EMP019", "2022-09-17", "Frontend Developer", manager3));
        employeeRepository.save(new Employee("Ella", "Perez", "ella.perez@company.com", "EMP020", "2023-01-05", "HR Specialist", manager1));

		};
	}
}
