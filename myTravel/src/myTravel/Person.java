package myTravel;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Person implements Serializable {
@Id
private String username;
private String password;
private int loginCount;

public Person(){}
public Person(String username,String password){
	this.username=username;
	this.password=password;
}
public String getPassword(){
	return password;
}
public void setPassword(String newVal){
	this.password=newVal;
}

public String getUsername(){
	return username;
}
public void setUsername(String str){
	this.username=str;
}

public int getLoginCount(){
	return loginCount;
}
public void setLoginCount(int newCount){
	this.loginCount=newCount;
}

}
