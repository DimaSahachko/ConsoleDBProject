package com.sahachko.consoledbproject.service.implementations;

import java.util.List;

import com.sahachko.consoledbproject.model.Post;
import com.sahachko.consoledbproject.repository.PostRepository;
import com.sahachko.consoledbproject.service.PostService;

public class PostServiceImplementation implements PostService {
	private PostRepository repository;
	
	public PostServiceImplementation(PostRepository repository) {
		this.repository = repository;
	}
	
	public Post savePost(Post post) {
		return repository.save(post);
	}
	
	public Post updatePost(Post post) {
		return repository.update(post);
	}
	
	public Post getPostById(long id) {
		return repository.getById(id);
	}
	
	public List<Post> getAllPosts() {
		return repository.getAll();
	}
	
	public void deletePostById(long id) {
		repository.deleteById(id);
	}
}
