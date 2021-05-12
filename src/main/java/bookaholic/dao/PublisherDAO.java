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

import bookaholic.entity.Publisher;
import bookaholic.form.PublisherForm;
import bookaholic.model.AuthorInfo;
import bookaholic.model.PublisherInfo;
import bookaholic.pagination.PaginationResult;

@Transactional
@Repository
public class PublisherDAO {
	@Autowired
	private SessionFactory sessionFactory;

	public Publisher findPublisher(int publisherId) {
		try {
			String sql = "SELECT e FROM " + Publisher.class.getName() + " e WHERE e.publisherId =:publisherId";

			Session session = this.sessionFactory.getCurrentSession();
			Query<Publisher> query = session.createQuery(sql, Publisher.class);
			query.setParameter("publisherId", publisherId);
			return (Publisher) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public PublisherInfo findPublisherInfo(int publisherId) {
		Publisher publisher = this.findPublisher(publisherId);
		if (publisher == null) {
			return null;
		}
		return new PublisherInfo(publisher.getPublisherId(), publisher.getPublisherName());
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void savePublisher(PublisherForm publisherForm) {
		Session session = this.sessionFactory.getCurrentSession();
		int publisherId = publisherForm.getPublisherId();
		Publisher publisher = null;
		boolean isNew = false;
		if (Integer.toString(publisherId) != null) {
			publisher = this.findPublisher(publisherId);
		}
		if (publisher == null) {
			isNew = true;
			publisher = new Publisher();
		}
		publisher.setPublisherId(publisherId);
		publisher.setPublisherName(publisherForm.getPublisherName());

		if (isNew) {
			session.persist(publisher);
		}
		session.flush();
	}

	public boolean deletePublisher(int publisherId) {
		String sql = "delete from Publisher e WHERE publisherId = " + "'" + publisherId + "'";
		Session session = this.sessionFactory.getCurrentSession();
		Query<Publisher> query = session.createQuery(sql);
		int result = query.executeUpdate();
		if (result > 0) {
			return true;
		}
		return false;
	}

	// @page = 1, 2, ...
	public PaginationResult<PublisherInfo> listPublisherInfo(int page, int maxResult, int maxNavigationPage) {
		String sql = "SELECT NEW " + PublisherInfo.class.getName()//
				+ "(publisher.publisherId, publisher.publisherName)" + " FROM " + Publisher.class.getName()
				+ " publisher "//
				+ " ORDER BY publisher.publisherId ";

		Session session = this.sessionFactory.getCurrentSession();
		Query<PublisherInfo> query = session.createQuery(sql, PublisherInfo.class);
		return new PaginationResult<PublisherInfo>(query, page, maxResult, maxNavigationPage);
	}

	public PaginationResult<PublisherInfo> queryPublishers(int page, int maxResult, int maxNavigationPage,
			String likeName) {
		String sql = "SELECT NEW " + Publisher.class.getName() //
				+ "(b.publisherId, b.publisherName) " + " from " + Publisher.class.getName() + " b";
		if (likeName != null && likeName.length() > 0) {
			sql += "WHERE LOWER(b.publisherName) LIKE :likeName ";
		}
		sql += " ORDER BY b.publisherId";
		Session session = this.sessionFactory.getCurrentSession();
		Query<PublisherInfo> query = session.createQuery(sql, PublisherInfo.class);
		if (likeName != null && likeName.length() > 0) {
			query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
		}
		return new PaginationResult<PublisherInfo>(query, page, maxResult, maxNavigationPage);
	}

	public PaginationResult<PublisherInfo> queryPublishers(int page, int maxResult, int maxNavigationPage) {
		return queryPublishers(page, maxResult, maxNavigationPage, null);
	}

	public Map<String, String> getMapPublishers() {
		PaginationResult<PublisherInfo> paginationPublishers //
				= listPublisherInfo(1, 20, 100);
		List<PublisherInfo> lst = paginationPublishers.getList();
		Map<String, String> map = new HashMap<String, String>();
		for (PublisherInfo publisherInfo : lst) {
			map.put(Integer.toString(publisherInfo.getPublisherId()), publisherInfo.getPublisherName());
		}
		return map;
	}
}
