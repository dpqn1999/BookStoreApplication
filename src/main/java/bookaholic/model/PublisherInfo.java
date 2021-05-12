package bookaholic.model;

import bookaholic.entity.Publisher;

public class PublisherInfo {
	private int publisherId;
	private String publisherName;
	

	public PublisherInfo() {
	}

	public PublisherInfo( Publisher publisher) {
		this.publisherId =publisher.getPublisherId();
		this.publisherName = publisher.getPublisherName();
		
	}

	public PublisherInfo(int publisherId, String publisherName) {
		this.publisherId = publisherId;
		this.publisherName = publisherName;
		
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

	
	
}
