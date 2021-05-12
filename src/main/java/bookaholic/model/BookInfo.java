package bookaholic.model;

import bookaholic.entity.Book;

public class BookInfo {
	private int bookId;
	private String bookName;
	private int bookPrice;
	private String bookType;
	private int bookQuantity;
	private String bookDescription;
	private String authorId;
	private String publisherId;

	public BookInfo() {
	}

	public BookInfo(Book book) {
		this.bookId = book.getBookId();
		this.bookName = book.getBookName();
		this.bookPrice = book.getBookPrice();
		this.bookType = book.getBookType();
		this.bookQuantity = book.getBookQuantity();
		this.bookDescription = book.getBookDescription();
		this.authorId = book.getAuthorId();
		this.publisherId = book.getPublisherId();
	}

	public BookInfo(int bookId, String bookName, int bookPrice, String bookType, int bookQuantity,
			String bookDescription, String authorId, String publisherId) {
		this.bookId = bookId;
		this.bookName = bookName;
		this.bookPrice = bookPrice;
		this.bookType = bookType;
		this.bookQuantity = bookQuantity;
		this.bookDescription = bookDescription;
		this.authorId = authorId;
		this.publisherId = publisherId;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public int getBookPrice() {
		return bookPrice;
	}

	public void setBookPrice(int bookPrice) {
		this.bookPrice = bookPrice;
	}

	public String getBookType() {
		return bookType;
	}

	public void setBookType(String bookType) {
		this.bookType = bookType;
	}

	public int getBookQuantity() {
		return bookQuantity;
	}

	public void setBookQuantity(int bookQuantity) {
		this.bookQuantity = bookQuantity;
	}

	public String getBookDescription() {
		return bookDescription;
	}

	public void setBookDescription(String bookDescription) {
		this.bookDescription = bookDescription;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
	}

}
