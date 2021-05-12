package bookaholic.form;

import bookaholic.entity.Author;

public class AuthorForm {
	private int authorId;
	private String authorName;

	private boolean newAuthor = false;



	public AuthorForm() {
		this.newAuthor = true;
	}

	public AuthorForm(Author author) {
		this.authorId = author.getAuthorId();
		this.authorName = author.getAuthorName();
		
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

	public boolean isNewAuthor() {
		return newAuthor;
	}

	public void setNewAuthor(boolean newAuthor) {
		this.newAuthor = newAuthor;
	}

	

}
