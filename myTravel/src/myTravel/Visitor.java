package myTravel;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.*;
import javax.sql.DataSource;
import javax.transaction.HeuristicCommitException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

@ManagedBean(name="visitor")
public class Visitor implements Serializable{
	
 private  static  final  long  serialVersionUID  =  20140518890362L; 
@Id
private String username;
private String password;
private int logincount;
  private int count;

@PersistenceUnit(unitName="default")
private EntityManagerFactory emf;
@Resource
private UserTransaction utx;
private boolean loggedIn;
private boolean registerIn=false;
 public Visitor(){}
 public Visitor(String username,String password){
	 this.username=username;
	 this.password=password;
 }
	 	  

 
private Logger logger=Logger.getLogger(UserBean.class.getName());
 
 public String getUsername() {   
	  if(username==null){
		  return "–¬≈Û”—";
		  }else {
		          return username; 
		          }
 }   
 
public int getLogincount(){return logincount;}

 public void setUsername(String newValue) {   
     this.username = newValue;   
 }   

 public String getPassword() {   
     return password;   
 }   

 public void setPassword(String newValue) {   
     password = newValue;   
 }
  
 public boolean nameExists(String name) throws NotSupportedException,SystemException,RollbackException,HeuristicCommitException {
 
	 /*
	  if(ds==null) throw new SQLException("No data source");
	  Connection conn=ds.getConnection();
	  if(conn==null) throw new SQLException("No connection");
	  PreparedStatement ps=null;
	  ResultSet rs=null;
	  logger.log(Level.SEVERE,"in the beginning for nameExists...");
	  String query="select username from user_t where username=?";
	  try{
		  ps=conn.prepareStatement(query);
		  ps.setString(1,name);
		  rs=ps.executeQuery();
		  return rs.next();
	  }catch (SQLException ex){
		  ex.printStackTrace();
		  return false;
	  }*/
	 logger.log(Level.INFO,"begin nameExists... using utx");
	 emf = javax.persistence.Persistence.createEntityManagerFactory("org.eclipse.persistence.jpa.PersistenceProvider");
	 if(emf==null)logger.log(Level.INFO,"emf is null,why?");
	 EntityManager em=emf.createEntityManager();
	 try {
		 Query qrstat=em.createQuery("select username,logincount,password from user_t where username=:username")
				 .setParameter("username",username);
		 logger.log(Level.INFO,"query...");
		 @SuppressWarnings("unchecked")
		 List<Person> result=qrstat.getResultList();
		 logger.log(Level.INFO,"List in nameExists..."+result.toString());
			 
		 if(result.size()==1){
			 Person c=result.get(0);
		     registerIn=false;
		     if(c.getPassword().equals(password)){
		    	 loggedIn=true;
		    	 count=c.getLoginCount();
		    	 c.setLoginCount(count+1);
		    	 return true;
		     }
		 }
	 } catch(Exception ex){
		 logger.log(Level.INFO,"query..."+ex.toString());
				 
	 }
	 return false;
 }
 
 public void doLogin() throws SQLException{
	  loggedIn=false;
/*
	  try{
		  conn.setAutoCommit(false);
		  boolean committed=false;
		  try{
			  PreparedStatement passwordQuery=conn.prepareStatement
					  ("select password,logincount from user_t where username=?" );
			  passwordQuery.setString(1,name);
			  ResultSet rs=passwordQuery.executeQuery();
			  if(!rs.next())return;
			  String storedPassword=rs.getString("password");
			  loggedIn=password.equals(storedPassword);
			  count=rs.getInt("logincount");
			  PreparedStatement updatecounterStat=conn.prepareStatement(
      "update user_t set logincount=logincount+1 where username=?");
			  updatecounterStat.setString(1,name);
			  updatecounterStat.executeUpdate();
			  conn.commit();
			  committed=true;
		  }finally{
			  if(!committed)conn.rollback();
		  }
		  
	  }finally{conn.close();}
	  */
 }
 
 private boolean verifyPassword(){
	 	  logger.log(Level.INFO,"begin to enter nameExists ... ");
	 	  try{
		  registerIn=nameExists(username);
	 	  }catch(Exception ex){ex.printStackTrace();}
	 	  return registerIn;
	  /*
	  if(registerIn){
		  try{
			  logger.log(Level.INFO,"befor doLogin");
		  doLogin();
		  }	    	  
	  catch(SQLException ex){return false;}
	  }
	  if(loggedIn) return true;
	  else {return false;}
	  */
 }
 
 
 public String loginAction(){
	//  FacesMessage doneMessage = null;
	  String outcome = null;
	  logger.log(Level.INFO,"begin loginAction...");
	    if (verifyPassword()) {
	   //     doneMessage = new FacesMessage("Successfully login");
	    	 logger.log(Level.INFO,"Successfully login");
	        outcome = "default";
	    } else {
	     //   doneMessage = new FacesMessage("Failed to login");
	    	 logger.log(Level.INFO,"failed to login");
	        outcome = "login";
	    }
//	  FacesContext.getCurrentInstance().addMessage(null, doneMessage);
	  return outcome;
 }
 
 public String addConfirmedUser() {
	  boolean added = true;
	  FacesMessage doneMessage = null;
	  String outcome = null;
	    if (added) {
	        doneMessage = new FacesMessage("Successfully added new user");
	        outcome = "welcome";
	    } else {
	        doneMessage = new FacesMessage("Failed to add new user");
	        outcome = "error";
	    }
	  FacesContext.getCurrentInstance().addMessage(null, doneMessage);
	  return outcome;
	}

 
}
