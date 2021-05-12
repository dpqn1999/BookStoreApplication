package bookaholic.dao;

import java.io.IOException;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import bookaholic.entity.Book;
import bookaholic.form.BookForm;
import bookaholic.model.BookInfo;
import bookaholic.pagination.PaginationResult;

@Transactional
@Repository
public class BookDAO {
	@Autowired
	private SessionFactory sessionFactory;

	public Book findBook(int bookId) {
		try {
			String sql = "SELECT e FROM " + Book.class.getName() + " e WHERE e.bookId =:bookId";
			Session session = this.sessionFactory.getCurrentSession();
			Query<Book> query = session.createQuery(sql, Book.class);
			query.setParameter("bookId", bookId);
			return (Book) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public BookInfo findBookInfo(int bookId) {
		Book book = this.findBook(bookId);
		if (book == null) {
			return null;
		}
		return new BookInfo(book.getBookId(), book.getBookName(), book.getBookPrice(), book.getBookType(),
				book.getBookQuantity(), book.getBookDescription(), book.getAuthorId(), book.getPublisherId());
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void saveBook(BookForm bookForm) {
		Session session = this.sessionFactory.getCurrentSession();
		int bookId = bookForm.getBookId();
		Book book = null;
		boolean isNew = false;
		if (Integer.toString(bookId) != null) {
			book = this.findBook(bookId);
		}
		if (book == null) {
			isNew = true;
			book = new Book();
		}
		book.setBookId(bookId);
		book.setBookName(bookForm.getBookName());
		book.setBookPrice(bookForm.getBookPrice());
		book.setBookType(bookForm.getBookType());
		book.setBookQuantity(bookForm.getBookQuantity());
		book.setBookDescription(bookForm.getBookDescription());
		book.setAuthorId(bookForm.getAuthorId());
		book.setPublisherId(bookForm.getPublisherId());

		if (bookForm.getFileData() != null) {
			byte[] image = null;
			try {
				image = bookForm.getFileData().getBytes();
			} catch (IOException e) {
			}
			if (image != null && image.length > 0) {
				book.setBookImage(image);
			}
		}
		if (isNew) {
			session.persist(book);
		}
		session.flush();
	}

	public boolean deleteBook(int bookId) {
		String sql = "delete from Book b WHERE bookId = " + "'" + bookId + "'";
		Session session = this.sessionFactory.getCurrentSession();
		Query<Book> query = session.createQuery(sql);
		int result = query.executeUpdate();
		if (result > 0) {
			return true;
		}
		return false;
	}

	public PaginationResult<BookInfo> listBookInfo(int page, int maxResult, int maxNavigationPage) {
		String sql = "Select new " + BookInfo.class.getName()//
				+ "(b.bookId, b.bookName, b.bookPrice, b.bookType, "
				+ " b.bookQuantity, b.bookDescription, b.bookImage, b.authorId, b.publisherId) " + " from "
				+ Book.class.getName() + " b "//
				+ " order by b.bookId";

		Session session = this.sessionFactory.getCurrentSession();
		Query<BookInfo> query = session.createQuery(sql, BookInfo.class);
		return new PaginationResult<BookInfo>(query, page, maxResult, maxNavigationPage);
	}

	public PaginationResult<BookInfo> queryBooks(int page, int maxResult, int maxNavigationPage, String likeName) {
		String sql = "SELECT NEW " + BookInfo.class.getName() //
				+ "(b.bookId, b.bookName, b.bookPrice, b.bookType, b.bookQuantity, b.bookDescription, b.authorId, b.publisherId) "
				+ " from " + Book.class.getName() + " b";
		if (likeName != null && likeName.length() > 0) {
			sql += "WHERE LOWER(b.bookName) LIKE :likeName ";
		}
		sql += " ORDER BY b.bookId";
		Session session = this.sessionFactory.getCurrentSession();
		Query<BookInfo> query = session.createQuery(sql, BookInfo.class);
		if (likeName != null && likeName.length() > 0) {
			query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
		}
		return new PaginationResult<BookInfo>(query, page, maxResult, maxNavigationPage);
	}

	public PaginationResult<BookInfo> searchBooks(int page, int maxResult, int maxNavigationPage, String likeName) {
		String sql = "SELECT distinct NEW " + BookInfo.class.getName() //
				+ "(b.bookId, b.bookName, b.bookPrice, b.bookType, b.bookQuantity, b.bookDescription, b.authorId, b.publisherId) "
				+ " from Book b, Author a, Publisher p WHERE (b.bookName) LIKE " + "'%" + likeName + "%'"
				+ " or (a.authorName) LIKE " + "'%" + likeName + "%' AND b.authorId = a.authorId"
				+ " or (p.publisherName) LIKE " + "'%" + likeName + "%' AND b.publisherId = p.publisherId";
		sql += " ORDER BY b.bookId";
		Session session = this.sessionFactory.getCurrentSession();
		Query<BookInfo> query = session.createQuery(sql, BookInfo.class);
		return new PaginationResult<BookInfo>(query, page, maxResult, maxNavigationPage);
	}

	public PaginationResult<BookInfo> queryBooks(int page, int maxResult, int maxNavigationPage) {
		return queryBooks(page, maxResult, maxNavigationPage, null);
	}
}
