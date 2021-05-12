package bookaholic.model;

import bookaholic.form.DeliveryForm;
import bookaholic.form.PasswordForm;

public class PasswordInfo {
	private String oldPassword;
	private String newPassword;

	private boolean valid;

	public PasswordInfo() {
	}

	public PasswordInfo(PasswordForm passwordForm) {
		this.oldPassword = passwordForm.getOldPassword();
		this.newPassword = passwordForm.getNewPassword();
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

}
