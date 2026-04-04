package com.Fitness.Controller;

import com.Fitness.exception.ResourceNotFoundException;
import com.Fitness.model.AppRole;
import com.Fitness.model.Role;
import com.Fitness.model.User;
import com.Fitness.repository.RoleRepository;
import com.Fitness.repository.UserRepository;
import com.Fitness.security.jwt.JwtUtils;
import com.Fitness.security.request.LoginRequest;
import com.Fitness.security.request.SignUpRequest;
import com.Fitness.security.response.MessageResponse;
import com.Fitness.security.response.UserInfoResponse;
import com.Fitness.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        Authentication authentication;
        try{
            System.out.println("Login attempt: " + loginRequest.getUserEmail());
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserEmail(),loginRequest.getPassword()));
        }
        catch(Exception exception){
            exception.printStackTrace();
            Map<String, Object> map = new HashMap<>();
            map.put("message","Bad Credential");
            map.put("status",false);
            return new ResponseEntity<Object>(map,HttpStatus.NOT_FOUND);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//        String jwtToken = jwtUtils.generateJwtTokenFromUserName(userDetails);
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
//        UserInfoResponse response = new UserInfoResponse(userDetails.getId(),jwtToken,userDetails.getEmail(),roles);
        UserInfoResponse response = new UserInfoResponse(userDetails.getId(),userDetails.getEmail(),roles);
//        return ResponseEntity.ok(response);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,jwtCookie.toString()).body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest){
        Optional<User> userFromDb = userRepository.findByEmail(signUpRequest.getEmail());
        if(userFromDb.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Error: Email is Already taken !!"));
        }
        User user = new User(signUpRequest.getFirstName(),signUpRequest.getLastName(),signUpRequest.getEmail(),passwordEncoder.encode(signUpRequest.getPassword()));


        Set<Role> roles = new HashSet<>();
        if(signUpRequest.getRole()==null || signUpRequest.getRole().isEmpty()){
            roles.add(roleRepository.findByRole(AppRole.ROLE_USER).orElseThrow(()->new ResourceNotFoundException("Role","roleName","ROLE_USER")));
        }
        else{
            signUpRequest.getRole().forEach(role->{
                switch (role.toLowerCase()){
                    case "admin" :
                        roles.add(roleRepository.findByRole(AppRole.ROLE_ADMIN).orElseThrow(()->new ResourceNotFoundException("Role","roleName","ROLE_ADMIN")));
                        break;
                    default:
                        roles.add(roleRepository.findByRole(AppRole.ROLE_USER).orElseThrow(()->new ResourceNotFoundException("Role","roleName","ROLE_USER")));
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User register successfully !!"));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOutUser(){
        ResponseCookie cookie = jwtUtils.generateCleanCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString()).body(new MessageResponse("You've been SignedOut !!"));
    }
}
