package bookaholic.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "Books")
public class Book implements Serializable {

	private static final long serialVersionUID = -1000119078147252957L;

	@Id
	@Column(name = "bookId", nullable = false)
	private int bookId;

	@Column(name = "bookName", length = 50, nullable = false)
	private String bookName;

	@Column(name = "bookPrice", nullable = false)
	private int bookPrice;

	@Column(name = "bookType", length = 50, nullable = false)
	private String bookType;

	@Column(name = "bookQuantity", nullable = false)
	private int bookQuantity;

	@Column(name = "bookDescription", length = Integer.MAX_VALUE, nullable = true)
	private String bookDescription;

	@Lob
	@Column(name = "bookImage", length = Integer.MAX_VALUE, nullable = true)
	private byte[] bookImage;

	@Column(name = "authorId", length = 20, nullable = true)
	private String authorId;

	@Column(name = "publisherId", length = 20, nullable = true)
	private String publisherId;

	public Book() {
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

	public byte[] getBookImage() {
		return bookImage;
	}

	public void setBookImage(byte[] bookImage) {
		this.bookImage = bookImage;
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
