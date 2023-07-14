package com.amazonkindleanalyserapplication.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.amazonkindleanalyserapplication.model.UserRating;

public class UserRatingDAOImpl implements UserRatingDAO {
	private Connection connection;

	public UserRatingDAOImpl(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void create(UserRating userRating) {
		String query = "INSERT INTO user_ratings (user_id, book_id, rating) VALUES (?, ?, ?)";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, userRating.getUserId());
			statement.setInt(2, userRating.getBookId());
			statement.setInt(3, userRating.getRating());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteAllUserRatings() {
		String query = "DELETE FROM user_ratings";

		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public UserRating getById(int id) {
		String query = "SELECT * FROM user_ratings WHERE id = ?";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, id);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return extractUserRatingFromResultSet(resultSet);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void update(UserRating userRating) {
		String query = "UPDATE user_ratings SET user_id = ?, book_id = ?, rating = ? WHERE id = ?";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, userRating.getUserId());
			statement.setInt(2, userRating.getBookId());
			statement.setInt(3, userRating.getRating());
			statement.setInt(4, userRating.getId());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(UserRating userRating) {
		String query = "DELETE FROM user_ratings WHERE id = ?";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, userRating.getId());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private UserRating extractUserRatingFromResultSet(ResultSet resultSet) throws SQLException {
		UserRating userRating = new UserRating();
		userRating.setId(resultSet.getInt("id"));
		userRating.setUserId(resultSet.getInt("user_id"));
		userRating.setBookId(resultSet.getInt("book_id"));
		userRating.setRating(resultSet.getInt("rating"));
		return userRating;
	}

	@Override
	public List<UserRating> getAllUserRatings() {
		String query = "SELECT * FROM user_ratings";

		List<UserRating> userRatings = new ArrayList<>();

		try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {

			while (resultSet.next()) {
				UserRating userRating = extractUserRatingFromResultSet(resultSet);
				userRatings.add(userRating);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return userRatings;
	}
}
