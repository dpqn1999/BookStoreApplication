package bookaholic.model;

public class OrderDetailInfo {
	private int orderDetailId;
	private int orderId;
	private int bookId;
	private String bookName;

	private double orderDetailPrice;
	private int orderDetailQuantity;
	private double orderDetailAmount;

	public OrderDetailInfo(int orderDetailId, int orderId, int bookId, String bookName, int orderDetailPrice,
			int orderDetailQuantity, int orderDetailAmount) {
		this.orderDetailId = orderDetailId;
		this.orderId = orderId;
		this.bookId = bookId;
		this.bookName = bookName;
		this.orderDetailPrice = orderDetailPrice;
		this.orderDetailQuantity = orderDetailQuantity;
		this.orderDetailAmount = orderDetailAmount;
	}

	public int getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(int orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
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

	public double getOrderDetailPrice() {
		return orderDetailPrice;
	}

	public void setOrderDetailPrice(double orderDetailPrice) {
		this.orderDetailPrice = orderDetailPrice;
	}

	public int getOrderDetailQuantity() {
		return orderDetailQuantity;
	}

	public void setOrderDetailQuantity(int orderDetailQuantity) {
		this.orderDetailQuantity = orderDetailQuantity;
	}

	public double getOrderDetailAmount() {
		return orderDetailAmount;
	}

	public void setOrderDetailAmount(double orderDetailAmount) {
		this.orderDetailAmount = orderDetailAmount;
	}

}
