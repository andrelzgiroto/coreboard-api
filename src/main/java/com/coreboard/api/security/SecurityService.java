package com.coreboard.api.security;

import com.coreboard.api.domain.entity.Employee;
import com.coreboard.api.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final EmployeeRepository employeeRepository;

    public Employee findUserLogged() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return employeeRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Logged-in employee not found in the database."));
    }

}