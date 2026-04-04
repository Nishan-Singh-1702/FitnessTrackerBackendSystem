package com.Fitness.service;

import com.Fitness.exception.APIException;
import com.Fitness.exception.ResourceNotFoundException;
import com.Fitness.model.AppRole;
import com.Fitness.model.Role;
import com.Fitness.model.User;
import com.Fitness.payload.UserDTO;
import com.Fitness.payload.UserResponse;
import com.Fitness.repository.RoleRepository;
import com.Fitness.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public UserDTO createNewUser(UserDTO userDTO) {
        Optional<User> userFromDb = userRepository.findByEmail(userDTO.getEmail());
        if(userFromDb.isPresent())throw new APIException("User already exist with email: "+userDTO.getEmail());
        User savedUser = modelMapper.map(userDTO, User.class);

        Set<Role> roles = new HashSet<>();
        if(userDTO.getRoles()==null || userDTO.getRoles().isEmpty()){
            Role role = roleRepository.findByRole(AppRole.ROLE_USER).orElseThrow(()->new ResourceNotFoundException("Role","roleName","ROLE_USER"));
            roles.add(role);
        }
        else{
            userDTO.getRoles().forEach(role->{
                switch (role.toLowerCase()){
                    case "admin" :
                        Role adminRole = roleRepository.findByRole(AppRole.ROLE_ADMIN).orElseThrow(()->new ResourceNotFoundException("Role","roleName","ROLE_ADMIN"));
                        roles.add(adminRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByRole(AppRole.ROLE_USER).orElseThrow(()->new ResourceNotFoundException("Role","roleName","ROLE_USER"));
                        roles.add(userRole);
                }
            });
        }
        savedUser.setRoles(roles);
        savedUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return modelMapper.map(userRepository.save(savedUser),UserDTO.class);
    }

    @Override
    public UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<User> page = userRepository.findAll(pageable);
        List<UserDTO> userDTOS = page.stream()
                .map(user->modelMapper.map(user,UserDTO.class))
                .toList();
        UserResponse response = new UserResponse();
        response.setContent(userDTOS);
        response.setPageNumber(pageable.getPageNumber());
        response.setPageSize(pageable.getPageSize());
        response.setTotalElement(page.getTotalElements());
        response.setTotalPage(page.getTotalPages());
        response.setLastPage(page.isLast());
        return response;
    }

    @Override
    public UserDTO getUserById(Long userId) {
        User userFromDb = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","userId",userId));
        return modelMapper.map(userFromDb,UserDTO.class);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User userFromDb = userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User","Email",email));
        return modelMapper.map(userFromDb,UserDTO.class);
    }
}
