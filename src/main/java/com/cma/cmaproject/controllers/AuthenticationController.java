package com.cma.cmaproject.controllers;

import com.cma.cmaproject.jwtconfigs.JwtUtil;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import com.cma.cmaproject.wrappers.JwtWrapper;
import com.cma.cmaproject.wrappers.UserLoginWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/cma-token")
public class AuthenticationController {
  /*  @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/")
    public String welcome() {
        return "WELCOME TO CMA AUTHENTICATION OF APIs!!";
    }

    @PostMapping("/authenticate")
    public String generateToken(@RequestBody UserLoginWrapper userLoginWrapper) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginWrapper.getUsername(), userLoginWrapper.getPassword())
            );
        } catch (Exception ex) {
            throw new Exception("inavalid username/password");
        }
        return jwtUtil.generateToken(userLoginWrapper.getUsername());
    }

    @PostMapping("/authenticate2")
    public GenericResponseWrapper generateToken2(@RequestBody UserLoginWrapper userLoginWrapper) throws Exception {
        GenericResponseWrapper genericResponseWrapper=new GenericResponseWrapper();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginWrapper.getUsername(), userLoginWrapper.getPassword())
            );
            String token=jwtUtil.generateToken(userLoginWrapper.getUsername());
            JwtWrapper jwtWrapper=new JwtWrapper();
            jwtWrapper.setUsername(userLoginWrapper.getUsername());
            jwtWrapper.setToken(token);
            jwtWrapper.setTokenVality("10 HRS");
            genericResponseWrapper.setData(jwtWrapper);
            genericResponseWrapper.setStatus("SUCCESS");
            genericResponseWrapper.setCode(200);
            genericResponseWrapper.setMessage("Token Generated Successfully");

        } catch (Exception ex) {
            genericResponseWrapper.setData(null);
            genericResponseWrapper.setStatus("FAILED");
            genericResponseWrapper.setCode(301);
            genericResponseWrapper.setMessage("Invalid Username/Password");

        }
        return genericResponseWrapper;
    }*/
}
