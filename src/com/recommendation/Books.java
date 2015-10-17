package com.recommendation;

public class Books implements Comparable<Books>{

	private float rating;
	private String bookName;
	private int ratingForComparision;
	
	Books(float rating,String bookName) {
		this.rating=rating;
		this.bookName=bookName;
		this.ratingForComparision=(int) (this.rating*100);
	}

	@Override
	public int compareTo(Books compareBook) {
		// TODO Auto-generated method stub
		return compareBook.ratingForComparision-this.ratingForComparision;
	}
	
	String getBookName(){
		return this.bookName;
	}
	
	String getRating(){
		return String.valueOf(this.rating);
	}
}
