package com.sahachko.consoledbproject.repository.jdbc;

import java.sql.*;
import java.util.Date;
import java.util.*;

import com.sahachko.consoledbproject.model.Post;
import com.sahachko.consoledbproject.repository.ConnectionUtils;
import com.sahachko.consoledbproject.repository.PostRepository;

public class JavaIOPostRepository implements PostRepository {

	@Override
	public List<Post> getAll() {
		List<Post> allPosts = new ArrayList<>();
		try(Statement statement = ConnectionUtils.getStatement()) {
			ResultSet allPostsSet = statement.executeQuery("SELECT * FROM posts;");
			while(allPostsSet.next()) {
				long id = allPostsSet.getLong("id");
				String content = allPostsSet.getString("content");
				Timestamp createdTimeStamp = allPostsSet.getTimestamp("created");
				Timestamp updatedTimeStamp = allPostsSet.getTimestamp("updated");
				Date created = new Date(createdTimeStamp.getTime());
				Date updated = null;
				if(updatedTimeStamp != null) {
					updated = new Date(updatedTimeStamp.getTime());
				}
				Post post = new Post(content);
				post.setId(id);
				post.setCreated(created);
				post.setUpdated(updated);
				allPosts.add(post);
			}
		} catch(SQLException exc) {
			exc.printStackTrace();
		}
		return allPosts;
	}

	@Override
	public Post getById(Long id) {
		Post postById = null;
		try(PreparedStatement prepStatement = ConnectionUtils.getPreparedStatement("SELECT * FROM posts WHERE id = ?;")) {
			prepStatement.setLong(1, id);
			ResultSet postByIdSet = prepStatement.executeQuery();
			if(postByIdSet.next()) {
				String content = postByIdSet.getString("content");
				Timestamp createdTimeStamp = postByIdSet.getTimestamp("created");
				Timestamp updatedTimeStamp = postByIdSet.getTimestamp("updated");
				Date created = new Date(createdTimeStamp.getTime());
				Date updated = null;
				if(updatedTimeStamp != null) {
					updated = new Date(updatedTimeStamp.getTime());
				}
				postById = new Post(content);
				postById.setId(id);
				postById.setContent(content);
				postById.setCreated(created);
				postById.setUpdated(updated);
			}
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
		return postById;
	}

	@Override
	public Post save(Post post) {
		long nextEmptyId = 0;
		try(Statement statement = ConnectionUtils.getStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
			ResultSet postsId = statement.executeQuery("SELECT id FROM posts;");
			if(postsId.last()) {
				nextEmptyId = postsId.getLong(1) + 1;
			} else {
				nextEmptyId = 1;
			}
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
		post.setId(nextEmptyId);
		post.setCreated(new Date());
		try(PreparedStatement prepStatement = ConnectionUtils.getPreparedStatement("INSERT INTO posts(id, content, created) VALUES(?, ?, ?);")) {
			prepStatement.setLong(1, post.getId());
			prepStatement.setString(2, post.getContent());
			prepStatement.setTimestamp(3, new Timestamp(post.getCreated().getTime()));
			prepStatement.executeUpdate();
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
		return post;
	}

	@Override
	public Post update(Post post) {
		try(PreparedStatement prepStatement = ConnectionUtils.getPreparedStatement("SELECT * FROM posts WHERE id = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
			prepStatement.setLong(1, post.getId());
			ResultSet updatePostSet = prepStatement.executeQuery();
			if(updatePostSet.next()) {
				updatePostSet.updateString("content", post.getContent());
				Date updated = new Date();
				post.setUpdated(updated);
				updatePostSet.updateTimestamp("updated", new Timestamp(updated.getTime()));
				updatePostSet.updateRow();
			} else {
				System.out.println("There is no post with such id");
			}
		} catch(SQLException exc) {
			exc.printStackTrace();
		}
		return post;
	}

	@Override
	public void deleteById(Long id) {
		try(PreparedStatement prepStatement = ConnectionUtils.getPreparedStatement("SELECT * FROM posts WHERE id = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
			prepStatement.setLong(1, id);
			ResultSet deletePostSet = prepStatement.executeQuery();
			if(deletePostSet.next()) {
				deletePostSet.deleteRow();
			} else {
				System.out.println("There is no post with such id");
			}
		} catch(SQLException exc) {
			exc.printStackTrace();
		}
	}

}
