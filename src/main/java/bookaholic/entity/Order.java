package bookaholic.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Orders")
public class Order implements Serializable {

	private static final long serialVersionUID = -2576670215015463100L;

	@Id
	@Column(name = "orderId", nullable = false)
	private int orderId;

	@Column(name = "orderAmount", nullable = false)
	private double orderAmount;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "orderDate", nullable = false)
	private Date orderDate;

	@Column(name = "orderStatus", length = 20, nullable = false)
	private String orderStatus;

	@Column(name = "orderComment", length = Integer.MAX_VALUE, nullable = true)
	private String orderComment;

	@Column(name = "orderAddress", length = 50, nullable = false)
	private String orderAddress;

	@Column(name = "orderPhone", length = 10, nullable = false)
	private String orderPhone;

	@Column(name = "accountUsername", length = 20, nullable = false)
	private String accountUsername;

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
}
