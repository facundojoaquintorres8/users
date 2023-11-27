package com.ar.users.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ar.users.config.JwtTokenUtil;
import com.ar.users.dto.SignUpRequestDTO;
import com.ar.users.dto.SignUpResponseDTO;
import com.ar.users.dto.UserDTO;
import com.ar.users.exception.CustomException;
import com.ar.users.model.Phone;
import com.ar.users.model.User;
import com.ar.users.repository.IUserRepository;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository repository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public SignUpResponseDTO signUp(SignUpRequestDTO request) throws Exception {

        Optional<User> optionalUser = repository.findByEmail(request.getEmail());
        
        if (optionalUser.isPresent()) {
            throw new CustomException("User already exists!");
        }
    
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setCreated(new Date());
        user.setToken(generateNewToken(user.getId()));
        user.setIsActive(true);
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(encript(request.getPassword()));
        List<Phone> phones = new ArrayList<Phone>();
        request.getPhones().stream().forEach(
            p -> {
                Phone phone = new Phone();
                phone.setId(UUID.randomUUID());
                phone.setNumber(p.getNumber());
                phone.setCityCode(p.getCityCode());
                phone.setCountryCode(p.getCountryCode());
                phone.setUser(user);
                phones.add(phone);
            }
        );
        user.setPhones(phones);
        repository.save(user);
        

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(user, SignUpResponseDTO.class);
    }

    @Override
    public UserDTO login(String token) throws Exception {

        Optional<User> optionalUser = repository.findByToken(token);
        
        if (!optionalUser.isPresent()) {
            throw new CustomException("User not found");
        }

        User user = optionalUser.get();

        user.setLastLogin(new Date());
        user.setToken(generateNewToken(user.getId()));
        repository.save(user);

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(user, UserDTO.class);
    }


    private String generateNewToken(UUID id) {
        return jwtTokenUtil.getToken(id);
    }
    
    private String encript(String value) {  // TODO: encriptar
        return value;
    }
}
