package com.amazonkindleanalyserapplication.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;

	private int age;

	private String gender;

	@Column(name = "favourite_genre")
	private String favouriteGenre;

	@Column(name = "favourite_writer")
	private String favouriteWriter;

	public User() {
		super();
	}

	public User(String name, int age, String gender, String favouriteGenre, String favouriteWriter) {
		super();
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.favouriteGenre = favouriteGenre;
		this.favouriteWriter = favouriteWriter;
	}
	
	public User(int id, String name, int age, String gender, String favouriteGenre, String favouriteWriter) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.favouriteGenre = favouriteGenre;
		this.favouriteWriter = favouriteWriter;
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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getFavouriteGenre() {
		return favouriteGenre;
	}

	public void setFavouriteGenre(String favouriteGenre) {
		this.favouriteGenre = favouriteGenre;
	}

	public String getFavouriteWriter() {
		return favouriteWriter;
	}

	public void setFavouriteWriter(String favouriteWriter) {
		this.favouriteWriter = favouriteWriter;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", age=" + age + ", gender=" + gender + ", favouriteGenre="
				+ favouriteGenre + ", favouriteWriter=" + favouriteWriter + "]";
	}
}
