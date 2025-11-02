package fi.haagahelia.working_hours_management.domain;

public class Employee {
    private String firstName;
    private String lastName;
    private String securityNumber;
    private String startDate;
    private String position;

    public Employee() {

    }

    public Employee(String firstName, String lastName, String securityNumber, String startDate, String position) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.securityNumber = securityNumber;
        this.startDate = startDate;
        this.position = position;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }



}
