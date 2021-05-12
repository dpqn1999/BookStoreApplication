package bookaholic.form;

import bookaholic.entity.Publisher;

public class PublisherForm {
	private int publisherId;
	private String publisherName;

	private boolean newPublisher = false;

	public PublisherForm(Publisher publisher) {
		this.publisherId = publisher.getPublisherId();
		this.publisherName = publisher.getPublisherName();
		
	}

	public PublisherForm() {
		this.newPublisher = true;
	}



	public int getPublisherId() {
		return publisherId;
	}



	public void setPublisherId(int publisherId) {
		this.publisherId = publisherId;
	}



	public String getPublisherName() {
		return publisherName;
	}



	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}



	public boolean isNewPublisher() {
		return newPublisher;
	}



	public void setNewPublisher(boolean newPublisher) {
		this.newPublisher = newPublisher;
	}

	
}
