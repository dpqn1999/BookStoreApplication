package bookaholic.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Publishers")
public class Publisher implements Serializable {

	private static final long serialVersionUID = -2576670215015463100L;

	@Id
	@Column(name = "publisherId", nullable = false)
	private int publisherId;

	@Column(name = "publisherName", length = 50, nullable = false)
	private String publisherName;

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
