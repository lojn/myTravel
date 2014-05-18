package myTravel;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

   @Entity
   public class Counselor implements Serializable {
   @Id
   private int counselor_id;
   private String first_name;
   private String nick_name;
   private String last_name;
    private String telephone;
   private String email;
   @Temporal(TemporalType.DATE)
   private Date member_since;

   public Counselor() {} // Required by JPA

   public Counselor(String first_name, String last_name,String telephone,
                   String email) {
      this.first_name = first_name;
      this.last_name = last_name;
      this.telephone = telephone;
      this.email = email;
   }

   public int getCounselor_id(){return counselor_id;}
   public void setCounselor_id(int counselor_id)
       {this.counselor_id=counselor_id;}

   public String getFirst_name() { return first_name; }
   public void setFirst_name(String first_name)
     {this.last_name=last_name;}

   public String getLast_name() { return last_name; }
   public void setLast_name(String last_name)
     {this.last_name=last_name;}

   public String getNick_name() { return nick_name; }
   public void setNick_name(String nick_name)
     {this.nick_name=nick_name;}

   public String getTelephone() { return telephone; }
   public void setTelephone(String telephone)
     {this.telephone=telephone;}

   public String getEmail() { return email; }
   public void setEmail(String email)
     {this.email=email;}

   public Date getMember_since() { return member_since; }
   public void setMember_since(Date member_since)
     {this.member_since=member_since;}
}



