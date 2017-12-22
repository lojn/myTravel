package myTravel;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
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
@SessionScoped
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
Connection conn;
private boolean loggedIn;
private boolean registerIn=false;
 public Visitor(){}
 public Visitor(String username,String password){
	 this.username=username;
	 this.password=password;
 }
	 	  
 @Resource(name = "jdbc/mytravel")
private	 DataSource ds;
 
private Logger logger=Logger.getLogger(UserBean.class.getName());
 
 public String getUsername() {   
	   
	 if (registerIn)
		          return username;
	 else return null;
		
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
  
 public boolean nameExists(String name) throws NotSupportedException,SystemException,RollbackException,HeuristicCommitException, SQLException {
  
	 Context context = null;
	 if(ds==null)
	 try {
		 
		 Context ctx = new InitialContext();
		 context = (Context) ctx.lookup("java:comp/env");
		 logger.log(Level.SEVERE,"befor context.lookup ... ");
		 ds = (DataSource)context.lookup("jdbc/mytravel");
		 } catch (NamingException e){return false;}
	 
	  
	  conn=ds.getConnection();
	  if(conn==null) throw new SQLException("No connection");
	  PreparedStatement ps=null;
	  ResultSet rs=null;
	  logger.log(Level.SEVERE,"in the beginning for nameExists...");
	  String query="select username from user_t where username=?";
	  try{
		  ps=conn.prepareStatement(query);
		  ps.setString(1,name);
		  rs=ps.executeQuery();
		  boolean next1;
		  next1=rs.next();
		  conn.close();
		  return next1;
	  }catch (SQLException ex){
		  ex.printStackTrace();
		  return false;
	  }
	  /*
	  
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
	 */
 }
 
 public void doLogin() throws SQLException{
	  loggedIn=false;
      count=0;
	  try{
		  conn.setAutoCommit(false);
		  boolean committed=false;
		  try{
			  PreparedStatement passwordQuery=conn.prepareStatement
					  ("select password,logincount from user_t where username=?" );
			  passwordQuery.setString(1,username);
			  ResultSet rs=passwordQuery.executeQuery();
			  if(!rs.next())return;
			  String storedPassword=rs.getString("password");
			  loggedIn=password.equals(storedPassword);
			  count=rs.getInt("logincount");
			  PreparedStatement updatecounterStat=conn.prepareStatement(
      "update user_t set logincount=logincount+1 where username=?");
			  updatecounterStat.setString(1,username);
			  updatecounterStat.executeUpdate();
			  conn.commit();
			  committed=true;
		  }finally{
			  if(!committed)conn.rollback();
		  }
		  
	  }finally{conn.close();}
	  
 }
 
 private boolean verifyPassword(){
	 	  logger.log(Level.INFO,"begin to enter verifyPassword ... ");
	 	  registerIn=false;
	 	 if(ds==null)
	 		 try {
	 			 
	 			 Context ctx = new InitialContext();
	 			Context context = (Context) ctx.lookup("java:comp/env");
	 			 logger.log(Level.SEVERE,"befor context.lookup ... ");
	 			 ds = (DataSource)context.lookup("jdbc/mytravel");
	 			 } catch (NamingException e){return false;}
	 		 
	 	  try{
		 
		  conn=ds.getConnection();
		  if(conn==null) throw new SQLException("No connection");
		  PreparedStatement ps=null;
		  ResultSet rs=null;
		  String query="select username from user_t where username=? and password=?";
		  try{
			  ps=conn.prepareStatement(query);
			  ps.setString(1,username);
			  ps.setString(2,password);			  
			  rs=ps.executeQuery();
			  boolean next1=rs.next();
			  if (next1) registerIn=true;
			  conn.close();
			  return next1;
		  }catch (SQLException ex){
			  ex.printStackTrace();
			  return false;
		  }
		  
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

 public String sendMail() throws Exception
 {
	 
	 Properties prop = new Properties();
	         prop.setProperty("mail.host", "smtp.qq.com");
	          prop.setProperty("mail.transport.protocol", "smtp");
	          prop.setProperty("mail.smtp.auth", "true");
	          //使用JavaMail发送邮件的5个步骤
	          //1、创建session
	          Session session = Session.getInstance(prop);
	          //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
	          session.setDebug(true);
	          //2、通过session得到transport对象
	          Transport ts = session.getTransport();
	          //3、连上邮件服务器，需要发件人提供邮箱的用户名和密码进行验证
	          ts.connect("smtp.qq.com", "406349406", "pwd#ljqq");
	          //4、创建邮件
	          Message message = createImageMail(session);
	          //5、发送邮件
	          ts.sendMessage(message, message.getAllRecipients());
	          ts.close();
	          return "default";
 }
 
 
 public static MimeMessage createImageMail(Session session) throws Exception {
	          //创建邮件
	   MimeMessage message = new MimeMessage(session);
	            //指明邮件的发件人
	            message.setFrom(new InternetAddress("406349406@qq.com"));
	            //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
	            message.setRecipient(Message.RecipientType.TO, new InternetAddress("ycluoj@189.cn"));
	            //邮件的标题
	            message.setSubject("只包含文本的简单邮件");
	            //邮件的文本内容
	            message.setContent("你好啊！", "text/html;charset=UTF-8");
	            //返回创建好的邮件对象
	            return message;	      }
 
 
}
