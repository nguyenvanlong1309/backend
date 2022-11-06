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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

    public UserResponse getUserResponseByUsername(String username) {
        User user = this.getUserByUsername(username);
        return this.objectMapper.convertValue(user, UserResponse.class);
    }

    public ResponseModel regisUser(UserModel userModel, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                FieldError fieldError = bindingResult.getFieldError();
                throw new IllegalArgumentException(fieldError.getDefaultMessage());
            }

            this.userDAO.findByUsername(userModel.getUsername())
                    .ifPresent((u) -> new IllegalArgumentException("Tên đăng nhập đã tồn tại"));

            if ("ADMIN".equals(userModel.getRole())) {
                userModel.setRole("USER");
            }
            String encodedPassword = this.passwordEncoder.encode(userModel.getPassword());
            User user = this.objectMapper.convertValue(userModel, User.class);
            user.setId(UUID.randomUUID().toString());
            user.setPassword(encodedPassword);
            user.setStatus(UserStatus.ACTIVE.getStatus());
            this.userDAO.save(user);
            return new ResponseModel(HttpStatus.OK, null);
        } catch (IllegalArgumentException illegalArgumentException) {
            return new ResponseModel(HttpStatus.BAD_REQUEST, illegalArgumentException.getMessage());
        } catch (Exception exception) {
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }
}
