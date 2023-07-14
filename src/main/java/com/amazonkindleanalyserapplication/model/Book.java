package com.amazonkindleanalyserapplication.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "books")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;

	private String writer;

	private String publisher;

	private String genre;

	@Column(name = "release_year")
	private int releaseYear;

	@Column(name = "sample_available")
	private boolean sampleAvailable;

	public Book() {
		super();
	}

	public Book(String name, String writer, String publisher, String genre, int releaseYear, boolean sampleAvailable) {
		super();
		this.name = name;
		this.writer = writer;
		this.publisher = publisher;
		this.genre = genre;
		this.releaseYear = releaseYear;
		this.sampleAvailable = sampleAvailable;
	}

	public Book(int id, String name, String writer, String publisher, String genre, int releaseYear,
			boolean sampleAvailable) {
		super();
		this.id = id;
		this.name = name;
		this.writer = writer;
		this.publisher = publisher;
		this.genre = genre;
		this.releaseYear = releaseYear;
		this.sampleAvailable = sampleAvailable;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public int getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(int releaseYear) {
		this.releaseYear = releaseYear;
	}

	public boolean isSampleAvailable() {
		return sampleAvailable;
	}

	public void setSampleAvailable(boolean sampleAvailable) {
		this.sampleAvailable = sampleAvailable;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", name=" + name + ", writer=" + writer + ", publisher=" + publisher + ", genre="
				+ genre + ", releaseYear=" + releaseYear + ", sampleAvailable=" + sampleAvailable + "]";
	}
}
