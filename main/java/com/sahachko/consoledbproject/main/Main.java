package com.sahachko.consoledbproject.main;

import com.sahachko.consoledbproject.controller.*;
import com.sahachko.consoledbproject.repository.*;
import com.sahachko.consoledbproject.repository.jdbc.*;
import com.sahachko.consoledbproject.service.*;
import com.sahachko.consoledbproject.service.implementations.*;
import com.sahachko.consoledbproject.view.*;

public class Main {

	public static void main(String[] args) {
		RegionRepository regRepo = new JavaIORegionRepository();
		PostRepository postRepo = new JavaIOPostRepository();
		UserRepository userRepo = new JavaIOUserRepository(regRepo, postRepo);
		RegionService regionService = new RegionServiceImplementation(regRepo);
		PostService postService = new PostServiceImplementation(postRepo);
		UserService userService = new UserServiceImplementation(userRepo);
		RegionController regionController = new RegionController(regionService);
		PostController postController = new PostController(postService);
		UserController userController = new UserController(userService, regionService, postService);
		RegionView regionView = new RegionView(regionController);
		PostView postView = new PostView(postController);
		UserView userView = new UserView(userController);
		MainView mainView = new MainView(regionView, postView, userView);
		mainView.startConsole();
	}
}

		
		