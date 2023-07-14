package com.amazonkindleanalyserapplication.repository;

import java.util.List;

import com.amazonkindleanalyserapplication.model.Book;
import com.amazonkindleanalyserapplication.model.Shelf;

public interface ShelfDAO {
	void create(Shelf shelf);

	void update(Shelf shelf);

	Shelf getById(int id);

	List<Book> getSuggestionsByGenre(int userId);

	List<Book> getSuggestionsByRating(int userId);

	List<Book> getMostReadBooksByAge(int age);

	int getPurchasedBookCount();

	void deleteAllShelves();

	List<Shelf> getAllShelves();
}
