package fi.haagahelia.working_hours_management.domain;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ManagerRepository extends CrudRepository<Manager, Long> {
    Optional<Manager> findByUsername(String username);
}
