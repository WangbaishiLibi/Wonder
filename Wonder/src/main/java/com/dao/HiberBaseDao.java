package com.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings({ "unchecked"})
public class HiberBaseDao {
	
	
//	@Autowired
	protected SessionFactory sessionFactory;
	
	
	public List<Object> query(String sql){
		return sessionFactory.openSession().createSQLQuery(sql).list();
	}
	
	
	public int excuteBySql(String sql) throws Exception{
		Session session = sessionFactory.openSession();
		int res = sessionFactory.openSession().createSQLQuery(sql).executeUpdate();
		session.flush();
		session.close();
		return res;
	}
	
	public void update(Object entity) {
		Session session = sessionFactory.openSession();
		sessionFactory.openSession().update(entity);
		session.flush();
		session.close();
	}
	
	public void insert(String entity) {
		Session session = sessionFactory.openSession();
		sessionFactory.openSession().save(entity);
		session.flush();
	}
	
	public void delete(String entity) throws Exception{
		Session session = sessionFactory.openSession();
		sessionFactory.openSession().delete(entity);
		session.flush();
	}

}
