package com.sahachko.consoledbproject.service;

import java.util.List;

import com.sahachko.consoledbproject.model.Post;
import com.sahachko.consoledbproject.repository.PostRepository;

public class PostService {
	private PostRepository repository;
	
	public PostService(PostRepository repository) {
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
