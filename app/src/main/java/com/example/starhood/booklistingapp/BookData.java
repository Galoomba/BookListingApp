package com.example.starhood.booklistingapp;

/**
 * Created by Starhood on 6/9/17.
 */

public class BookData {

    private String bookName;
    private String bookWriter;
    private String bookDetails;

    public BookData(String bookName, String bookWriter, String bookDetails) {
        this.bookName = bookName;
        this.bookWriter = bookWriter;
        this.bookDetails = bookDetails;
    }

    public String getBookDetails() {
        return bookDetails;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookWriter() {
        return bookWriter;
    }
}
