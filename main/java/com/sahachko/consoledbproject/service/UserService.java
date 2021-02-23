package com.sahachko.consoledbproject.service;

import java.util.List;
import com.sahachko.consoledbproject.model.User;

public interface UserService {
	
	User saveUser(User user);
	
	User getUserById(long id);
	
	User updateUser(User user);
	
	List<User> getAllUsers();
	
	void deleteUserById(long id);
}
