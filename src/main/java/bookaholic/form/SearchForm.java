package bookaholic.form;

public class SearchForm {
	private String searchKeyword;

	public SearchForm() {
		super();
	}

	public SearchForm(String searchKeyword) {
		super();
		this.searchKeyword = searchKeyword;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

}
