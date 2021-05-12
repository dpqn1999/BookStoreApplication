package bookaholic.model;

import bookaholic.entity.Author;


public class AuthorInfo {
	private int authorId;
	private String authorName;
	

	public AuthorInfo() {
	}

	public AuthorInfo( Author author) {
		this.authorId = author.getAuthorId();
		this.authorName = author.getAuthorName();
		
	}

	public AuthorInfo(int authorId, String authorName) {
		this.authorId = authorId;
		this.authorName = authorName;
		
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