package com.tuthien.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuthien.backend.constant.UserStatus;
import com.tuthien.backend.dao.UserDAO;
import com.tuthien.backend.entity.User;
import com.tuthien.backend.model.ChangePasswordModel;
import com.tuthien.backend.model.ResponseModel;
import com.tuthien.backend.model.UserModel;
import com.tuthien.backend.model.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserDAO userDAO;

    private final ObjectMapper objectMapper;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    public List<UserResponse> findAll() {
        return this.userDAO.findAll().stream()
                .filter(_user -> !"ADMIN".equals(_user.getRole()))
                .map(_user -> this.objectMapper.convertValue(_user, UserResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.getUserByUsername(username);
        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.getPassword())
                .authorities(user.getRole())
                .accountLocked(!UserStatus.ACTIVE.getStatus().equals(user.getStatus()))
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
        } catch (Exception ex) {
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
                .ifPresent((u) -> new IllegalArgumentException("T??n ????ng nh???p ???? t???n t???i"));

        this.userDAO.findByPhone(userModel.getPhone())
                .ifPresent((u) -> new IllegalArgumentException("S??T ???? t???n t???i"));

        this.userDAO.findByEmail(userModel.getEmail())
                .ifPresent((u) -> {
                    throw new IllegalArgumentException("Email ???? t???n t???i");
                });

        User sessionUser = this.getSessionUser();
        String encodedPassword = this.passwordEncoder.encode(userModel.getPassword());
        User user = this.objectMapper.convertValue(userModel, User.class);
        user.setId(UUID.randomUUID().toString());
        user.setPassword(encodedPassword);
        user.setStatus(UserStatus.ACTIVE.getStatus());
        this.userDAO.save(user);
        return new ResponseModel(HttpStatus.OK, null);
    }

    public UserResponse updateInfo(UserModel userModel) {
        User _user = this.userDAO.findByUsername(userModel.getUsername())
                .orElseThrow(() -> new IllegalArgumentException(userModel.getUsername() + " kh??ng t???n t???i"));
        userModel.setId(_user.getId());
        userModel.setPassword(_user.getPassword());
        userModel.setStatus(_user.getStatus());
        userModel.setUsername(_user.getUsername());

        this.userDAO.findByPhone(userModel.getPhone())
                .filter(u -> !u.getId().equals(_user.getId()))
                .ifPresent((u) -> {
                    throw new IllegalArgumentException("S??T ???? t???n t???i");
                });

        this.userDAO.findByEmail(userModel.getEmail())
                .filter(u -> !u.getId().equals(_user.getId()))
                .ifPresent((u) -> {
                    throw new IllegalArgumentException("Email ???? t???n t???i");
                });

        User user = this.objectMapper.convertValue(userModel, User.class);
        User sessionUser = this.getSessionUser();
        if (!"ADMIN".equals(sessionUser.getRole())) {
            user.setRole(_user.getRole());
        }
        user = this.userDAO.save(user);
        return this.objectMapper.convertValue(user, UserResponse.class);
    }

    @Transactional
    public ResponseModel changeStatusUser(String username, UserStatus userStatus) {
        User user = this.getUserByUsername(username);
        user.setStatus(userStatus.getStatus());
        this.userDAO.save(user);
        return new ResponseModel(HttpStatus.OK, this.objectMapper.convertValue(user, UserResponse.class), "Th??nh c??ng");
    }

    @Transactional
    public ResponseModel changePassword(ChangePasswordModel changePasswordModel) {
        User currentUser = this.getSessionUser();
        boolean matches = this.passwordEncoder.matches(changePasswordModel.getCurrentPassword(),
                currentUser.getPassword());
        if (!matches) {
            throw new IllegalArgumentException("M???t kh???u hi???n t???i kh??ng ????ng");
        }
        String newPasswordEncoded = this.passwordEncoder.encode(changePasswordModel.getNewPassword());
        currentUser.setPassword(newPasswordEncoded);
        this.userDAO.save(currentUser);

        UserDetails userDetails = this.loadUserByUsername(currentUser.getUsername());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authenticationToken);
        return new ResponseModel(HttpStatus.OK, null, "Th??nh c??ng");
    }

    public ResponseModel forgetPassword(UserModel userModel) {
        User user = this.userDAO.findByUsername(userModel.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Kh??ng t??m th???y t??i kho???n"));

        String newPass = ((int) (Math.random() * 899999 + 100000)) + "";

        String content = "M???t kh???u m???i c???a b???n l??: " + newPass;
        this.mailService.sendMailAsync(user.getEmail(), content, "L???Y LAI M???T KH???U");
        user.setPassword(this.passwordEncoder.encode(newPass));
        this.userDAO.save(user);
        return new ResponseModel(HttpStatus.OK, null, "Th??nh c??ng");
    }
}
