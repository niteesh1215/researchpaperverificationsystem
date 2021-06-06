package com.kristujayanticollege.researchpaperverificationsystem.controller;

import java.util.HashMap;

import com.kristujayanticollege.researchpaperverificationsystem.model.User;
import com.kristujayanticollege.researchpaperverificationsystem.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    
    @Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;


    @PostMapping(path = "/add-new-user")
	public @ResponseBody HashMap<String, String> addNewUser(@RequestBody User user) {
		HashMap<String, String> map = new HashMap<>();
		try {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userRepository.save(user);
			map.put("status", "success");
			map.put("message", "Successfuly added");
			return map;
		} catch (Exception e) {
			map.clear();
			map.put("status", "error");
			if (e.toString().contains("UNIQUE")) {
				map.put("message", "username exists");
				return map;
			} else {
				map.put("message", "An unknown error occurred");
				return map;
			}
		}
	}

	
}
