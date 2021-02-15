package com.sahachko.consoledbproject.model;

import java.util.List;

public class User {
	private Long id;
	private String firstName;
	private String lastName;
	private List<Post> posts;
	private	Region region;
	private Role role;
	
	public User(String firstName, String lastName, Region region, Role role) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.region = region;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return id + "." + firstName + " " + lastName + ". Region " + region + ". Role " + role + "\n Posts:" + posts;
	}
}
