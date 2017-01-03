package com.dao;

import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Repository;

import com.entity.Ticket;
import com.util.Page;



@Repository
@Configurable
public class JPABaseDao {

	PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("Tutorial");
//	EntityManagerFactory emf = Persistence.createEntityManagerFactory("Tutorial");
//	PersistenceManager pm = pmf.getPersistenceManager();
//	EntityManager em = emf.createEntityManager();
	
	
	public PersistenceManager getPM(){	
		return pmf.getPersistenceManager();
	}
	
//	public EntityManager getEM(){
//		if(tx.isActive())	tx.begin();
//		return this.em;
//	}
	
	public Transaction begin(){
		Transaction tx = getPM().currentTransaction();
		tx.begin();
		return tx;
	}
	
	public Transaction begin(PersistenceManager pm){
		Transaction tx =pm.currentTransaction();
		tx.begin();
		return tx;
	}
	
	public boolean commit(Transaction tx){
		try{
			tx.commit();
		}catch (Exception e){
        	e.printStackTrace();
        	return false;
        }
        finally{
            if (tx.isActive()){
                tx.rollback();
            }
        }
		return true;
	}
	
	
	public <T> Object query(Class<T> cls){
		PersistenceManager pm = getPM();
		Transaction tx = pm.currentTransaction();
		tx.begin();
		Query<T> query = pm.newQuery(cls);
		Object obj = query.executeList();
		commit(tx);
		return obj;
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> Object queryByPage(Class<T> cls, Page page){
		PersistenceManager pm = getPM();
		Transaction tx = begin();
		Query<?> query = pm.newQuery("select from " + cls.getName());
		query.addExtension("datanucleus.query.results.cached", "true");
		query.setResultClass(cls);
		page.setTotal(query.executeList().size());
		if(page.getPage_total()>0){
			page.setPage_count((int)Math.ceil((float)page.getTotal()/page.getPage_total()));
		}
		int min = (page.getPage_index() - 1) * page.getPage_total();
		int max = page.getPage_index() * page.getPage_total();
		query.range(min, max);
		Object obj = (List<T>)query.execute();
		commit(tx);
		return obj;
	}
	
	
	public int count(Class<?> cls, String whrSql){
		PersistenceManager pm = getPM();
		Transaction tx = pm.currentTransaction();
		Query<?> query = pm.newQuery("select from " + cls.getName() +  whrSql);
		commit(tx);
		return query.executeList().size();
	}
	
	
}
