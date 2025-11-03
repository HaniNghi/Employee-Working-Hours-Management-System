package fi.haagahelia.working_hours_management.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
@Entity
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String securityNumber;
    private String startDate;

    public Manager() {

    }

    public Manager(String firstName, String lastName, String email, String securityNumber, String startDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.securityNumber = securityNumber;
        this.startDate = startDate;
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

    public String getSecurityNumber() {
        return securityNumber;
    }

    public void setSecurityNumber(String securityNumber) {
        this.securityNumber = securityNumber;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

}
