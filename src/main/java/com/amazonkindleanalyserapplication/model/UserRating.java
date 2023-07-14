package com.amazonkindleanalyserapplication.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_ratings")
public class UserRating {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "user_id")
	private int userId;

	@Column(name = "book_id")
	private int bookId;

	private int rating;

	public UserRating() {
		super();
	}

	public UserRating(int id, int userId, int bookId, int rating) {
		super();
		this.id = id;
		this.userId = userId;
		this.bookId = bookId;
		this.rating = rating;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return "UserRating [id=" + id + ", userId=" + userId + ", bookId=" + bookId + ", rating=" + rating + "]";
	}
}
