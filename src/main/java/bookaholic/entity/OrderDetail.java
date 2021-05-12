package bookaholic.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_details")
public class OrderDetail implements Serializable {

	private static final long serialVersionUID = 7550745928843183535L;

	@Id
	@Column(name = "orderDetailId", nullable = false)
	private int orderDetailId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orderId", nullable = false, //
			foreignKey = @ForeignKey(name = "order_details_ibfk_1"))
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bookId", nullable = false, //
			foreignKey = @ForeignKey(name = "order_details_ibfk_2"))
	private Book book;

	@Column(name = "orderDetailPrice", nullable = false)
	private int orderDetailPrice;
	
	@Column(name = "orderDetailQuantity", nullable = false)
	private int orderDetailQuantity;

	@Column(name = "orderDetailAmount", nullable = false)
	private int orderDetailAmount;

	public int getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(int orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public int getOrderDetailQuantity() {
		return orderDetailQuantity;
	}

	public void setOrderDetailQuantity(int orderDetailQuantity) {
		this.orderDetailQuantity = orderDetailQuantity;
	}

	public int getOrderDetailPrice() {
		return orderDetailPrice;
	}

	public void setOrderDetailPrice(int orderDetailPrice) {
		this.orderDetailPrice = orderDetailPrice;
	}

	public int getOrderDetailAmount() {
		return orderDetailAmount;
	}

	public void setOrderDetailAmount(int orderDetailAmount) {
		this.orderDetailAmount = orderDetailAmount;
	}
}
