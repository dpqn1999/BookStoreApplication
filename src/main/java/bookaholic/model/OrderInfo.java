package bookaholic.model;

import java.util.Date;
import java.util.List;

public class OrderInfo {

	private int orderId;
	private double orderAmount;
	private Date orderDate;
	private String orderStatus;
	private String orderComment;
	private String orderAddress;
	private String orderPhone;
	private String accountUsername;

	private List<OrderDetailInfo> details;

	public OrderInfo() {
	}

	public OrderInfo(int orderId, double orderAmount, Date orderDate, String orderStatus, String orderComment,
			String orderAddress, String orderPhone, String accountUsername) {
		this.orderId = orderId;
		this.orderAmount = orderAmount;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.orderComment = orderComment;
		this.orderAddress = orderAddress;
		this.orderPhone = orderPhone;
		this.accountUsername = accountUsername;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderComment() {
		return orderComment;
	}

	public void setOrderComment(String orderComment) {
		this.orderComment = orderComment;
	}

	public String getOrderAddress() {
		return orderAddress;
	}

	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}

	public String getOrderPhone() {
		return orderPhone;
	}

	public void setOrderPhone(String orderPhone) {
		this.orderPhone = orderPhone;
	}

	public String getAccountUsername() {
		return accountUsername;
	}

	public void setAccountUsername(String accountUsername) {
		this.accountUsername = accountUsername;
	}

	public List<OrderDetailInfo> getDetails() {
		return details;
	}

	public void setDetails(List<OrderDetailInfo> details) {
		this.details = details;
	}

}
