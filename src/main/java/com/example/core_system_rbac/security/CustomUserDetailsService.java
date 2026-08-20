package com.example.core_system_rbac.security;

import com.example.core_system_rbac.entities.User;
import com.example.core_system_rbac.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Tìm user theo email và đảm bảo chưa bị xóa mềm
        User user = userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy User với email: " + email));

        // Ánh xạ Roles của user thành GrantedAuthority của Spring Security
        List<SimpleGrantedAuthority> authorities = user.getUserRoles().stream()
                .map(ur -> new SimpleGrantedAuthority("ROLE_" + ur.getRole().getRoleCode()))
                .collect(Collectors.toList());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail()) // Hệ thống dùng Email làm tài khoản
                .password(user.getPassword())
                .authorities(authorities)
                .disabled(!user.getIsActive())
                .build();
    }
}