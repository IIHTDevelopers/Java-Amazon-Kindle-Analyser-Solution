package com.amazonkindleanalyserapplication.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.amazonkindleanalyserapplication.model.Book;
import com.amazonkindleanalyserapplication.model.Shelf;

public class ShelfDAOImpl implements ShelfDAO {
	private Connection connection;

	public ShelfDAOImpl(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void create(Shelf shelf) {
		String query = "INSERT INTO shelves (user_id, description, is_purchased, book_id) VALUES (?, ?, ?, ?)";

		try (PreparedStatement statement = connection.prepareStatement(query,
				PreparedStatement.RETURN_GENERATED_KEYS)) {
			statement.setInt(1, shelf.getUserId());
			statement.setString(2, shelf.getDescription());
			statement.setBoolean(3, shelf.isPurchased());
			statement.setInt(4, shelf.getBookId());

			statement.executeUpdate();

			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					int generatedId = generatedKeys.getInt(1);
					shelf.setId(generatedId);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Shelf shelf) {
		String query = "UPDATE shelves SET user_id = ?, description = ?, is_purchased = ? WHERE id = ?";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, shelf.getUserId());
			statement.setString(2, shelf.getDescription());
			statement.setBoolean(3, shelf.isPurchased());
			statement.setInt(4, shelf.getId());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Shelf getById(int id) {
		String query = "SELECT * FROM shelves WHERE id = ?";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, id);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return extractShelfFromResultSet(resultSet);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<Book> getSuggestionsByGenre(int userId) {
		String query = "SELECT * FROM books WHERE genre = (" + "SELECT favourite_genre FROM users WHERE id = ?" + ")";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, userId);

			try (ResultSet resultSet = statement.executeQuery()) {
				List<Book> books = new ArrayList<>();
				while (resultSet.next()) {
					Book book = extractBookFromResultSet(resultSet);
					books.add(book);
				}
				return books;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return new ArrayList<>();
	}

	@Override
	public List<Book> getSuggestionsByRating(int rating) {
		String query = "SELECT b.* FROM books b " + "JOIN user_ratings ur ON b.id = ur.book_id "
				+ "WHERE ur.rating >= ?";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, rating);

			try (ResultSet resultSet = statement.executeQuery()) {
				List<Book> books = new ArrayList<>();
				while (resultSet.next()) {
					Book book = extractBookFromResultSet(resultSet);
					books.add(book);
				}
				return books;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return new ArrayList<>();
	}

	@Override
	public List<Book> getMostReadBooksByAge(int age) {
		String query = "SELECT b.* FROM books b " + "INNER JOIN user_ratings ur ON b.id = ur.book_id "
				+ "INNER JOIN users u ON ur.user_id = u.id " + "WHERE u.age = ? " + "GROUP BY b.id "
				+ "ORDER BY COUNT(ur.id) DESC " + "LIMIT 10";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, age);

			try (ResultSet resultSet = statement.executeQuery()) {
				List<Book> books = new ArrayList<>();
				while (resultSet.next()) {
					Book book = extractBookFromResultSet(resultSet);
					books.add(book);
				}
				return books;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return new ArrayList<>();
	}

	private String getUserFavouriteGenre(int userId) {
		String query = "SELECT favourite_genre FROM users WHERE id = ?";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, userId);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getString("favourite_genre");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "";
	}

	private Shelf extractShelfFromResultSet(ResultSet resultSet) throws SQLException {
		Shelf shelf = new Shelf();
		shelf.setId(resultSet.getInt("id"));
		shelf.setUserId(resultSet.getInt("user_id"));
		shelf.setDescription(resultSet.getString("description"));
		shelf.setPurchased(resultSet.getBoolean("is_purchased"));
		return shelf;
	}

	private Book extractBookFromResultSet(ResultSet resultSet) throws SQLException {
		Book book = new Book();
		book.setId(resultSet.getInt("id"));
		book.setName(resultSet.getString("name"));
		book.setWriter(resultSet.getString("writer"));
		book.setPublisher(resultSet.getString("publisher"));
		book.setGenre(resultSet.getString("genre"));
		book.setReleaseYear(resultSet.getInt("release_year"));
		book.setSampleAvailable(resultSet.getBoolean("sample_available"));
		return book;
	}

	public int getPurchasedBookCount() {
		String query = "SELECT COUNT(DISTINCT book_id) FROM shelves WHERE is_purchased = true";

		try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	public void deleteAllShelves() {
		String query = "DELETE FROM shelves";

		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Shelf> getAllShelves() {
		List<Shelf> shelves = new ArrayList<>();

		String query = "SELECT * FROM shelves";

		try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {

			while (resultSet.next()) {
				Shelf shelf = new Shelf();
				shelf.setId(resultSet.getInt("id"));
				shelf.setUserId(resultSet.getInt("user_id"));
				shelf.setBookId(resultSet.getInt("book_id"));
				shelf.setDescription(resultSet.getString("description"));
				shelf.setPurchased(resultSet.getBoolean("is_purchased"));

				shelves.add(shelf);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return shelves;
	}
}
