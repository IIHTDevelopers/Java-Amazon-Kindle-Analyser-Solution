package com.assessment.amazonkindleanalyserapplication.functional;

import static com.assessment.amazonkindleanalyserapplication.testutils.TestUtils.businessTestFile;
import static com.assessment.amazonkindleanalyserapplication.testutils.TestUtils.currentTest;
import static com.assessment.amazonkindleanalyserapplication.testutils.TestUtils.testReport;
import static com.assessment.amazonkindleanalyserapplication.testutils.TestUtils.yakshaAssert;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import com.amazonkindleanalyserapplication.AmazonKindleAnalyserApplication;
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

@Component
public class FunctionalTests {

	private static BookDAO bookDAO;
	private static ShelfDAO shelfDAO;
	private static UserRatingDAO userRatingDAO;
	private static UserDAO userDAO;
	private static AmazonKindleAnalyserApplication mainObj;

	@BeforeAll
	public static void setUp() throws IOException {
		Properties properties = new Properties();
		try (InputStream inputStream = AmazonKindleAnalyserApplication.class.getClassLoader()
				.getResourceAsStream("application.properties")) {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String dbUrl = properties.getProperty("db.url") + properties.getProperty("db.database");
		String dbUser = properties.getProperty("db.username");
		String dbPassword = properties.getProperty("db.password");

		mainObj = new AmazonKindleAnalyserApplication();
		mainObj.loadDatabaseProperties();
		bookDAO = new BookDAOImpl(mainObj.getConnection());
		shelfDAO = new ShelfDAOImpl(mainObj.getConnection());
		userRatingDAO = new UserRatingDAOImpl(mainObj.getConnection());
		userDAO = new UserDAOImpl(mainObj.getConnection());
	}

	@BeforeEach
	void clearDatabase() {
		try {
			userRatingDAO.deleteAllUserRatings();
			shelfDAO.deleteAllShelves();
			userDAO.deleteAllUsers();
			bookDAO.deleteAllBooks();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterAll
	public static void afterAll() {
		testReport();
	}

	@Test
	public void testCreateUserRating() throws IOException {
		userDAO.create(new User("User", 29, "Male", "OLD", "ABC"));
		bookDAO.create(new Book("Book", "ABC", "XYZ", "OLD", 2020, false));
		UserRating userRating = new UserRating();
		userRating.setUserId(userDAO.getAllUsers().get(0).getId());
		userRating.setBookId(bookDAO.getAllBooks().get(0).getId());
		userRating.setRating(4);

		userRatingDAO.create(userRating);

		if (userRatingDAO.getAllUserRatings() != null) {
			UserRating retrievedUserRating = userRatingDAO.getById(userRatingDAO.getAllUserRatings().get(0).getId());
			try {
				yakshaAssert(currentTest(),
						retrievedUserRating != null && retrievedUserRating.getBookId() == userRating.getBookId()
								&& retrievedUserRating.getRating() == userRating.getRating() ? true : false,
						businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetUserRatingById() throws IOException {
		userDAO.create(new User("User", 29, "Male", "OLD", "ABC"));
		bookDAO.create(new Book("Book", "ABC", "XYZ", "OLD", 2020, false));
		UserRating userRating = new UserRating();
		userRating.setUserId(userDAO.getAllUsers().get(0).getId());
		userRating.setBookId(bookDAO.getAllBooks().get(0).getId());
		userRating.setRating(4);

		userRatingDAO.create(userRating);

		if (userRatingDAO.getAllUserRatings() != null) {
			UserRating retrievedUserRating = userRatingDAO.getById(userRatingDAO.getAllUserRatings().get(0).getId());
			try {
				yakshaAssert(currentTest(),
						retrievedUserRating != null && retrievedUserRating.getBookId() == userRating.getBookId()
								&& retrievedUserRating.getRating() == userRating.getRating() ? true : false,
						businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testUpdateUserRating() throws IOException {
		userDAO.create(new User("User", 29, "Male", "OLD", "ABC"));
		bookDAO.create(new Book("Book", "ABC", "XYZ", "OLD", 2020, false));
		UserRating userRating = new UserRating();
		userRating.setUserId(userDAO.getAllUsers().get(0).getId());
		userRating.setBookId(bookDAO.getAllBooks().get(0).getId());
		userRating.setRating(4);

		userRatingDAO.create(userRating);

		if (userRatingDAO.getAllUserRatings() != null) {
			userRating = userRatingDAO.getById(userRatingDAO.getAllUserRatings().get(0).getId());
			userRating.setRating(5);
			userRatingDAO.update(userRating);
			UserRating retrievedUserRating = userRatingDAO.getById(userRatingDAO.getAllUserRatings().get(0).getId());
			try {
				yakshaAssert(currentTest(),
						retrievedUserRating != null && retrievedUserRating.getRating() == userRating.getRating() ? true
								: false,
						businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testDeleteUserRating() throws IOException {
		userDAO.create(new User("User", 29, "Male", "OLD", "ABC"));
		bookDAO.create(new Book("Book", "ABC", "XYZ", "OLD", 2020, false));
		UserRating userRating = new UserRating();
		userRating.setUserId(userDAO.getAllUsers().get(0).getId());
		userRating.setBookId(bookDAO.getAllBooks().get(0).getId());
		userRating.setRating(4);

		userRatingDAO.create(userRating);
		if (userRatingDAO.getAllUserRatings() != null) {
			UserRating retrievedUserRating = userRatingDAO.getById(userRatingDAO.getAllUserRatings().get(0).getId());
			userRatingDAO.delete(retrievedUserRating);
			try {
				yakshaAssert(currentTest(), userRatingDAO.getAllUserRatings().isEmpty() ? true : false,
						businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testCreateUser() throws IOException {
		User user = new User();
		user.setName("John Doe");
		user.setAge(30);
		user.setGender("Male");
		user.setFavouriteGenre("Mystery");
		user.setFavouriteWriter("Agatha Christie");

		userDAO.create(user);

		if (userDAO.getAllUsers() != null) {
			user = userDAO.getAllUsers().get(0);
			try {
				yakshaAssert(currentTest(),
						user != null && user.getAge() == 30 && user.getName().equals("John Doe") ? true : false,
						businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetUserById() throws IOException {
		User user = new User();
		user.setName("John Doe");
		user.setAge(30);
		user.setGender("Male");
		user.setFavouriteGenre("Mystery");
		user.setFavouriteWriter("Agatha Christie");

		userDAO.create(user);

		if (userDAO.getAllUsers() != null) {
			user = userDAO.getById(userDAO.getAllUsers().get(0).getId());
			try {
				yakshaAssert(currentTest(),
						user != null && user.getAge() == 30 && user.getName().equals("John Doe") ? true : false,
						businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testUpdateUser() throws IOException {
		User user = new User();
		user.setName("John Doe");
		user.setAge(30);
		user.setGender("Male");
		user.setFavouriteGenre("Mystery");
		user.setFavouriteWriter("Agatha Christie");

		userDAO.create(user);

		if (userDAO.getAllUsers() != null) {
			user = userDAO.getById(userDAO.getAllUsers().get(0).getId());
			user.setName("Jane Smith");
			userDAO.update(user);
			user = userDAO.getById(user.getId());
			try {
				yakshaAssert(currentTest(), user != null && user.getName().equals("Jane Smith") ? true : false,
						businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testDeleteUser() throws IOException {
		User user = new User();
		user.setName("John Doe");
		user.setAge(30);
		user.setGender("Male");
		user.setFavouriteGenre("Mystery");
		user.setFavouriteWriter("Agatha Christie");

		userDAO.create(user);

		if (userDAO.getAllUsers() != null) {
			user = userDAO.getById(userDAO.getAllUsers().get(0).getId());
			userDAO.deleteAllUsers();
			try {
				yakshaAssert(currentTest(), userDAO.getAllUsers().isEmpty() ? true : false, businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetAllUsers() throws IOException {
		User user1 = new User();
		user1.setName("John Doe");
		user1.setAge(30);
		user1.setGender("Male");
		user1.setFavouriteGenre("Mystery");
		user1.setFavouriteWriter("Agatha Christie");

		User user2 = new User();
		user2.setName("Jane Smith");
		user2.setAge(25);
		user2.setGender("Female");
		user2.setFavouriteGenre("Romance");
		user2.setFavouriteWriter("Jane Austen");

		userDAO.create(user1);
		userDAO.create(user2);

		if (userDAO.getAllUsers() != null) {
			List<User> allUsers = userDAO.getAllUsers();
			try {
				yakshaAssert(currentTest(), allUsers.size() == 2 ? true : false, businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testSearchBookByNameWriterOrGenre() throws IOException {
		Book book1 = new Book();
		book1.setName("The Great Gatsby");
		book1.setWriter("F. Scott Fitzgerald");
		book1.setPublisher("Scribner");
		book1.setGenre("Classic");
		book1.setReleaseYear(1925);
		book1.setSampleAvailable(true);

		Book book2 = new Book();
		book2.setName("To Kill a Mockingbird");
		book2.setWriter("Harper Lee");
		book2.setPublisher("J. B. Lippincott & Co.");
		book2.setGenre("Classic");
		book2.setReleaseYear(1960);
		book2.setSampleAvailable(true);

		Book book3 = new Book();
		book3.setName("1984");
		book3.setWriter("George Orwell");
		book3.setPublisher("Secker & Warburg");
		book3.setGenre("Dystopian");
		book3.setReleaseYear(1949);
		book3.setSampleAvailable(true);

		bookDAO.create(book1);
		bookDAO.create(book2);
		bookDAO.create(book3);

		if (bookDAO.getAllBooks() != null) {
			List<Book> searchResults = bookDAO.search("Mockingbird");
			try {
				yakshaAssert(currentTest(),
						searchResults != null && searchResults.size() == 1
								&& searchResults.get(0).getName().equals(book2.getName()) ? true : false,
						businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetTrendingBooks() throws IOException {
		Book book1 = new Book();
		book1.setName("The Great Gatsby");
		book1.setWriter("F. Scott Fitzgerald");
		book1.setPublisher("Scribner");
		book1.setGenre("Classic");
		book1.setReleaseYear(1925);
		book1.setSampleAvailable(true);

		Book book2 = new Book();
		book2.setName("To Kill a Mockingbird");
		book2.setWriter("Harper Lee");
		book2.setPublisher("J. B. Lippincott & Co.");
		book2.setGenre("Classic");
		book2.setReleaseYear(1960);
		book2.setSampleAvailable(true);

		Book book3 = new Book();
		book3.setName("1984");
		book3.setWriter("George Orwell");
		book3.setPublisher("Secker & Warburg");
		book3.setGenre("Dystopian");
		book3.setReleaseYear(1949);
		book3.setSampleAvailable(true);

		bookDAO.create(book1);
		bookDAO.create(book2);
		bookDAO.create(book3);

		if (bookDAO.getAllBooks() != null) {
			List<Book> trendingBooks = bookDAO.getTrendingBooks();
			try {
				yakshaAssert(currentTest(), trendingBooks != null && trendingBooks.size() == 3 ? true : false,
						businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
//		assertNotNull(trendingBooks);
//		assertEquals(3, trendingBooks.size());
//		assertTrue(trendingBooks.contains(book1));
//		assertTrue(trendingBooks.contains(book2));
//		assertTrue(trendingBooks.contains(book3));
	}

	@Test
	public void testGetPurchasedBooks() throws IOException {
		User user = new User();
		user.setName("John Doe");
		user.setAge(30);
		user.setGender("Male");
		user.setFavouriteGenre("Mystery");
		user.setFavouriteWriter("Agatha Christie");

		userDAO.create(user);

		Book book1 = new Book();
		book1.setName("The Great Gatsby");
		book1.setWriter("F. Scott Fitzgerald");
		book1.setPublisher("Scribner");
		book1.setGenre("Classic");
		book1.setReleaseYear(1925);
		book1.setSampleAvailable(true);

		Book book2 = new Book();
		book2.setName("To Kill a Mockingbird");
		book2.setWriter("Harper Lee");
		book2.setPublisher("J. B. Lippincott & Co.");
		book2.setGenre("Classic");
		book2.setReleaseYear(1960);
		book2.setSampleAvailable(true);

		Book book3 = new Book();
		book3.setName("1984");
		book3.setWriter("George Orwell");
		book3.setPublisher("Secker & Warburg");
		book3.setGenre("Dystopian");
		book3.setReleaseYear(1949);
		book3.setSampleAvailable(true);

		bookDAO.create(book1);
		bookDAO.create(book2);
		bookDAO.create(book3);

		if (userDAO.getAllUsers() != null && bookDAO.getAllBooks() != null) {
			Shelf shelf1 = new Shelf();
			shelf1.setUserId(userDAO.getAllUsers().get(0).getId());
			shelf1.setBookId(bookDAO.getAllBooks().get(0).getId());
			shelf1.setPurchased(true);
			shelf1.setDescription("Description");
			shelfDAO.create(shelf1);

			Shelf shelf2 = new Shelf();
			shelf2.setUserId(userDAO.getAllUsers().get(0).getId());
			shelf2.setBookId(bookDAO.getAllBooks().get(2).getId());
			shelf2.setPurchased(true);
			shelf2.setDescription("Description");
			shelfDAO.create(shelf1);

			List<Book> purchasedBooks = bookDAO.getPurchasedBooks();
			if (purchasedBooks != null) {
				try {
					yakshaAssert(currentTest(), purchasedBooks.size() == 2 ? true : false, businessTestFile);
				} catch (Exception e) {
					yakshaAssert(currentTest(), false, businessTestFile);
				}
			} else {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}

//		assertNotNull(purchasedBooks);
//		assertEquals(2, purchasedBooks.size());
//		assertTrue(purchasedBooks.contains(book2));
//		assertTrue(purchasedBooks.contains(book3));
	}

	@Test
	public void testGetPurchasePercentage() throws IOException {
		User user = new User();
		user.setName("John Doe");
		user.setAge(30);
		user.setGender("Male");
		user.setFavouriteGenre("Mystery");
		user.setFavouriteWriter("Agatha Christie");

		userDAO.create(user);

		Book book1 = new Book();
		book1.setName("The Great Gatsby");
		book1.setWriter("F. Scott Fitzgerald");
		book1.setPublisher("Scribner");
		book1.setGenre("Classic");
		book1.setReleaseYear(1925);
		book1.setSampleAvailable(true);

		Book book2 = new Book();
		book2.setName("To Kill a Mockingbird");
		book2.setWriter("Harper Lee");
		book2.setPublisher("J. B. Lippincott & Co.");
		book2.setGenre("Classic");
		book2.setReleaseYear(1960);
		book2.setSampleAvailable(true);

		bookDAO.create(book1);
		bookDAO.create(book2);

		if (userDAO.getAllUsers() != null && bookDAO.getAllBooks() != null) {

			Shelf shelf1 = new Shelf();
			shelf1.setUserId(userDAO.getAllUsers().get(0).getId());
			shelf1.setBookId(bookDAO.getAllBooks().get(0).getId());
			shelf1.setPurchased(true);
			shelf1.setDescription("Description");
			shelfDAO.create(shelf1);

			float purchasePercentage = bookDAO.getPurchasePercentage();
			try {
				yakshaAssert(currentTest(), purchasePercentage == 100f ? true : false, businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetMostReadBooksByAge() throws IOException {
		Book book1 = new Book();
		book1.setName("The Great Gatsby");
		book1.setWriter("F. Scott Fitzgerald");
		book1.setPublisher("Scribner");
		book1.setGenre("Classic");
		book1.setReleaseYear(1925);
		book1.setSampleAvailable(true);

		Book book2 = new Book();
		book2.setName("To Kill a Mockingbird");
		book2.setWriter("Harper Lee");
		book2.setPublisher("J. B. Lippincott & Co.");
		book2.setGenre("Classic");
		book2.setReleaseYear(1960);
		book2.setSampleAvailable(true);

		Book book3 = new Book();
		book3.setName("1984");
		book3.setWriter("George Orwell");
		book3.setPublisher("Secker & Warburg");
		book3.setGenre("Dystopian");
		book3.setReleaseYear(1949);
		book3.setSampleAvailable(true);

		bookDAO.create(book1);
		bookDAO.create(book2);
		bookDAO.create(book3);

		int userAge = 30;

		if (bookDAO.getMostReadBooksByAge(userAge) != null) {
			List<Book> mostReadBooks = bookDAO.getMostReadBooksByAge(userAge);
			try {
				yakshaAssert(currentTest(), true, businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetUniqueBookCount() throws IOException {
		Book book1 = new Book();
		book1.setName("The Great Gatsby");
		book1.setWriter("F. Scott Fitzgerald");
		book1.setPublisher("Scribner");
		book1.setGenre("Classic");
		book1.setReleaseYear(1925);
		book1.setSampleAvailable(true);

		Book book2 = new Book();
		book2.setName("To Kill a Mockingbird");
		book2.setWriter("Harper Lee");
		book2.setPublisher("J. B. Lippincott & Co.");
		book2.setGenre("Classic");
		book2.setReleaseYear(1960);
		book2.setSampleAvailable(true);

		Book book3 = new Book();
		book3.setName("1984");
		book3.setWriter("George Orwell");
		book3.setPublisher("Secker & Warburg");
		book3.setGenre("Dystopian");
		book3.setReleaseYear(1949);
		book3.setSampleAvailable(true);

		bookDAO.create(book1);
		bookDAO.create(book2);
		bookDAO.create(book3);

		int uniqueBookCount = bookDAO.getUniqueBookCount();
		try {
			yakshaAssert(currentTest(), uniqueBookCount == 3 ? true : false, businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testCreateBook() throws IOException {
		Book book = new Book();
		book.setName("The Great Gatsby");
		book.setWriter("F. Scott Fitzgerald");
		book.setPublisher("Scribner");
		book.setGenre("Classic");
		book.setReleaseYear(1925);
		book.setSampleAvailable(true);

		bookDAO.create(book);

		if (bookDAO.getAllBooks() != null) {
			Book retrievedBook = bookDAO.getById(bookDAO.getAllBooks().get(0).getId());
			try {
				yakshaAssert(currentTest(), retrievedBook != null && retrievedBook.getName().equals(book.getName())
						&& retrievedBook.getWriter().equals(book.getWriter()) ? true : false, businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetBookById() throws IOException {
		Book book = new Book();
		book.setName("The Great Gatsby");
		book.setWriter("F. Scott Fitzgerald");
		book.setPublisher("Scribner");
		book.setGenre("Classic");
		book.setReleaseYear(1925);
		book.setSampleAvailable(true);

		bookDAO.create(book);

		if (bookDAO.getAllBooks() != null) {
			Book retrievedBook = bookDAO.getById(bookDAO.getAllBooks().get(0).getId());
			try {
				yakshaAssert(currentTest(), retrievedBook != null && retrievedBook.getName().equals(book.getName())
						&& retrievedBook.getWriter().equals(book.getWriter()) ? true : false, businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testUpdateBook() throws IOException {
		Book book = new Book();
		book.setName("The Great Gatsby");
		book.setWriter("F. Scott Fitzgerald");
		book.setPublisher("Scribner");
		book.setGenre("Classic");
		book.setReleaseYear(1925);
		book.setSampleAvailable(true);

		bookDAO.create(book);

		if (bookDAO.getAllBooks() != null) {
			book = bookDAO.getById(bookDAO.getAllBooks().get(0).getId());
			book.setName("Pride and Prejudice");
			bookDAO.update(book);
			Book retrievedBook = bookDAO.getById(bookDAO.getAllBooks().get(0).getId());
			try {
				yakshaAssert(currentTest(), retrievedBook != null && retrievedBook.getName().equals(book.getName())
						&& retrievedBook.getWriter().equals(book.getWriter()) ? true : false, businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testDeleteBook() throws IOException {
		Book book = new Book();
		book.setName("The Great Gatsby");
		book.setWriter("F. Scott Fitzgerald");
		book.setPublisher("Scribner");
		book.setGenre("Classic");
		book.setReleaseYear(1925);
		book.setSampleAvailable(true);

		bookDAO.create(book);
		if (bookDAO.getAllBooks() != null) {
			book = bookDAO.getById(bookDAO.getAllBooks().get(0).getId());
			bookDAO.deleteAllBooks();
			try {
				yakshaAssert(currentTest(), bookDAO.getAllBooks().isEmpty() ? true : false, businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetAllBooks() throws IOException {
		Book book1 = new Book();
		book1.setName("The Great Gatsby");
		book1.setWriter("F. Scott Fitzgerald");
		book1.setPublisher("Scribner");
		book1.setGenre("Classic");
		book1.setReleaseYear(1925);
		book1.setSampleAvailable(true);

		Book book2 = new Book();
		book2.setName("To Kill a Mockingbird");
		book2.setWriter("Harper Lee");
		book2.setPublisher("J. B. Lippincott & Co.");
		book2.setGenre("Classic");
		book2.setReleaseYear(1960);
		book2.setSampleAvailable(true);

		bookDAO.create(book1);
		bookDAO.create(book2);

		if (bookDAO.getAllBooks() != null) {
			List<Book> allBooks = bookDAO.getAllBooks();
			try {
				yakshaAssert(currentTest(), allBooks != null && allBooks.size() == 2 ? true : false, businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testCreateShelf() throws IOException {
		userDAO.create(new User("User", 29, "Male", "OLD", "ABC"));
		bookDAO.create(new Book("Book", "ABC", "XYZ", "OLD", 2020, false));
		if (bookDAO.getAllBooks() != null && bookDAO.getAllBooks() != null) {
			Shelf shelf = new Shelf();
			shelf.setUserId(userDAO.getAllUsers().get(0).getId());
			shelf.setBookId(bookDAO.getAllBooks().get(0).getId());
			shelf.setDescription("My Favorite Books");
			shelf.setPurchased(true);

			shelfDAO.create(shelf);
			if (shelfDAO.getAllShelves() != null) {
				Shelf retrievedShelf = shelfDAO.getById(shelfDAO.getAllShelves().get(0).getId());
				try {
					yakshaAssert(currentTest(),
							retrievedShelf != null && retrievedShelf.getUserId() == shelf.getUserId()
									&& retrievedShelf.getDescription().equals(shelf.getDescription()) ? true : false,
							businessTestFile);
				} catch (Exception e) {
					yakshaAssert(currentTest(), false, businessTestFile);
				}
			} else {
				yakshaAssert(currentTest(), false, businessTestFile);
			}

		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testUpdateShelf() throws IOException {
//		Shelf shelf = new Shelf();
//		shelf.setUserId(1);
//		shelf.setBookId(1);
//		shelf.setDescription("My Favorite Books");
//		shelf.setPurchased(true);
//
//		shelfDAO.createShelf(shelf);
//
//		shelf.setDescription("Updated Shelf Description");
//
//		shelfDAO.updateShelf(shelf);
//
//		Shelf retrievedShelf = shelfDAO.getShelfById(shelf.getId());

//		assertNotNull(retrievedShelf);
//		assertEquals(shelf.getDescription(), retrievedShelf.getDescription());
		userDAO.create(new User("User", 29, "Male", "OLD", "ABC"));
		bookDAO.create(new Book("Book", "ABC", "XYZ", "OLD", 2020, false));
		if (bookDAO.getAllBooks() != null && bookDAO.getAllBooks() != null) {
			Shelf shelf = new Shelf();
			shelf.setUserId(userDAO.getAllUsers().get(0).getId());
			shelf.setBookId(bookDAO.getAllBooks().get(0).getId());
			shelf.setDescription("My Favorite Books");
			shelf.setPurchased(true);

			shelfDAO.create(shelf);

			if (shelfDAO.getAllShelves() != null) {
				Shelf retrievedShelf = shelfDAO.getById(shelfDAO.getAllShelves().get(0).getId());
				retrievedShelf.setDescription("Updated Shelf Description");
				shelfDAO.update(retrievedShelf);
				retrievedShelf = shelfDAO.getById(shelfDAO.getAllShelves().get(0).getId());
				try {
					yakshaAssert(currentTest(),
							retrievedShelf != null
									&& retrievedShelf.getDescription().equals("Updated Shelf Description") ? true
											: false,
							businessTestFile);
				} catch (Exception e) {
					yakshaAssert(currentTest(), false, businessTestFile);
				}
			} else {
				yakshaAssert(currentTest(), false, businessTestFile);
			}

		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetShelfById() throws IOException {
		userDAO.create(new User("User", 29, "Male", "OLD", "ABC"));
		bookDAO.create(new Book("Book", "ABC", "XYZ", "OLD", 2020, false));
		if (bookDAO.getAllBooks() != null && bookDAO.getAllBooks() != null) {
			Shelf shelf = new Shelf();
			shelf.setUserId(userDAO.getAllUsers().get(0).getId());
			shelf.setBookId(bookDAO.getAllBooks().get(0).getId());
			shelf.setDescription("My Favorite Books");
			shelf.setPurchased(true);

			shelfDAO.create(shelf);

			if (shelfDAO.getAllShelves() != null) {
				Shelf retrievedShelf = shelfDAO.getById(shelfDAO.getAllShelves().get(0).getId());
				try {
					yakshaAssert(currentTest(),
							retrievedShelf != null && retrievedShelf.getDescription().equals("My Favorite Books") ? true
									: false,
							businessTestFile);
				} catch (Exception e) {
					yakshaAssert(currentTest(), false, businessTestFile);
				}
			} else {
				yakshaAssert(currentTest(), false, businessTestFile);
			}

		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testDeleteShelf() throws IOException {
		userDAO.create(new User("User", 29, "Male", "OLD", "ABC"));
		bookDAO.create(new Book("Book", "ABC", "XYZ", "OLD", 2020, false));
		if (bookDAO.getAllBooks() != null && bookDAO.getAllBooks() != null) {
			Shelf shelf = new Shelf();
			shelf.setUserId(userDAO.getAllUsers().get(0).getId());
			shelf.setBookId(bookDAO.getAllBooks().get(0).getId());
			shelf.setDescription("My Favorite Books");
			shelf.setPurchased(true);

			shelfDAO.create(shelf);

			if (shelfDAO.getAllShelves() != null) {
				shelfDAO.deleteAllShelves();
				try {
					yakshaAssert(currentTest(), shelfDAO.getAllShelves().isEmpty() ? true : false, businessTestFile);
				} catch (Exception e) {
					yakshaAssert(currentTest(), false, businessTestFile);
				}
			} else {
				yakshaAssert(currentTest(), false, businessTestFile);
			}

		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetAllShelves() throws IOException {
		userDAO.create(new User("User", 29, "Male", "OLD", "ABC"));
		bookDAO.create(new Book("Book", "ABC", "XYZ", "OLD", 2020, false));
		if (bookDAO.getAllBooks() != null && bookDAO.getAllBooks() != null) {
			Shelf shelf = new Shelf();
			shelf.setUserId(userDAO.getAllUsers().get(0).getId());
			shelf.setBookId(bookDAO.getAllBooks().get(0).getId());
			shelf.setDescription("My Favorite Books");
			shelf.setPurchased(true);

			shelfDAO.create(shelf);

			shelf = new Shelf();
			shelf.setUserId(userDAO.getAllUsers().get(0).getId());
			shelf.setBookId(bookDAO.getAllBooks().get(0).getId());
			shelf.setDescription("My Favorite Books 2");
			shelf.setPurchased(false);

			shelfDAO.create(shelf);

			if (shelfDAO.getAllShelves() != null) {
				try {
					yakshaAssert(currentTest(), shelfDAO.getAllShelves().size() == 2 ? true : false, businessTestFile);
				} catch (Exception e) {
					yakshaAssert(currentTest(), false, businessTestFile);
				}
			} else {
				yakshaAssert(currentTest(), false, businessTestFile);
			}

		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}
}
