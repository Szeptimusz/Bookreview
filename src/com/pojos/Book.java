package com.pojos;

/**
 * A könyveket reprezentáló osztály. Ennek segítségével történik meg
 * a könyveket leíró adatok átadása.
 * @author Szept
 *
 */
public class Book {
	private int id;
	private String author;
	private String title;
	private float reviewpoint;
	private float myPoint;
	private String message;
	
	public Book(int id, String author, String title, float reviewpoint) {
		this.id = id;
		this.author = author;
		this.title = title;
		this.reviewpoint = reviewpoint;
	}
	
	public Book(int id, String author, String title, float reviewpoint, float myPoint, String message) {
		this.id = id;
		this.author = author;
		this.title = title;
		this.reviewpoint = reviewpoint;
		this.myPoint = myPoint;
		this.message = message;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public float getReviewpoint() {
		return reviewpoint;
	}

	public void setReviewpoint(float reviewpoint) {
		this.reviewpoint = reviewpoint;
	}

	public float getMyPoint() {
		return myPoint;
	}

	public void setMyPoint(float myPoint) {
		this.myPoint = myPoint;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
