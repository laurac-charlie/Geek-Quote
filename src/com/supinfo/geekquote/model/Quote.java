package com.supinfo.geekquote.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Quote implements Serializable{

	private static final long serialVersionUID = 1L;
	private String strQuote = "";
	private int rating;
	private String creationDate;
	
	public Quote(String strQuote, int rating) {
		this.strQuote = strQuote;
		this.rating = rating;
	}

	public Quote(){}
	
	public Quote(String str)
	{
		this.strQuote = str;
		SimpleDateFormat formatd = new SimpleDateFormat ("dd/MM/yyyy"); 
		this.creationDate = formatd.format(new Date());
		this.rating = 0;
	}
	
	public String getStrQuote() {
		return strQuote;
	}
	public void setStrQuote(String strQuote) {
		this.strQuote = strQuote;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public String toString() {
		return this.strQuote;
	}
	
}
