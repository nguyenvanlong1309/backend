package com.tuthien.backend.controller;

import com.tuthien.backend.model.*;
import com.tuthien.backend.service.UserService;
import com.tuthien.backend.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity login(@Validated @RequestBody LoginModel loginModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            ResponseModel responseModel = new ResponseModel();
            responseModel.setData(fieldError.getDefaultMessage());
            responseModel.setStatus(HttpStatus.BAD_REQUEST);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginModel.getUsername(),
                loginModel.getPassword()
        );
        Authentication authenticate = null;

        try {
            authenticate = this.authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException badCredentialsException) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseModel<>(HttpStatus.BAD_REQUEST, null, "T??i kho???n ho???c m???t kh???u kh??ng ????ng"));
        } catch (LockedException exception) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseModel<>(HttpStatus.BAD_REQUEST, null, "T??i kho???n ???? b??? kh??a. Li??n h??? v???i admin ????? m???."));
        }

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = this.jwtUtils.generateToken(loginModel.getUsername());
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setToken(token);
        jwtResponse.setUser(this.userService.getUserResponseByUsername(loginModel.getUsername()));
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/regis-user")
    public ResponseEntity registerUser(@Validated @RequestBody UserModel userModel, BindingResult bindingResult) {
        ResponseModel responseModel = this.userService.regisUser(userModel, bindingResult);
        return ResponseEntity.status(responseModel.getStatus()).body(responseModel);
    }

    @PostMapping("/change-password")
    public ResponseEntity changePassword(@RequestBody ChangePasswordModel changePasswordModel) {
        ResponseModel responseModel = this.userService.changePassword(changePasswordModel);
        return ResponseEntity.ok(responseModel);
    }

    @PostMapping("/forget-password")
    public ResponseEntity forgetPassword(@RequestBody UserModel userModel) {
        ResponseModel responseModel = this.userService.forgetPassword(userModel);
        return ResponseEntity.ok(responseModel);
    }
}
