package bookaholic.form;

import bookaholic.model.DeliveryInfo;
import bookaholic.model.PasswordInfo;

public class PasswordForm {
	private String oldPassword;
	private String newPassword;

	private boolean valid;

	public PasswordForm() {
	}

	public PasswordForm(PasswordInfo passwordInfo) {
		if (passwordInfo != null) {
			this.oldPassword = passwordInfo.getOldPassword();
			this.newPassword = passwordInfo.getNewPassword();
		}
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
