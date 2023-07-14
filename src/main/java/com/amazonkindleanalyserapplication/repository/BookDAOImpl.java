package com.amazonkindleanalyserapplication.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.amazonkindleanalyserapplication.model.Book;

public class BookDAOImpl implements BookDAO {
	private Connection connection;

	public BookDAOImpl(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void create(Book book) {
		String query = "INSERT INTO books (name, writer, publisher, genre, release_year, sample_available) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";

		try (PreparedStatement statement = connection.prepareStatement(query,
				PreparedStatement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, book.getName());
			statement.setString(2, book.getWriter());
			statement.setString(3, book.getPublisher());
			statement.setString(4, book.getGenre());
			statement.setInt(5, book.getReleaseYear());
			statement.setBoolean(6, book.isSampleAvailable());

			statement.executeUpdate();

			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					int generatedId = generatedKeys.getInt(1);
					book.setId(generatedId);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Book book) {
		String query = "UPDATE books SET name = ?, writer = ?, publisher = ?, genre = ?, release_year = ?, "
				+ "sample_available = ? WHERE id = ?";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, book.getName());
			statement.setString(2, book.getWriter());
			statement.setString(3, book.getPublisher());
			statement.setString(4, book.getGenre());
			statement.setInt(5, book.getReleaseYear());
			statement.setBoolean(6, book.isSampleAvailable());
			statement.setInt(7, book.getId());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Book getById(int id) {
		String query = "SELECT * FROM books WHERE id = ?";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, id);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return extractBookFromResultSet(resultSet);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<Book> search(String keyword) {
		String query = "SELECT * FROM books WHERE name LIKE ? OR writer LIKE ? OR genre LIKE ?";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			String likeKeyword = "%" + keyword + "%";
			statement.setString(1, likeKeyword);
			statement.setString(2, likeKeyword);
			statement.setString(3, likeKeyword);

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
	public List<Book> getTrendingBooks() {
		String query = "SELECT * FROM books ORDER BY release_year DESC LIMIT 10";

		try (PreparedStatement statement = connection.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery()) {
			List<Book> books = new ArrayList<>();
			while (resultSet.next()) {
				Book book = extractBookFromResultSet(resultSet);
				books.add(book);
			}
			return books;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return new ArrayList<>();
	}

	@Override
	public List<Book> getPurchasedBooks() {
		String query = "SELECT b.* FROM books b " + "JOIN shelves s ON b.id = s.book_id "
				+ "WHERE s.is_purchased = true";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			try (ResultSet resultSet = statement.executeQuery()) {
				List<Book> purchasedBooks = new ArrayList<>();
				while (resultSet.next()) {
					Book book = extractBookFromResultSet(resultSet);
					purchasedBooks.add(book);
				}
				return purchasedBooks;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return new ArrayList<>();
	}

	@Override
	public float getPurchasePercentage() {
		String query = "SELECT COUNT(*) FROM shelves WHERE is_purchased = 1";
		int purchasedCount = 0;
		int totalCount = 0;

		try (PreparedStatement statement = connection.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery()) {
			if (resultSet.next()) {
				purchasedCount = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		query = "SELECT COUNT(*) FROM shelves";
		try (PreparedStatement statement = connection.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery()) {
			if (resultSet.next()) {
				totalCount = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (totalCount > 0) {
			return (float) purchasedCount / totalCount * 100;
		}
		return 0;
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

	public int getUniqueBookCount() {
		String query = "SELECT COUNT(DISTINCT id) FROM books";

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
	public void deleteAllBooks() {
		String query = "DELETE FROM books";

		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Book> getAllBooks() {
		String query = "SELECT * FROM books";

		List<Book> books = new ArrayList<>();

		try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {

			while (resultSet.next()) {
				Book book = extractBookFromResultSet(resultSet);
				books.add(book);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return books;
	}

}
