package bookaholic.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import bookaholic.entity.Author;
import bookaholic.form.AuthorForm;
import bookaholic.model.AuthorInfo;
import bookaholic.pagination.PaginationResult;

@Transactional
@Repository
public class AuthorDAO {
	@Autowired
	private SessionFactory sessionFactory;

	public Author findAuthor(int authorId) {
		try {
			String sql = "SELECT e FROM " + Author.class.getName() + " e WHERE e.authorId =:authorId";

			Session session = this.sessionFactory.getCurrentSession();
			Query<Author> query = session.createQuery(sql, Author.class);
			query.setParameter("authorId", authorId);
			return (Author) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public AuthorInfo findAuthorInfo(int authorId) {
		Author author = this.findAuthor(authorId);
		if (author == null) {
			return null;
		}
		return new AuthorInfo(author.getAuthorId(), author.getAuthorName());
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void saveAuthor(AuthorForm authorForm) {
		Session session = this.sessionFactory.getCurrentSession();
		int authorId = authorForm.getAuthorId();
		Author author = null;
		boolean isNew = false;
		if (Integer.toString(authorId) != null) {
			author = this.findAuthor(authorId);
		}
		if (author == null) {
			isNew = true;
			author = new Author();
		}
		author.setAuthorId(authorId);
		author.setAuthorName(authorForm.getAuthorName());

		if (isNew) {
			session.persist(author);
		}
		session.flush();
	}

	public boolean deleteAuthor(int authorId) {
		String sql = "delete from Author a WHERE authorId = " + "'" + authorId + "'";
		System.out.println(sql);
		Session session = this.sessionFactory.getCurrentSession();
		Query<Author> query = session.createQuery(sql);
		int result = query.executeUpdate();
		if (result > 0) {
			return true;
		}
		return false;
	}

	// @page = 1, 2, ...
	public PaginationResult<AuthorInfo> listAuthorInfo(int page, int maxResult, int maxNavigationPage) {
		String sql = "SELECT NEW " + AuthorInfo.class.getName()//
				+ "(author.authorId, author.authorName)" + " FROM " + Author.class.getName() + " author "//
				+ " ORDER BY author.authorId ";

		Session session = this.sessionFactory.getCurrentSession();
		Query<AuthorInfo> query = session.createQuery(sql, AuthorInfo.class);
		return new PaginationResult<AuthorInfo>(query, page, maxResult, maxNavigationPage);
	}

	public PaginationResult<AuthorInfo> queryAuthors(int page, int maxResult, int maxNavigationPage, String likeName) {
		String sql = "SELECT NEW " + Author.class.getName() //
				+ "(b.authorId, b.authorName) " + " from " + Author.class.getName() + " b";
		if (likeName != null && likeName.length() > 0) {
			sql += "WHERE LOWER(b.authorName) LIKE :likeName ";
		}
		sql += " ORDER BY b.authorId";
		System.out.println(sql + "-----------------");
		Session session = this.sessionFactory.getCurrentSession();
		Query<AuthorInfo> query = session.createQuery(sql, AuthorInfo.class);
		if (likeName != null && likeName.length() > 0) {
			query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
		}
		return new PaginationResult<AuthorInfo>(query, page, maxResult, maxNavigationPage);
	}

	public PaginationResult<AuthorInfo> queryAuthors(int page, int maxResult, int maxNavigationPage) {
		return queryAuthors(page, maxResult, maxNavigationPage, null);
	}

	public Map<String, String> getMapAuthors() {
		PaginationResult<AuthorInfo> paginationAuthors //
				= listAuthorInfo(1, 20, 100);
		List<AuthorInfo> lst = paginationAuthors.getList();
		Map<String, String> map = new HashMap<String, String>();
		for (AuthorInfo authorInfo : lst) {
			map.put(Integer.toString(authorInfo.getAuthorId()), authorInfo.getAuthorName());
		}
		return map;
	}
}
