package com.ar.users.service;

import com.ar.users.dto.SignUpRequestDTO;
import com.ar.users.dto.SignUpResponseDTO;
import com.ar.users.dto.UserDTO;

public interface IUserService {

    SignUpResponseDTO signUp(SignUpRequestDTO request) throws Exception;

    UserDTO login(String token) throws Exception;
}
