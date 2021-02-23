package com.sahachko.consoledbproject.repository.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sahachko.consoledbproject.model.*;
import com.sahachko.consoledbproject.repository.ConnectionUtils;
import com.sahachko.consoledbproject.repository.PostRepository;
import com.sahachko.consoledbproject.repository.RegionRepository;
import com.sahachko.consoledbproject.repository.UserRepository;

public class JavaIOUserRepository implements UserRepository {
	private RegionRepository regionRepository;
	private PostRepository postRepository;
	
	public JavaIOUserRepository(RegionRepository regionRepository, PostRepository postRepository) {
		this.regionRepository = regionRepository;
		this.postRepository = postRepository;
	}

	@Override
	public List<User> getAll() {
		List<User> allUsers = new ArrayList<>();
		try(Statement statement = ConnectionUtils.getStatement()) {
			String query = "SELECT id FROM users;";
			ResultSet usersIdSet = statement.executeQuery(query);
			while(usersIdSet.next()) {
				long userId = usersIdSet.getLong("id");
				User user = getById(userId);
				allUsers.add(user);
			}
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
		return allUsers;
	}

	@Override
	public User getById(Long id) {
		User userById = null;
		try(PreparedStatement prepStatement = ConnectionUtils.getPreparedStatement("SELECT * FROM users WHERE id = ?;");
				PreparedStatement prepStatementForUserPosts = ConnectionUtils.getPreparedStatement("SELECT post_id FROM posts_by_users WHERE user_id = ?;")) {
			prepStatement.setLong(1, id);
			ResultSet userByIdSet = prepStatement.executeQuery();
			if(userByIdSet.next()) {
				String firstName = userByIdSet.getString("firstName");
				String lastName = userByIdSet.getString("lastName");
				long regionId = userByIdSet.getLong("region");
				Region region = regionRepository.getById(regionId);
				Role role = Role.valueOf(userByIdSet.getString("role"));
				userById = new User(firstName, lastName, region, role);
				userById.setId(id);
			} else {
				return userById;
			}
			List<Post> posts = new ArrayList<>();
			prepStatementForUserPosts.setLong(1, id);
			ResultSet postsByUserIdSet = prepStatementForUserPosts.executeQuery();
			while(postsByUserIdSet.next()) {
				long postId = postsByUserIdSet.getLong("post_id");
				Post post = postRepository.getById(postId);
				posts.add(post);
			}
			userById.setPosts(posts);
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
		return userById;
	}

	@Override
	public User save(User user) {
		long nextEmptyId = 0;
		try(Statement statement = ConnectionUtils.getStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
			ResultSet usersIdSet = statement.executeQuery("SELECT id FROM users;");
			if(usersIdSet.last()) {
				nextEmptyId = usersIdSet.getLong(1) + 1;
			} else {
				nextEmptyId = 1;
			}
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
		user.setId(nextEmptyId);
		try(PreparedStatement prepStatement = ConnectionUtils.getPreparedStatement("INSERT INTO users(id, firstName, lastName, region, role) VALUES(?, ?, ?, ?, ?);");
			PreparedStatement prepStatementForUserPosts = ConnectionUtils.getPreparedStatement("INSERT INTO posts_by_users VALUES(?, ?)")) {
			prepStatement.setLong(1, user.getId());
			prepStatement.setString(2, user.getFirstName());
			prepStatement.setString(3, user.getLastName());
			prepStatement.setLong(4,  user.getRegion().getId());
			prepStatement.setString(5,  user.getRole().name());
			prepStatement.executeUpdate();
			
			List<Post> userPosts = user.getPosts();
			if(userPosts != null) {
				for(Post post : userPosts) {
					prepStatementForUserPosts.setLong(1, user.getId());
					prepStatementForUserPosts.setLong(2, post.getId());
					prepStatementForUserPosts.executeUpdate();
				}
			}
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
		return user;
	}

	@Override
	public User update(User user) {
		try(PreparedStatement prepStatement = ConnectionUtils.getPreparedStatement("SELECT * FROM users WHERE id = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			PreparedStatement prepStatementDeletePosts = ConnectionUtils.getPreparedStatement("DELETE FROM posts_by_users WHERE user_id = ?;");
			PreparedStatement prepStatementForAddingUserPosts = ConnectionUtils.getPreparedStatement("INSERT INTO posts_by_users VALUES(?, ?);")) {
			prepStatement.setLong(1, user.getId());
			ResultSet updateUserSet = prepStatement.executeQuery();
			if(updateUserSet.next()) {
				updateUserSet.updateString("firstName", user.getFirstName());
				updateUserSet.updateString("lastName", user.getLastName());
				updateUserSet.updateLong("region", user.getRegion().getId());
				updateUserSet.updateString("role", user.getRole().name());
				updateUserSet.updateRow();
				prepStatementDeletePosts.setLong(1, user.getId());
				prepStatementDeletePosts.executeUpdate();
				List<Post> allPosts = user.getPosts();
				for(Post post : allPosts) {
					prepStatementForAddingUserPosts.setLong(1, user.getId());
					prepStatementForAddingUserPosts.setLong(2, post.getId());
					prepStatementForAddingUserPosts.executeUpdate();
				}
			} else {
				System.out.println("There is no user with such id");
			}
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
		return user;
	}

	@Override
	public void deleteById(Long id) {
		try(PreparedStatement prepStatement = ConnectionUtils.getPreparedStatement("DELETE FROM users WHERE id = ?;");
			PreparedStatement prepStatementForDeletePosts = ConnectionUtils.getPreparedStatement("DELETE FROM posts_by_users WHERE user_id = ?;")) {
			prepStatement.setLong(1, id);
			int result = prepStatement.executeUpdate();
			if(result != 0) {
				prepStatementForDeletePosts.setLong(1, id);
				prepStatementForDeletePosts.executeUpdate();
			} else {
				System.out.println("There is no user with such id");
			}
		} catch(SQLException exc) {
			exc.printStackTrace();
		}
	}
}
