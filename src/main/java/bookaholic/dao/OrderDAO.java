package bookaholic.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import bookaholic.entity.Book;
import bookaholic.entity.Order;
import bookaholic.entity.OrderDetail;
import bookaholic.model.AccountInfo;
import bookaholic.model.CartInfo;
import bookaholic.model.CartLineInfo;
import bookaholic.model.DeliveryInfo;
import bookaholic.model.OrderDetailInfo;
import bookaholic.model.OrderInfo;
import bookaholic.pagination.PaginationResult;

@Transactional
@Repository
public class OrderDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private BookDAO bookDAO;

	private int getMaxOrderId() {
		String sql = "Select max(o.orderId) from " + Order.class.getName() + " o ";
		Session session = this.sessionFactory.getCurrentSession();
		Query<Integer> query = session.createQuery(sql, Integer.class);
		Integer value = (Integer) query.getSingleResult();
		if (value == null) {
			return 0;
		}
		return value;
	}

	private int getMaxOrderDetailId() {
		String sql = "Select max(o.orderDetailId) from " + OrderDetail.class.getName() + " o ";
		Session session = this.sessionFactory.getCurrentSession();
		Query<Integer> query = session.createQuery(sql, Integer.class);
		Integer value = (Integer) query.getSingleResult();
		if (value == null) {
			return 0;
		}
		return value;
	}

	private int getBookQuantity(int bookId) {
		String sql = "Select bookQuantity from " + Book.class.getName() + " o  where bookId = " + bookId;
		Session session = this.sessionFactory.getCurrentSession();
		Query<Integer> query = session.createQuery(sql, Integer.class);
		Integer value = (Integer) query.getSingleResult();
		if (value == null) {
			return 0;
		}
		return value;
	}

	private boolean updateBookQuantity(int bookId, int oldQuantity, int quantity) {
		int newQuantity = oldQuantity - quantity;
		String sql = "UPDATE " + Book.class.getName() + " SET bookQuantity = '" + newQuantity + "' WHERE (bookId = '"
				+ bookId + "')";
		Session session = this.sessionFactory.getCurrentSession();
		Query<Integer> query = session.createQuery(sql);
		int result = query.executeUpdate();
		if (result > 0) {
			return true;
		}
		return false;
	}

	public boolean updateOrderStatus(int orderId, String orderStatus) {
		String sql = "UPDATE " + Order.class.getName() + " SET orderStatus = '" + orderStatus + "' WHERE (orderId = '"
				+ orderId + "')";
		Session session = this.sessionFactory.getCurrentSession();
		Query<Integer> query = session.createQuery(sql);
		int result = query.executeUpdate();
		if (result > 0) {
			return true;
		}
		return false;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void saveOrder(CartInfo cartInfo) {
		Session session = this.sessionFactory.getCurrentSession();
		int orderId = this.getMaxOrderId() + 1;
		Order order = new Order();
		AccountInfo accountInfo = cartInfo.getAccountInfo();
		DeliveryInfo deliveryInfo = cartInfo.getDeliveryInfo();
		order.setOrderId(orderId);
		order.setOrderAmount(cartInfo.getAmountTotal());
		order.setOrderDate(new Date());
		order.setOrderStatus("Received");
		order.setOrderComment(deliveryInfo.getOrderComment());
		order.setOrderAddress(deliveryInfo.getOrderAddress());
		order.setOrderPhone(deliveryInfo.getOrderPhone());
		order.setAccountUsername(accountInfo.getAccountUsername());
		session.persist(order);

		int orderDetailId = getMaxOrderDetailId();
		List<CartLineInfo> lines = cartInfo.getCartLines();
		for (CartLineInfo line : lines) {
			orderDetailId++;
			OrderDetail detail = new OrderDetail();
			detail.setOrderDetailId(orderDetailId);
			detail.setOrder(order);
			detail.setOrderDetailQuantity(line.getQuantity());
			detail.setOrderDetailPrice(line.getBookInfo().getBookPrice());
			detail.setOrderDetailAmount(line.getAmount());
			int bookId = line.getBookInfo().getBookId();
			Book book = this.bookDAO.findBook(bookId);
			detail.setBook(book);
			session.persist(detail);
			int oldQuantity = getBookQuantity(bookId);
			updateBookQuantity(bookId, oldQuantity, line.getQuantity());
		}
		cartInfo.setOrderId(orderId);
		session.flush();
	}

	public boolean deleteOrder(int orderId) {
		String sql = "delete from Order b WHERE orderId = " + "'" + orderId + "'";
		Session session = this.sessionFactory.getCurrentSession();
		Query<Book> query = session.createQuery(sql);
		int result = query.executeUpdate();
		if (result > 0) {
			return true;
		}
		return false;
	}

	// @page = 1, 2, ...
	public PaginationResult<OrderInfo> listOrderInfo(int page, int maxResult, int maxNavigationPage) {
		String sql = "Select new " + OrderInfo.class.getName()//
				+ "(ord.orderId, ord.orderAmount, ord.orderDate, ord.orderStatus, "
				+ " ord.orderComment, ord.orderAddress, ord.orderPhone, ord.accountUsername) " + " from "
				+ Order.class.getName() + " ord "//
				+ " order by ord.orderId desc";
		Session session = this.sessionFactory.getCurrentSession();
		Query<OrderInfo> query = session.createQuery(sql, OrderInfo.class);
		return new PaginationResult<OrderInfo>(query, page, maxResult, maxNavigationPage);
	}

	public Order findOrder(int orderId) {
		Session session = this.sessionFactory.getCurrentSession();
		return session.find(Order.class, orderId);
	}

	public OrderInfo getOrderInfo(int orderId) {
		Order order = this.findOrder(orderId);
		if (order == null) {
			return null;
		}
		return new OrderInfo(order.getOrderId(), order.getOrderAmount(), order.getOrderDate(), order.getOrderStatus(),
				order.getOrderComment(), order.getOrderAddress(), order.getOrderPhone(), order.getAccountUsername());
	}

	public OrderInfo getCustomerOrderInfo(int orderId) {
		Order order = this.findOrder(orderId);
		if (order == null) {
			return null;
		}
		return new OrderInfo(order.getOrderId(), order.getOrderAmount(), order.getOrderDate(), order.getOrderStatus(),
				order.getOrderComment(), order.getOrderAddress(), order.getOrderPhone(), order.getAccountUsername());
	}

	// @page = 1, 2, ...
	public PaginationResult<OrderInfo> listCustomerOrderInfo(int page, int maxResult, int maxNavigationPage,
			String accountUsername) {
		String sql = "Select new " + OrderInfo.class.getName()//
				+ "(ord.orderId, ord.orderAmount, ord.orderDate, ord.orderStatus, "
				+ " ord.orderComment, ord.orderAddress, ord.orderPhone, ord.accountUsername) " + " from "
				+ Order.class.getName() + " ord "//
				+ "where ord.accountUsername = '" + accountUsername + "' order by ord.orderId desc";
		Session session = this.sessionFactory.getCurrentSession();
		Query<OrderInfo> query = session.createQuery(sql, OrderInfo.class);
		return new PaginationResult<OrderInfo>(query, page, maxResult, maxNavigationPage);
	}

	public List<OrderDetailInfo> listOrderDetailInfos(int orderId) {
		String sql = "SELECT NEW " + OrderDetailInfo.class.getName() //
				+ "(d.orderDetailId, d.order.orderId, d.book.bookId, d.book.bookName, d.orderDetailPrice, d.orderDetailQuantity, d.orderDetailAmount) "//
				+ " FROM " + OrderDetail.class.getName() + " d "//
				+ " WHERE d.order.orderId = " + orderId;
		Session session = this.sessionFactory.getCurrentSession();
		Query<OrderDetailInfo> query = session.createQuery(sql, OrderDetailInfo.class);
		return query.getResultList();
	}
}
