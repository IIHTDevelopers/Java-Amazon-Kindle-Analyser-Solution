package com.amazonkindleanalyserapplication.repository;

import java.util.List;

import com.amazonkindleanalyserapplication.model.Book;

public interface BookDAO {
	void create(Book book);

	void update(Book book);

	Book getById(int id);

	List<Book> search(String keyword);

	List<Book> getTrendingBooks();

	List<Book> getPurchasedBooks();

	float getPurchasePercentage();

	List<Book> getMostReadBooksByAge(int age);

	int getUniqueBookCount();

	void deleteAllBooks();

	List<Book> getAllBooks();
}
