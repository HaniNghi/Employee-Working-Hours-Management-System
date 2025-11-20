package fi.haagahelia.working_hours_management.web;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fi.haagahelia.working_hours_management.domain.Employee;
import fi.haagahelia.working_hours_management.domain.EmployeeRepository;
import fi.haagahelia.working_hours_management.domain.Manager;
import fi.haagahelia.working_hours_management.domain.ManagerRepository;

@Controller
public class AuthController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailSender mailSender;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    // Login form
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Forgot password form
    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }

    // Handling forgotten password
    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam String username, org.springframework.ui.Model model) {
        System.out.println("Received username: " + username);
        logger.info("Controller invoked");
        // Find Employee or Manager
        Optional<Employee> empOpt = employeeRepository.findByUsername(username);
        Optional<Manager> mgrOpt = managerRepository.findByUsername(username);

        if (empOpt.isEmpty() && mgrOpt.isEmpty()) {
            model.addAttribute("error", "Username not found");
            return "forgot-password";
        }

        String newPassword = generateRandomPassword();
        String encodedPassword = passwordEncoder.encode(newPassword);

        String email = "";

        if (empOpt.isPresent()) {
            Employee emp = empOpt.get();
            emp.setPassword(encodedPassword);
            employeeRepository.save(emp);
            email = emp.getEmail();
        } else {
            Manager mgr = mgrOpt.get();
            mgr.setPassword(encodedPassword);
            managerRepository.save(mgr);
            email = mgr.getEmail();
        }

        // Test email and new password is ready
        System.out.println("Sending email to: " + email + " with new password: " + newPassword);
        // Send email
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("New Password");
            message.setText("Your new password is: " + newPassword);
            mailSender.send(message);

            model.addAttribute("message", "New password has been sent to your email.");
        } catch (Exception e) {

            model.addAttribute("error", "Failed to send email. Check your mail configuration." + e);
        }
        return "forgot-password";
    }

    // Create random password
    private String generateRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 8); // 8 ký tự
    }

    // Send email
    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
