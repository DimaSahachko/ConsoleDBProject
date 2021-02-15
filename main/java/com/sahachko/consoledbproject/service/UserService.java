package com.sahachko.consoledbproject.service;

import java.util.List;

import com.sahachko.consoledbproject.model.User;
import com.sahachko.consoledbproject.repository.UserRepository;

public class UserService {
	private UserRepository repository;

	public UserService(UserRepository repository) {
		this.repository = repository;
	}
	
	public User saveUser(User user) {
		return repository.save(user);
	}
	
	public User getUserById(long id) {
		return repository.getById(id);
	}
	
	public User updateUser(User user) {
		return repository.update(user);
	}
	
	public List<User> getAllUsers() {
		return repository.getAll();
	}
	
	public void deleteUserById(long id) {
		repository.deleteById(id);
	}
}
