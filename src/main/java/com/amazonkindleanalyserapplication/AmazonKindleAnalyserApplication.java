package com.amazonkindleanalyserapplication;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import com.amazonkindleanalyserapplication.model.Book;
import com.amazonkindleanalyserapplication.model.Shelf;
import com.amazonkindleanalyserapplication.model.User;
import com.amazonkindleanalyserapplication.model.UserRating;
import com.amazonkindleanalyserapplication.repository.BookDAO;
import com.amazonkindleanalyserapplication.repository.BookDAOImpl;
import com.amazonkindleanalyserapplication.repository.ShelfDAO;
import com.amazonkindleanalyserapplication.repository.ShelfDAOImpl;
import com.amazonkindleanalyserapplication.repository.UserDAO;
import com.amazonkindleanalyserapplication.repository.UserDAOImpl;
import com.amazonkindleanalyserapplication.repository.UserRatingDAO;
import com.amazonkindleanalyserapplication.repository.UserRatingDAOImpl;

public class AmazonKindleAnalyserApplication {
	private static final String DB_PROPERTIES_FILE = "application.properties";
	private static final String DB_URL_KEY = "db.url";
	private static final String DB_USERNAME_KEY = "db.username";
	private static final String DB_PASSWORD_KEY = "db.password";

	private static Connection connection;
	private static BookDAO bookDAO;
	private static UserDAO userDAO;
	private static ShelfDAO shelfDAO;
	private static UserRatingDAO userRatingDAO;

