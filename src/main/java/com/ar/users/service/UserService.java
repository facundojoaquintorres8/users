package com.ar.users.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${iv.secret.key}")
    private String ivSecretKey;

    @Value("${secret.key}")
    private String secretKey;

    @Override
    public SignUpResponseDTO signUp(SignUpRequestDTO request) throws Exception {

        validateEmail(request.getEmail());
        validatePassword(request.getPassword());

        Optional<User> optionalUser = repository.findByEmail(request.getEmail());

        if (optionalUser.isPresent()) {
            throw new CustomException("User already exists");
        }

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setCreated(new Date());
        user.setToken(generateNewToken(user.getId()));
        user.setIsActive(true);
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(encript(request.getPassword()));
        if (request.getPhones() != null) {
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
                    });
            user.setPhones(phones);
        }
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

    private void validateEmail(String email) throws Exception {
        if (email == null || email.trim().equals("")) {
            throw new CustomException("Email not entered");
        } else {
            Boolean isValid = email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[a-zA-Z]{2,4}$");
            if (!isValid) {
                throw new CustomException("Invalid email format");
            }
        }
    }

    private void validatePassword(String password) throws Exception {
        if (password == null || password.trim().equals("")) {
            throw new CustomException("Password not entered");
        } else {
            Boolean hasOneCapitalLetter = password.matches("^[a-z0-9]*[A-Z]{1}[a-z0-9]*$");
            Boolean hasTwoNumbers = password.matches("^[a-zA-Z]*[0-9]{1}[a-zA-Z]*[0-9]{1}[a-zA-Z]*$");
            Boolean hasOnlyLettersAndNumbers = password.matches("^[a-zA-Z0-9]*$");
            Boolean validLength = password.matches("^[a-zA-Z0-9]{8,12}$");
            if (!hasOneCapitalLetter || !hasTwoNumbers || !hasOnlyLettersAndNumbers || !validLength) {
                throw new CustomException(
                        "The password must have a capital letter, two numbers and a combination with lowercase letters. With a length between 8 and 12 characters");
            }
        }
    }

    private String generateNewToken(UUID id) {
        return jwtTokenUtil.getToken(id);
    }

    private byte[] encript(String pwd) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(ivSecretKey.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        return cipher.doFinal(pwd.getBytes());
    }
}
