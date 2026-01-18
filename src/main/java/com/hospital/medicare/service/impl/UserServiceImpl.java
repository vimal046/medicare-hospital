package com.hospital.medicare.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hospital.medicare.dto.UserRegistrationDTO;
import com.hospital.medicare.entity.Role;
import com.hospital.medicare.entity.User;
import com.hospital.medicare.exception.DuplicateResourceException;
import com.hospital.medicare.exception.ResourceNotFoundException;
import com.hospital.medicare.repository.RoleRepository;
import com.hospital.medicare.repository.UserRepository;
import com.hospital.medicare.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public User registerUser(UserRegistrationDTO registrationDTO) {

		if (userRepository.existsByUsername(registrationDTO.getUsername())) {
			throw new DuplicateResourceException("Username already exists");
		}

		if (userRepository.existsByemail(registrationDTO.getEmail())) {
			throw new DuplicateResourceException("Email already exists");
		}

		User user = User.builder()
				.username(registrationDTO.getUsername())
				.email(registrationDTO.getEmail())
				.password(passwordEncoder.encode(registrationDTO.getPassword()))
				.fullName(registrationDTO.getFullName())
				.phone(registrationDTO.getPhone())
				.enabled(true)
				.build();

		Set<Role> roles = new HashSet<>();
		Role userRole = roleRepository.findByName(registrationDTO.getRole())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Role not found: " + registrationDTO.getRole()));
		roles.add(userRole);
		user.setRoles(roles);

		return userRepository.save(user);
	}

	@Override
	@Transactional(readOnly = true)
	public User findByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException(
						"User not found with the username: " + username));
	}

	@Override
	@Transactional(readOnly = true)
	public User findByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(
						() -> new ResourceNotFoundException("User not found with email: " + email));
	}

	@Override
	@Transactional(readOnly = true)
	public User findById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	@Transactional
	public void deleteUser(Long id) {
		if (!userRepository.existsById(id)) {
			throw new ResourceNotFoundException("User not found with id: " + id);
		}
		userRepository.deleteById(id);
	}

	@Override
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByemail(email);
	}

}