	public static void main(String[] args) {
		try {
			loadDatabaseProperties();
			createDatabaseIfNotExists();
			createTablesIfNotExists();
			initializeDAOs();

			showOptions();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}

	public static void loadDatabaseProperties() throws IOException {
		InputStream inputStream = AmazonKindleAnalyserApplication.class.getClassLoader()
				.getResourceAsStream(DB_PROPERTIES_FILE);
		Properties properties = new Properties();
		properties.load(inputStream);

		String url = properties.getProperty(DB_URL_KEY);
		String username = properties.getProperty(DB_USERNAME_KEY);
		String password = properties.getProperty(DB_PASSWORD_KEY);

		try {
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void createDatabaseIfNotExists() throws SQLException {
		String query = "CREATE DATABASE IF NOT EXISTS kindle_analyser";
		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(query);
		}
	}

	private static void createTablesIfNotExists() throws SQLException {
		String createUserTableQuery = "CREATE TABLE IF NOT EXISTS users (" + "id INT AUTO_INCREMENT PRIMARY KEY,"
				+ "name VARCHAR(255) NOT NULL," + "age INT NOT NULL," + "gender VARCHAR(10) NOT NULL,"
				+ "favourite_genre VARCHAR(255) NOT NULL," + "favourite_writer VARCHAR(255) NOT NULL" + ")";

		String createBookTableQuery = "CREATE TABLE IF NOT EXISTS books (" + "id INT AUTO_INCREMENT PRIMARY KEY,"
				+ "name VARCHAR(255) NOT NULL," + "writer VARCHAR(255) NOT NULL," + "publisher VARCHAR(255) NOT NULL,"
				+ "genre VARCHAR(255) NOT NULL," + "release_year INT NOT NULL," + "sample_available BOOLEAN NOT NULL"
				+ ")";

		String createShelfTableQuery = "CREATE TABLE IF NOT EXISTS shelves (" + "id INT AUTO_INCREMENT PRIMARY KEY,"
				+ "user_id INT NOT NULL," + "book_id INT NOT NULL," + "description VARCHAR(255) NOT NULL,"
				+ "is_purchased BOOLEAN NOT NULL," + "FOREIGN KEY (user_id) REFERENCES users(id),"
				+ "FOREIGN KEY (book_id) REFERENCES books(id)" + ")";

		String createUserRatingTableQuery = "CREATE TABLE IF NOT EXISTS user_ratings ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY," + "user_id INT NOT NULL," + "book_id INT NOT NULL,"
				+ "rating INT NOT NULL," + "FOREIGN KEY (user_id) REFERENCES users(id),"
				+ "FOREIGN KEY (book_id) REFERENCES books(id)" + ")";

		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(createUserTableQuery);
			statement.executeUpdate(createBookTableQuery);
			statement.executeUpdate(createShelfTableQuery);
			statement.executeUpdate(createUserRatingTableQuery);
		}
	}

	private static void initializeDAOs() {
		bookDAO = new BookDAOImpl(connection);
		userDAO = new UserDAOImpl(connection);
		shelfDAO = new ShelfDAOImpl(connection);
		userRatingDAO = new UserRatingDAOImpl(connection);
	}

	public static Connection getConnection() {
		return connection;
	}

	private static void showOptions() {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("----- Reading Preferences Analysis -----");
			System.out.println("1. Create a new user");
			System.out.println("2. Update a user");
			System.out.println("3. Get details of a user");
			System.out.println("4. Create a new book");
			System.out.println("5. Update a book");
			System.out.println("6. Get details of a book");
			System.out.println("7. Create a new shelf");
			System.out.println("8. Update a shelf");
			System.out.println("9. Get details of a shelf");
			System.out.println("10. Create user rating");
			System.out.println("11. Search for a book");
			System.out.println("12. Get books suggestions by genre");
			System.out.println("13. Get insights of most read books by age");
			System.out.println("14. Get book suggestions by rating");
			System.out.println("15. Show trending books");
			System.out.println("16. Get list of purchased books");
			System.out.println("17. Get percentage of books purchased after reading their sample");
			System.out.println("0. Exit");
			System.out.print("Enter your choice: ");
			int choice = scanner.nextInt();
			scanner.nextLine(); // Consume the newline character

			switch (choice) {
			case 1:
				createUser(scanner);
				break;
			case 2:
				updateUser(scanner);
				break;
			case 3:
				getUserDetails(scanner);
				break;
			case 4:
				createBook(scanner);
				break;
			case 5:
				updateBook(scanner);
				break;
			case 6:
				getBookDetails(scanner);
				break;
			case 7:
				createShelf(scanner);
				break;
			case 8:
				updateShelf(scanner);
				break;
			case 9:
				getShelfDetails(scanner);
				break;
			case 10:
				createUserRating(scanner);
				break;
			case 11:
				searchBooks(scanner);
				break;
			case 12:
				getBookSuggestionsByGenre(scanner);
				break;
			case 13:
				getMostReadBooksByAge(scanner);
				break;
			case 14:
				getBookSuggestionsByRating(scanner);
				break;
			case 15:
				showTrendingBooks();
				break;
			case 16:
				showPurchasedBooks();
				break;
			case 17:
				showPurchasePercentage();
				break;
			case 0:
				return;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		}
	}

	private static void createUser(Scanner scanner) {
		// Collect user input
		System.out.println("Enter user details:");
		System.out.print("Name: ");
		String name = scanner.nextLine();
		System.out.print("Age: ");
		int age = scanner.nextInt();
		scanner.nextLine(); // Consume the newline character
		System.out.print("Gender: ");
		String gender = scanner.nextLine();
		System.out.print("Favorite Genre: ");
		String favoriteGenre = scanner.nextLine();
		System.out.print("Favorite Writer: ");
		String favoriteWriter = scanner.nextLine();

		// Create a new User object
		User user = new User();
		user.setName(name);
		user.setAge(age);
		user.setGender(gender);
		user.setFavouriteGenre(favoriteGenre);
		user.setFavouriteWriter(favoriteWriter);

		// Save the user in the database
		userDAO.create(user);

		System.out.println("User created successfully!");
	}

	private static void updateUser(Scanner scanner) {
		// Collect user input
		System.out.print("Enter user ID to update: ");
		int userId = scanner.nextInt();
		scanner.nextLine(); // Consume the newline character

		// Retrieve the user from the database
		User user = userDAO.getById(userId);
		if (user == null) {
			System.out.println("User not found.");
			return;
		}

		// Collect updated user details
		System.out.println("Enter updated user details:");
		System.out.print("Name: ");
		String name = scanner.nextLine();
		System.out.print("Age: ");
		int age = scanner.nextInt();
		scanner.nextLine(); // Consume the newline character
		System.out.print("Gender: ");
		String gender = scanner.nextLine();
		System.out.print("Favorite Genre: ");
		String favoriteGenre = scanner.nextLine();
		System.out.print("Favorite Writer: ");
		String favoriteWriter = scanner.nextLine();

		// Update the user object
		user.setName(name);
		user.setAge(age);
		user.setGender(gender);
		user.setFavouriteGenre(favoriteGenre);
		user.setFavouriteWriter(favoriteWriter);

		// Update the user in the database
		userDAO.update(user);

		System.out.println("User updated successfully!");
	}

	private static void getUserDetails(Scanner scanner) {
		// Collect user input
		System.out.print("Enter user ID to get details: ");
		int userId = scanner.nextInt();
		scanner.nextLine(); // Consume the newline character

		// Retrieve the user from the database
		User user = userDAO.getById(userId);
		if (user == null) {
			System.out.println("User not found.");
			return;
		}

		// Display user details
		System.out.println("User Details:");
		System.out.println("ID: " + user.getId());
		System.out.println("Name: " + user.getName());
		System.out.println("Age: " + user.getAge());
		System.out.println("Gender: " + user.getGender());
		System.out.println("Favorite Genre: " + user.getFavouriteGenre());
		System.out.println("Favorite Writer: " + user.getFavouriteWriter());
	}

	private static void searchBooks(Scanner scanner) {
		// Collect user input
		System.out.print("Enter name or writer or genre keyword to search: ");
		String keyword = scanner.nextLine();

		// Search for books by keyword
		List<Book> books = bookDAO.search(keyword);

		// Display search results
		System.out.println("Search Results:");
		for (Book book : books) {
			System.out.println("ID: " + book.getId());
			System.out.println("Name: " + book.getName());
			System.out.println("Writer: " + book.getWriter());
			System.out.println("Publisher: " + book.getPublisher());
			System.out.println("Genre: " + book.getGenre());
			System.out.println("Release Year: " + book.getReleaseYear());
			System.out.println("Sample Available: " + book.isSampleAvailable());
			System.out.println("------------------------");
		}
	}

	private static void getBookSuggestionsByGenre(Scanner scanner) {
		System.out.print("Enter user ID: ");
		int userId = scanner.nextInt();
		scanner.nextLine(); // Consume the newline character

		List<Book> books = shelfDAO.getSuggestionsByGenre(userId);

		System.out.println("Book Suggestions by Genre:");
		System.out.println(books.size());
		for (Book book : books) {
			System.out.println("ID: " + book.getId());
			System.out.println("Name: " + book.getName());
			System.out.println("Genre: " + book.getGenre());
			System.out.println("------------------------");
		}
	}

	private static void getMostReadBooksByAge(Scanner scanner) {
		// Collect user input
		System.out.print("Enter age: ");
		int age = scanner.nextInt();
		scanner.nextLine(); // Consume the newline character

		// Get most read books by age
		List<Book> books = bookDAO.getMostReadBooksByAge(age);

		// Display most read books
		System.out.println("Most Read Books by Age:");
		for (Book book : books) {
			System.out.println("ID: " + book.getId());
			System.out.println("Name: " + book.getName());
			System.out.println("Writer: " + book.getWriter());
			System.out.println("Publisher: " + book.getPublisher());
			System.out.println("Genre: " + book.getGenre());
			System.out.println("Release Year: " + book.getReleaseYear());
			System.out.println("Sample Available: " + book.isSampleAvailable());
			System.out.println("------------------------");
		}
	}

	private static void getBookSuggestionsByRating(Scanner scanner) {
		// Collect user input
		System.out.print("Enter user ID: ");
		int userId = scanner.nextInt();
		scanner.nextLine(); // Consume the newline character

		// Get shelf suggestions by rating for the user
		List<Book> books = shelfDAO.getSuggestionsByRating(userId);

		// Display shelf suggestions
		System.out.println("Book Suggestions by Rating:");
		for (Book book : books) {
			System.out.println("ID: " + book.getId());
			System.out.println("Name: " + book.getName());
			System.out.println("Genre: " + book.getGenre());
			System.out.println("------------------------");
		}
	}

	private static void showTrendingBooks() {
		// Get trending books
		List<Book> books = bookDAO.getTrendingBooks();

		// Display trending books
		System.out.println("Trending Books:");
		for (Book book : books) {
			System.out.println("ID: " + book.getId());
			System.out.println("Name: " + book.getName());
			System.out.println("Writer: " + book.getWriter());
			System.out.println("Publisher: " + book.getPublisher());
			System.out.println("Genre: " + book.getGenre());
			System.out.println("Release Year: " + book.getReleaseYear());
			System.out.println("Sample Available: " + book.isSampleAvailable());
			System.out.println("------------------------");
		}
	}

	private static void showPurchasedBooks() {
		// Get purchased books
		List<Book> books = bookDAO.getPurchasedBooks();

		// Display purchased books
		System.out.println("Purchased Books:");
		for (Book book : books) {
			System.out.println("ID: " + book.getId());
			System.out.println("Name: " + book.getName());
			System.out.println("Writer: " + book.getWriter());
			System.out.println("Publisher: " + book.getPublisher());
			System.out.println("Genre: " + book.getGenre());
			System.out.println("Release Year: " + book.getReleaseYear());
			System.out.println("Sample Available: " + book.isSampleAvailable());
			System.out.println("------------------------");
		}
	}

	private static void showPurchasePercentage() {
		int totalBooks = bookDAO.getUniqueBookCount();
		int purchasedBooks = shelfDAO.getPurchasedBookCount();

		if (totalBooks > 0) {
			double percentage = (double) purchasedBooks / totalBooks * 100;
			System.out.printf("Percentage of books purchased after reading their sample: %.2f%%%n", percentage);
		} else {
			System.out.println("No books found.");
		}
	}

	private static void closeConnection() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void createBook(Scanner scanner) {
		// Collect book details from the user
		System.out.println("Enter book details:");
		System.out.print("Name: ");
		String name = scanner.nextLine();
		System.out.print("Writer: ");
		String writer = scanner.nextLine();
		System.out.print("Publisher: ");
		String publisher = scanner.nextLine();
		System.out.print("Genre: ");
		String genre = scanner.nextLine();
		System.out.print("Release Year: ");
		int releaseYear = scanner.nextInt();
		System.out.print("Sample Available (true/false): ");
		boolean sampleAvailable = scanner.nextBoolean();
		scanner.nextLine(); // Consume the newline character

		// Create a new Book object
		Book book = new Book();
		book.setName(name);
		book.setWriter(writer);
		book.setPublisher(publisher);
		book.setGenre(genre);
		book.setReleaseYear(releaseYear);
		book.setSampleAvailable(sampleAvailable);

		// Save the book in the database
		bookDAO.create(book);

		System.out.println("Book created successfully!");
	}

	private static void updateBook(Scanner scanner) {
		// Collect book ID from the user
		System.out.print("Enter book ID to update: ");
		int bookId = scanner.nextInt();
		scanner.nextLine(); // Consume the newline character

		// Retrieve the book from the database
		Book book = bookDAO.getById(bookId);
		if (book == null) {
			System.out.println("Book not found.");
			return;
		}

		// Collect updated book details from the user
		System.out.println("Enter updated book details:");
		System.out.print("Name: ");
		String name = scanner.nextLine();
		System.out.print("Writer: ");
		String writer = scanner.nextLine();
		System.out.print("Publisher: ");
		String publisher = scanner.nextLine();
		System.out.print("Genre: ");
		String genre = scanner.nextLine();
		System.out.print("Release Year: ");
		int releaseYear = scanner.nextInt();
		System.out.print("Sample Available (true/false): ");
		boolean sampleAvailable = scanner.nextBoolean();
		scanner.nextLine(); // Consume the newline character

		// Update the book object
		book.setName(name);
		book.setWriter(writer);
		book.setPublisher(publisher);
		book.setGenre(genre);
		book.setReleaseYear(releaseYear);
		book.setSampleAvailable(sampleAvailable);

		// Update the book in the database
		bookDAO.update(book);

		System.out.println("Book updated successfully!");
	}

	private static void getBookDetails(Scanner scanner) {
		// Collect book ID from the user
		System.out.print("Enter book ID to get details: ");
		int bookId = scanner.nextInt();
		scanner.nextLine(); // Consume the newline character

		// Retrieve the book from the database
		Book book = bookDAO.getById(bookId);
		if (book == null) {
			System.out.println("Book not found.");
			return;
		}

		// Display book details
		System.out.println("Book Details:");
		System.out.println("ID: " + book.getId());
		System.out.println("Name: " + book.getName());
		System.out.println("Writer: " + book.getWriter());
		System.out.println("Publisher: " + book.getPublisher());
		System.out.println("Genre: " + book.getGenre());
		System.out.println("Release Year: " + book.getReleaseYear());
		System.out.println("Sample Available: " + book.isSampleAvailable());
	}

	private static void createShelf(Scanner scanner) {
		// Collect shelf details from the user
		System.out.println("Enter shelf details:");
		System.out.print("User ID: ");
		int userId = scanner.nextInt();
		System.out.print("Book ID: ");
		int bookId = scanner.nextInt();
		scanner.nextLine(); // Consume the newline character
		System.out.print("Description: ");
		String description = scanner.nextLine();
		System.out.print("Is Purchased (true/false): ");
		boolean isPurchased = scanner.nextBoolean();
		scanner.nextLine(); // Consume the newline character

		// Create a new Shelf object
		Shelf shelf = new Shelf();
		shelf.setUserId(userId);
		shelf.setBookId(bookId);
		shelf.setDescription(description);
		shelf.setPurchased(isPurchased);

		// Save the shelf in the database
		shelfDAO.create(shelf);

		System.out.println("Shelf created successfully!");
	}

	private static void updateShelf(Scanner scanner) {
		// Collect shelf ID from the user
		System.out.print("Enter shelf ID to update: ");
		int shelfId = scanner.nextInt();
		scanner.nextLine(); // Consume the newline character

		// Retrieve the shelf from the database
		Shelf shelf = shelfDAO.getById(shelfId);
		if (shelf == null) {
			System.out.println("Shelf not found.");
			return;
		}

		// Collect updated shelf details from the user
		System.out.println("Enter updated shelf details:");
		System.out.print("User ID: ");
		int userId = scanner.nextInt();
		System.out.print("Book ID: ");
		int bookId = scanner.nextInt();
		scanner.nextLine(); // Consume the newline character
		System.out.print("Description: ");
		String description = scanner.nextLine();
		System.out.print("Is Purchased (true/false): ");
		boolean isPurchased = scanner.nextBoolean();
		scanner.nextLine(); // Consume the newline character

		// Update the shelf object
		shelf.setUserId(userId);
		shelf.setBookId(bookId);
		shelf.setDescription(description);
		shelf.setPurchased(isPurchased);

		// Update the shelf in the database
		shelfDAO.update(shelf);

		System.out.println("Shelf updated successfully!");
	}

	private static void getShelfDetails(Scanner scanner) {
		// Collect shelf ID from the user
		System.out.print("Enter shelf ID to get details: ");
		int shelfId = scanner.nextInt();
		scanner.nextLine(); // Consume the newline character

		// Retrieve the shelf from the database
		Shelf shelf = shelfDAO.getById(shelfId);
		if (shelf == null) {
			System.out.println("Shelf not found.");
			return;
		}

		// Display shelf details
		System.out.println("Shelf Details:");
		System.out.println("ID: " + shelf.getId());
		System.out.println("User ID: " + shelf.getUserId());
		System.out.println("Book ID: " + shelf.getBookId());
		System.out.println("Description: " + shelf.getDescription());
		System.out.println("Is Purchased: " + shelf.isPurchased());
	}

	private static void createUserRating(Scanner scanner) {
		// Collect user rating details from the user
		System.out.println("Enter user rating details:");
		System.out.print("User ID: ");
		int userId = scanner.nextInt();
		System.out.print("Book ID: ");
		int bookId = scanner.nextInt();
		System.out.print("Rating (1-5): ");
		int rating = scanner.nextInt();
		scanner.nextLine(); // Consume the newline character

		// Create a new UserRating object
		UserRating userRating = new UserRating();
		userRating.setUserId(userId);
		userRating.setBookId(bookId);
		userRating.setRating(rating);

		// Save the user rating in the database
		userRatingDAO.create(userRating);

		System.out.println("User rating created successfully!");
	}
}
