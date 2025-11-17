package fi.haagahelia.working_hours_management.domain;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    private String username; // dùng để login
    private String password; // mật khẩu mã hóa (BCrypt)


    @ManyToOne
    @JoinColumn(name = "managerId")
    private Manager manager;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.REMOVE)
    private List<WorkHour> workHours;



    public Employee() {

    }

    public Employee(String firstName, String lastName, String email, Manager manager, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.manager = manager;
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public List<WorkHour> getWorkHours() {
        return workHours;
    }

    public void setWorkHours(List<WorkHour> workHours) {
        this.workHours = workHours;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }





}
