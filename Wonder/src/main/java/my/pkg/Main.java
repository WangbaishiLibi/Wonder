package my.pkg;


import java.text.DateFormat;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import com.entity.*;

public class Main {

	public static void main(String[] arg0s) {
		// TODO Auto-generated method stub
		PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("Tutorial");
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try{
            tx.begin();
            for(int i=0; i<300; i++){
            	Ticket tic = new Ticket("黑客帝国", DateFormat.getDateTimeInstance().parse("2016-01-02 18:00:00"), "待售");
            	pm.makePersistent(tic);
            }
            tx.commit();
        }
        catch (Exception e){
        	e.printStackTrace();
        }
        finally{
            if (tx.isActive()){
                tx.rollback();
            }
            pm.close();
        }
		
		
		
	}

}
