package com.amazonkindleanalyserapplication.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.amazonkindleanalyserapplication.model.User;

public class UserDAOImpl implements UserDAO {
	private Connection connection;

	public UserDAOImpl(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void create(User user) {
		String query = "INSERT INTO users (name, age, gender, favourite_genre, favourite_writer) "
				+ "VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement statement = connection.prepareStatement(query,
				PreparedStatement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, user.getName());
			statement.setInt(2, user.getAge());
			statement.setString(3, user.getGender());
			statement.setString(4, user.getFavouriteGenre());
			statement.setString(5, user.getFavouriteWriter());

			statement.executeUpdate();

			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					int generatedId = generatedKeys.getInt(1);
					user.setId(generatedId);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(User user) {
		String query = "UPDATE users SET name = ?, age = ?, gender = ?, favourite_genre = ?, favourite_writer = ? "
				+ "WHERE id = ?";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, user.getName());
			statement.setInt(2, user.getAge());
			statement.setString(3, user.getGender());
			statement.setString(4, user.getFavouriteGenre());
			statement.setString(5, user.getFavouriteWriter());
			statement.setInt(6, user.getId());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public User getById(int id) {
		String query = "SELECT * FROM users WHERE id = ?";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, id);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return extractUserFromResultSet(resultSet);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	private User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
		User user = new User();
		user.setId(resultSet.getInt("id"));
		user.setName(resultSet.getString("name"));
		user.setAge(resultSet.getInt("age"));
		user.setGender(resultSet.getString("gender"));
		user.setFavouriteGenre(resultSet.getString("favourite_genre"));
		user.setFavouriteWriter(resultSet.getString("favourite_writer"));
		return user;
	}

	@Override
	public void deleteAllUsers() {
		String query = "DELETE FROM users";

		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<User> getAllUsers() {
		String query = "SELECT * FROM users";

		List<User> users = new ArrayList<>();

		try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {

			while (resultSet.next()) {
				User user = extractUserFromResultSet(resultSet);
				users.add(user);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return users;
	}

}
