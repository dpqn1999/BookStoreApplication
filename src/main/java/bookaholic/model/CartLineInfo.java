package bookaholic.model;


public class CartLineInfo {

	private BookInfo bookInfo;
	private int quantity;

	public CartLineInfo() {
		this.quantity = 0;
	}

	public BookInfo getBookInfo() {
		return bookInfo;
	}

	public void setBookInfo(BookInfo bookInfo) {
		this.bookInfo = bookInfo;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getAmount() {
		return this.bookInfo.getBookPrice() * this.quantity;
	}

}
