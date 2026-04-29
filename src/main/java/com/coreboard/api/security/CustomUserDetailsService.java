package com.coreboard.api.security;

import com.coreboard.api.domain.entity.Employee;
import com.coreboard.api.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found"));

        return User
                .builder()
                .username(employee.getEmail())
                .password(employee.getPassword())
                .authorities(employee.getEmployeeType().name())
                .build();
    }
}
