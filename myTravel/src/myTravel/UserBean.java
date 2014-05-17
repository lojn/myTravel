package myTravel;

import java.io.Serializable;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.*;

@ManagedBean(name="user")   
@SessionScoped  

public class UserBean implements Serializable {

	   private  static  final  long  serialVersionUID  =  20140509890362L; 
	  	  private String name;  	  
	      private String password;  
	     private String register;
	     private int count;
	     private boolean loggedIn;
	     private boolean registerIn=false;
	     
@Resource(name="jdbc/mytravel")
	     private DataSource ds;
	     private Logger logger=Logger.getLogger("myTravel");
	      
	      public String getName() {   
	    	  if(name==null){
	    		  return "–¬≈Û”—";
	    		  }else {
	    		          return name; 
	    		          }
	      }   
	    
	      public void setName(String newValue) {   
	          name = newValue;   
	      }   
	    
	      public String getPassword() {   
	          return password;   
	      }   
	    
	      public void setPassword(String newValue) {   
	          password = newValue;   
	      }
	       
	      public boolean nameExists(String name) throws SQLException {
	    	  if(ds==null){
	    		  try{
	    		  Context ctx=new InitialContext();
	    		  ds=(DataSource)ctx.lookup("java:comp/env/jdbc/mytravel");	    		  
	    		  }catch (NamingException ex){
	    			  ex.printStackTrace();
	    		  }
	    	  }
	    	  if(ds==null) throw new SQLException("No data source");
	    	  Connection conn=ds.getConnection();
	    	  if(conn==null) throw new SQLException("No connection");
	    	  PreparedStatement ps=null;
	    	  ResultSet rs=null;
	    	  System.out.print("in the beginning for nameExists...");
	    	  String query="select username from user_t where username=?";
	    	  try{
	    		  ps=conn.prepareStatement(query);
	    		  ps.setString(1,name);
	    		  rs=ps.executeQuery();
	    		  return rs.next();
	    	  }catch (SQLException ex){
	    		  ex.printStackTrace();
	    		  return false;
	    	  } 
	    	   
	      }
	      public void doLogin() throws SQLException{
	    	  loggedIn=false;
	    	  if(ds==null) throw new SQLException ("No data source");
	    	  Connection conn=ds.getConnection();
	    	  if(conn==null) throw new SQLException ("No connection");
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
	      }
	      
	      private boolean verifyPassword(){
	    	  try {
	    		  System.out.print("\nbegin to nameExists ... ");
	    		  registerIn=nameExists(name);
	    	  }catch(SQLException ex){
	    		//  logger.log(Level.SEVERE,"no name in th list",ex);
	    		 ex.printStackTrace();
	    		  return false;
	    	  }
	    	  
	    	  if(registerIn){
	    		  try{
	    			  System.out.print("befor doLogin");
	    		  doLogin();
	    		  }	    	  
	    	  catch(SQLException ex){return false;}
	    	  }
	    	  if(loggedIn) return true;
	    	  else {return false;}
	      }
	      
	      
	      public String loginAction(){
	    	//  FacesMessage doneMessage = null;
	    	  String outcome = null;
	    	    if (verifyPassword()) {
	    	   //     doneMessage = new FacesMessage("Successfully login");
	    	    	 System.out.print("\nSuccessfully login");
	    	        outcome = "default";
	    	    } else {
	    	     //   doneMessage = new FacesMessage("Failed to login");
	    	    	System.out.print("failed to login");
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
