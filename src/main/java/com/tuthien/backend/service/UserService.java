package com.tuthien.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuthien.backend.constant.UserStatus;
import com.tuthien.backend.dao.UserDAO;
import com.tuthien.backend.entity.User;
import com.tuthien.backend.model.ResponseModel;
import com.tuthien.backend.model.UserModel;
import com.tuthien.backend.model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.getUserByUsername(username);
        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.getPassword())
                .authorities(user.getRole())
                .disabled(UserStatus.INACTIVE.getStatus() == user.getStatus())
                .roles(user.getRole())
                .build();
    }

    public User getUserByUsername(String username) {
        return this.userDAO.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(""));
    }

    public User getSessionUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return this.getUserByUsername(userDetails.getUsername());
        } catch (UsernameNotFoundException ex) {
            return null;
        }
    }

    public UserResponse getUserResponseByUsername(String username) {
        User user = this.getUserByUsername(username);
        return this.objectMapper.convertValue(user, UserResponse.class);
    }

    public ResponseModel regisUser(UserModel userModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            throw new IllegalArgumentException(fieldError.getDefaultMessage());
        }

        this.userDAO.findByUsername(userModel.getUsername())
                .ifPresent((u) -> new IllegalArgumentException("Tên đăng nhập đã tồn tại"));

        this.userDAO.findByPhone(userModel.getPhone())
                .ifPresent((u) -> new IllegalArgumentException("SĐT đã tồn tại"));

        String encodedPassword = this.passwordEncoder.encode(userModel.getPassword());
        User user = this.objectMapper.convertValue(userModel, User.class);
        user.setId(UUID.randomUUID().toString());
        user.setPassword(encodedPassword);
        user.setStatus(UserStatus.ACTIVE.getStatus());
        user.setRole("USER");
        this.userDAO.save(user);
        return new ResponseModel(HttpStatus.OK, null);
    }

    public UserResponse updateInfo(UserModel userModel) {
        User _user = this.userDAO.findByUsername(userModel.getUsername())
                .orElseThrow(() -> new IllegalArgumentException(userModel.getUsername() + " không tồn tại"));
        userModel.setPassword(_user.getPassword());
        userModel.setStatus(_user.getStatus());

        if (StringUtils.hasText(userModel.getPhone())) {
            this.userDAO.findByPhone(userModel.getPhone())
                    .filter(u -> !u.getId().equals(_user.getId()))
                    .ifPresent((u) -> {
                        throw new IllegalArgumentException("SĐT đã tồn tại");
                    });
        }

        if (StringUtils.hasText(userModel.getEmail())) {
            this.userDAO.findByEmail(userModel.getEmail())
                    .filter(u -> !u.getId().equals(_user.getId()))
                    .ifPresent((u) -> {
                        throw new IllegalArgumentException("Email đã tồn tại");
                    });
        }

        User user = this.objectMapper.convertValue(userModel, User.class);
        user = this.userDAO.save(user);
        return this.objectMapper.convertValue(user, UserResponse.class);
    }
}
