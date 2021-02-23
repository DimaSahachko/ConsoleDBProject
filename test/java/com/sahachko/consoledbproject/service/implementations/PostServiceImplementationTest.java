package com.sahachko.consoledbproject.service.implementations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.gt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sahachko.consoledbproject.model.Post;
import com.sahachko.consoledbproject.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
class PostServiceImplementationTest {
	
	@InjectMocks
	PostServiceImplementation postService;
	
	@Mock
	PostRepository postRepository;
	
	@Test
	public void testSavePost_shouldCallRepositorySaveMethod() {
		Post post = new Post("Post under test");
		postService.savePost(post);
		verify(postRepository).save(post);
	}
	
	@Test
	public void testSavePost_shouldReturnTheSameObject() {
		Post post = new Post("Post under test");
		when(postRepository.save(post)).thenReturn(post);
		assertEquals(post, postService.savePost(post));
	}
	
	@Test
	@Disabled
	public void testSavePost_shouldFail() {
		when(postRepository.save(null)).thenThrow(NullPointerException.class);
		assertEquals(null, postService.savePost(null));
	}
	
	@Test
	public void testUpdatePost_shouldCallRepositoryUpdateMethod() {
		Post post = new Post("Post under test");
		postService.updatePost(post);
		verify(postRepository).update(post);
	}
	
	@Test
	public void testUpdatePost_shouldReturnTheSameObject() {
		Post post = new Post("Post under test");
		when(postRepository.update(post)).thenReturn(post);
		assertEquals(post, postService.updatePost(post));
	}
	
	@Test
	@Disabled
	public void testUpdatePost_shouldFail() {
		when(postRepository.update(null)).thenThrow(NullPointerException.class);
		assertEquals(null, postService.updatePost(null));
	}
	
	@Test
	public void testGetAllPosts_shouldCallRepositoryGetAllMethod() {
		postService.getAllPosts();
		verify(postRepository).getAll();
	}
	
	@Test
	public void testGetAllPosts_shouldReturnListOfAllPosts() {
		List<Post> allPosts = Arrays.asList(new Post("First post"), new Post("Second post"), new Post("Third post"));
		when(postRepository.getAll()).thenReturn(allPosts);
		assertEquals(allPosts, postService.getAllPosts());
	}
	
	@Test
	@Disabled
	public void testGetAllPosts_shouldFail() {
		List<Post> allPosts = Arrays.asList(new Post("First post"), new Post("Second post"), new Post("Third post"));
		assertEquals(allPosts, postService.getAllPosts());
	}
	
	@Test
	public void testGetPostById_shouldCallRepositoryGetByIdMethod() {
		postService.getPostById(1l);
		verify(postRepository).getById(1l);
	}
	
	@Test
	public void testGetPostById_shouldReturnPostById() {
		Post firstPost = new Post("First post");
		Post secondPost = new Post("Second post");
		Map<Integer, Post> posts = new HashMap<>();
		posts.put(1, firstPost);
		posts.put(2, secondPost);
		when(postRepository.getById(1l)).thenReturn(posts.get(1));
		assertEquals(firstPost, postService.getPostById(1l));
	}
	
	@Test
	@Disabled
	public void testGetPostById_shouldFail() {
		Post firstPost = new Post("First post");
		Post secondPost = new Post("Second post");
		Map<Integer, Post> posts = new HashMap<>();
		posts.put(1, firstPost);
		posts.put(2, secondPost);
		when(postRepository.getById(gt(2l))).thenReturn(null);
		assertThrows(NullPointerException.class, () -> postService.getPostById(3));
	}
	
	@Test
	public void testDeletePostById_shouldCallRepositoryDeleteByIdMethod() {
		postService.deletePostById(3l);
		verify(postRepository).deleteById(3l);
	}
	
	@Test
	@Disabled
	public void testDeletePostById_shouldFail() {
		postService.deletePostById(1l);
		verify(postRepository, never()).deleteById(1l);
	}
}
