package com.example.clothing_sell_website.service.auth;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.clothing_sell_website.dto.request.AuthenticationRequest;
import com.example.clothing_sell_website.dto.request.RegisterRequest;
import com.example.clothing_sell_website.dto.respone.AuthenticationResponse;
import com.example.clothing_sell_website.dto.respone.RegisterResponse;
import com.example.clothing_sell_website.entity.Account;
import com.example.clothing_sell_website.entity.Customer;
import com.example.clothing_sell_website.entity.Role;
import com.example.clothing_sell_website.entity.Staff;
import com.example.clothing_sell_website.repository.AccountRepository;
import com.example.clothing_sell_website.repository.CustomerRepository;
import com.example.clothing_sell_website.repository.RoleRepository;
import com.example.clothing_sell_website.repository.StaffRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final JavaMailSender javaMailSender;

    @Autowired
    private final CustomerRepository customerRepository;

    @Autowired
    private final AccountRepository accountRepository;

    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final StaffRepository staffRepository;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            Optional<Account> accountOtp = accountRepository.findByUsername(request.getUsername());
            if (accountOtp.isPresent()) {
                Account account = accountOtp.get();
                String token = jwtService.generateToken(authentication);

                return AuthenticationResponse.builder()
                        .token(token)
                        .message("Đăng nhập thành công")
                        .role(account.getRole().getRoleName())
                        .success(true)
                        .build();
            }

        } catch (AuthenticationException e) {
            return AuthenticationResponse.builder()
                    .message("Tên đăng nhập hoặc mật khẩu không đúng")
                    .success(false)
                    .build();
        }
        return AuthenticationResponse.builder()
                .message("Lỗi đăng nhập")
                .success(false)
                .build();
    }

    public RegisterResponse registerNewCustomerAccount(RegisterRequest request) {
        Optional<Account> accountOptional = accountRepository.findByUsername(request.getUsername());
        if (accountOptional.isPresent()) {
            return RegisterResponse.builder()
                    .message("Username đã tồn tại")
                    .success(false)
                    .build();
        }

        Customer customer = new Customer();
        try {
            customer.setCustomerId(generateCustomerId());
            customer.setName(request.getName());
            customer.setEmail(request.getEmail());
            customer.setPhoneNum(request.getPhoneNum());
            customer.setCreditNum(request.getCreditNum());
            customerRepository.save(customer);
        } catch (Exception e) {
            return RegisterResponse.builder()
                    .message("Không thể tạo user")
                    .success(false)
                    .build();
        }

        Role role = roleRepository.findByRoleId("1");
        try {
            Account account = new Account();
            account.setUsername(request.getUsername());
            account.setPassword(passwordEncoder.encode(request.getPassword()));
            account.setCustomer(customer);
            account.setRole(role);
            accountRepository.save(account);
        } catch (Exception e) {
            return RegisterResponse.builder()
                    .message("Không thể tạo tài khoản")
                    .success(false)
                    .build();
        }
        try {
            return RegisterResponse.builder()
                    .message("Đăng ký thành công")
                    .success(true)
                    .build();
        } catch (Exception e) {
            return RegisterResponse.builder()
                    .message("Đã xảy ra sự cố")
                    .success(false)
                    .build();
        }
    }

    public RegisterResponse registerNewStaffAccount(RegisterRequest request) {
        Optional<Account> accountOptional = accountRepository.findByUsername(request.getUsername());
        if (accountOptional.isPresent()) {
            return RegisterResponse.builder()
                    .message("Username đã tồn tại")
                    .success(false)
                    .build();
        }

        Staff staff = new Staff();
        try {
            staff.setStaffId(generateStaffId());
            staff.setName(request.getName());
            staff.setEmail(request.getEmail());
            staff.setPhoneNum(request.getPhoneNum());
            staff.setSex("male");
            staff.setStatus(true);
            staffRepository.save(staff);
        } catch (Exception e) {
            return RegisterResponse.builder()
                    .message("Không thể tạo nhân viên")
                    .success(false)
                    .build();
        }

        Role role = roleRepository.findByRoleId("0");
        try {
            Account account = new Account();
            account.setUsername(request.getUsername());
            account.setPassword(passwordEncoder.encode(request.getPassword()));
            account.setStaff(staff);
            account.setRole(role);
            accountRepository.save(account);
        } catch (Exception e) {
            return RegisterResponse.builder()
                    .message("Không thể tạo tài khoản")
                    .success(false)
                    .build();
        }
        try {
            return RegisterResponse.builder()
                    .message("Đăng ký thành công")
                    .success(true)
                    .build();
        } catch (Exception e) {
            return RegisterResponse.builder()
                    .message("Đã xảy ra sự cố")
                    .success(false)
                    .build();
        }
    }

    public void sendSimpleMail(String to, String subject, String text) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);
        javaMailSender.send(simpleMailMessage);
    }

    private String generateRandomString() {
        int length = 7;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public void forgotPassword(String email) {
        Optional<Customer> customerOptional = customerRepository.findByEmail(email);
        if (customerOptional.isEmpty()) {
            throw new RuntimeException("Email không tồn tại.");
        }
        Customer customer = customerOptional.get();

        Optional<Account> accountOp = accountRepository.findByCustomer(customer);
        if (accountOp.isEmpty()) {
            throw new RuntimeException("Tài khoản cho email không tồn tại.");
        }
        Account account = accountOp.get();
        String newPassword = generateRandomString();
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);

        String subject = "Reset mật khẩu thành công";
        String text =
                "Mật khẩu mới của bạn là: " + newPassword + "\nVui lòng đăng nhập và thay đổi mật khẩu ngay lập tức.";
        sendSimpleMail(email, subject, text);
    }

    public String generateCustomerId() {
        Random random = new Random();
        String customerId = "";
        while (customerId.isEmpty() || customerRepository.findById(customerId).isPresent()) {
            customerId = "KH" + generateRandomString();
        }
        return customerId;
    }

    public String generateStaffId() {
        Random random = new Random();
        String staffId = "";
        while (staffId.isEmpty() || staffRepository.findById(staffId).isPresent()) {
            staffId = "NV" + generateRandomString();
        }
        return staffId;
    }
}
