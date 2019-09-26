package com.pojos;

/**
 * A k�nyveket reprezent�l� oszt�ly. Ennek seg�ts�g�vel t�rt�nik meg
 * a k�nyveket le�r� adatok �tad�sa.
 * @author Szept
 *
 */
public class Book {
	private int id;
	private String author;
	private String title;
	private float reviewpoint;
	
	public Book(int id, String author, String title, float reviewpoint) {
		this.id = id;
		this.author = author;
		this.title = title;
		this.reviewpoint = reviewpoint;
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
	
	
}
