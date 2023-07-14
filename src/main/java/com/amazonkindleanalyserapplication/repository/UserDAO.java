package com.amazonkindleanalyserapplication.repository;

import java.util.List;

import com.amazonkindleanalyserapplication.model.User;

public interface UserDAO {
	void create(User user);

	void update(User user);

	User getById(int id);

	void deleteAllUsers();

	List<User> getAllUsers();
}
