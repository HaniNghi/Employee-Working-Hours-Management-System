package fi.haagahelia.working_hours_management.domain;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    Optional<Employee> findByUsername(String username);
}
