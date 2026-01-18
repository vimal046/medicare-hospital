package com.hospital.medicare.service;

import java.util.List;

import com.hospital.medicare.dto.UserRegistrationDTO;
import com.hospital.medicare.entity.User;

public interface UserService {

	User registerUser(UserRegistrationDTO registrationDTO);
	User findByUsername(String username);
	User findByEmail(String email);
	User findById(Long id);
	List<User> getAllUsers();
	void deleteUser(Long id);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
}
