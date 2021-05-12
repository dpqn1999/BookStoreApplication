package bookaholic.model;

import bookaholic.form.DeliveryForm;

public class DeliveryInfo {

	private String orderAddress;
	private String orderPhone;
	private String orderComment;

	private boolean valid;

	public DeliveryInfo() {
	}

	public DeliveryInfo(DeliveryForm deliveryForm) {
		this.orderAddress = deliveryForm.getOrderAddress();
		this.orderPhone = deliveryForm.getOrderPhone();
		this.orderComment = deliveryForm.getOrderComment();
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
