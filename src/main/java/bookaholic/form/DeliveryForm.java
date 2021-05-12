package bookaholic.form;

import bookaholic.model.DeliveryInfo;

public class DeliveryForm {
	private String orderAddress;
	private String orderPhone;
	private String orderComment;

	private boolean valid;

	public DeliveryForm() {
	}

	public DeliveryForm(DeliveryInfo deliveryInfo) {
		if (deliveryInfo != null) {
			this.orderAddress = deliveryInfo.getOrderAddress();
			this.orderPhone = deliveryInfo.getOrderPhone();
			this.orderComment = deliveryInfo.getOrderComment();
		}
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

	public String getOrderComment() {
		return orderComment;
	}

	public void setOrderComment(String orderComment) {
		this.orderComment = orderComment;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

}
