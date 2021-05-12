package bookaholic.model;

import java.util.ArrayList;
import java.util.List;

public class CartInfo {

	private int orderId;

	private AccountInfo accountInfo;

	private DeliveryInfo deliveryInfo;

	private final List<CartLineInfo> cartLines = new ArrayList<CartLineInfo>();

	public CartInfo() {
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public AccountInfo getAccountInfo() {
		return accountInfo;
	}

	public void setAccountInfo(AccountInfo accountInfo) {
		this.accountInfo = accountInfo;
	}

	public DeliveryInfo getDeliveryInfo() {
		return deliveryInfo;
	}

	public void setDeliveryInfo(DeliveryInfo deliveryInfo) {
		this.deliveryInfo = deliveryInfo;
	}

	public List<CartLineInfo> getCartLines() {
		return this.cartLines;
	}

	private CartLineInfo findLineByBookId(int bookId) {
		for (CartLineInfo line : this.cartLines) {
			if (line.getBookInfo().getBookId() == (bookId)) {
				return line;
			}
		}
		return null;
	}

	public void addBook(BookInfo bookInfo, int quantity) {
		CartLineInfo line = this.findLineByBookId(bookInfo.getBookId());

		if (line == null) {
			line = new CartLineInfo();
			line.setQuantity(0);
			line.setBookInfo(bookInfo);
			this.cartLines.add(line);
		}
		int newQuantity = line.getQuantity() + quantity;
		if (newQuantity <= 0) {
			this.cartLines.remove(line);
		} else {
			line.setQuantity(newQuantity);
		}
	}

	public void validate() {
	}

	public void updateBook(int bookId, int quantity) {
		CartLineInfo line = this.findLineByBookId(bookId);
		if (line != null) {
			if (quantity <= 0) {
				this.cartLines.remove(line);
			} else {
				line.setQuantity(quantity);
			}
		}
	}

	public void removeBook(BookInfo bookInfo) {
		CartLineInfo line = this.findLineByBookId(bookInfo.getBookId());
		if (line != null) {
			this.cartLines.remove(line);
		}
	}

	public boolean isEmpty() {
		return this.cartLines.isEmpty();
	}

	public int getQuantityTotal() {
		int quantity = 0;
		for (CartLineInfo line : this.cartLines) {
			quantity += line.getQuantity();
		}
		return quantity;
	}

	public double getAmountTotal() {
		double total = 0;
		for (CartLineInfo line : this.cartLines) {
			total += line.getAmount();
		}
		return total;
	}

	public void updateQuantity(CartInfo cartForm) {
		if (cartForm != null) {
			List<CartLineInfo> lines = cartForm.getCartLines();
			System.out.println(lines.get(0).getBookInfo().getBookId());
			for (CartLineInfo line : lines) {
				this.updateBook(line.getBookInfo().getBookId(), line.getQuantity());
			}
		}
	}

	public boolean isValidAccount() {
		return this.accountInfo != null && this.accountInfo.isValid();
	}
	
	
}
