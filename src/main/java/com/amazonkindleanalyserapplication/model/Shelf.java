package com.amazonkindleanalyserapplication.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shelves")
public class Shelf {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "user_id")
	private int userId;

	@Column(name = "book_id")
	private int bookId;

	@Column(name = "description")
	private String description;

	@Column(name = "is_purchased")
	private boolean isPurchased;

	public Shelf() {
		super();
	}

	public Shelf(int id, int userId, int bookId, String description, boolean isPurchased) {
		super();
		this.id = id;
		this.userId = userId;
		this.bookId = bookId;
		this.description = description;
		this.isPurchased = isPurchased;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isPurchased() {
		return isPurchased;
	}

	public void setPurchased(boolean isPurchased) {
		this.isPurchased = isPurchased;
	}

	@Override
	public String toString() {
		return "Shelf [id=" + id + ", userId=" + userId + ", bookId=" + bookId + ", description=" + description
				+ ", isPurchased=" + isPurchased + "]";
	}

}
