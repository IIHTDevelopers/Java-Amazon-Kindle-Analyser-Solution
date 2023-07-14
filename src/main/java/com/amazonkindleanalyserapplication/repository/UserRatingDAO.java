package com.amazonkindleanalyserapplication.repository;

import java.util.List;

import com.amazonkindleanalyserapplication.model.UserRating;

public interface UserRatingDAO {
	void create(UserRating userRating);

	void deleteAllUserRatings();

	void delete(UserRating userRating);

	void update(UserRating userRating);

	UserRating getById(int id);

	List<UserRating> getAllUserRatings();
}
