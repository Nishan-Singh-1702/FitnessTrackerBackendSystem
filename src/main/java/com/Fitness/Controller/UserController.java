package com.Fitness.Controller;

import com.Fitness.config.AppConstant;
import com.Fitness.payload.UserDTO;
import com.Fitness.payload.UserResponse;
import com.Fitness.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/admin/user")
    public ResponseEntity<UserDTO> createNewUser(@Valid @RequestBody UserDTO userDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createNewUser(userDTO));
    }

    @GetMapping("/public/users")
    public ResponseEntity<UserResponse> getAllUser(@RequestParam(name = "pageNumber", required = false, defaultValue = AppConstant.pageNumber)Integer pageNumber,
                                                   @RequestParam(name = "pageSize",required = false,defaultValue = AppConstant.pageSize)Integer pageSize,
                                                   @RequestParam(name = "sortBy",required = false,defaultValue = AppConstant.sortBy)String sortBy,
                                                   @RequestParam(name = "sortOrder",required = false,defaultValue = AppConstant.sort_dir)String sortOrder){
        return ResponseEntity.ok(userService.getAllUsers(pageNumber,pageSize,sortBy,sortOrder));
    }

    @GetMapping("/public/user/userId/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("/public/user/userEmail/{userEmail}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String userEmail){
        return ResponseEntity.ok(userService.getUserByEmail(userEmail));
    }
}
