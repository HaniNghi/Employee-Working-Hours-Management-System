package fi.haagahelia.working_hours_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fi.haagahelia.working_hours_management.domain.Employee;
import fi.haagahelia.working_hours_management.domain.EmployeeRepository;
import fi.haagahelia.working_hours_management.domain.Manager;
import fi.haagahelia.working_hours_management.domain.ManagerRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Find Employee
        Optional<Employee> empOpt = employeeRepository.findByUsername(username);
        if (empOpt.isPresent()) {
            var emp = empOpt.get();
            System.out.println("Employee role: ROLE_EMPLOYEE");
            return new User(emp.getUsername(), emp.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE")));

        }

        // Find Manager
        Optional<Manager> mgrOpt = managerRepository.findByUsername(username);
        if (mgrOpt.isPresent()) {
            var mgr = mgrOpt.get();
            return new User(mgr.getUsername(), mgr.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_MANAGER")));
        }
        throw new UsernameNotFoundException("User not found");
    }
}