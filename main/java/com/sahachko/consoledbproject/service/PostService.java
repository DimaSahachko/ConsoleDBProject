package com.sahachko.consoledbproject.service;

import java.util.List;
import com.sahachko.consoledbproject.model.Post;

public interface PostService {
	
	Post savePost(Post post);
	
	Post updatePost(Post post);
	
	Post getPostById(long id);
	
	List<Post> getAllPosts();
	
	void deletePostById(long id);
}
