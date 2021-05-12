package bookaholic.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Authors")
public class Author implements Serializable {

	private static final long serialVersionUID = -2576670215015463100L;

	@Id
	@Column(name = "authorId", nullable = false)
	private int authorId;

	@Column(name = "authorName", length = 50, nullable = false)
	private String authorName;

	public Author() {
	}
	
	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

}
