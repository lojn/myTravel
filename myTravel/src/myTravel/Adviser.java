package myTravel;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import javax.persistence.*;



@Named("adviser")
@SessionScoped
@Stateful
public class Adviser {
   private int counselor_id;
   private String first_name;
   private String nick_name;
   private String last_name;
   private String telephone;
   private String email;
   private Date member_since;

   private int subject_id;
   private String subjectname;
   private String description;
   private int counselor_idfk;

   private boolean loggedIn=false;
   private boolean existIn=false;
   protected boolean addOK=false;

   @PersistenceContext(unitName="default")
   private EntityManager em;

    public int getCounselor_id() {
        return counselor_id;
    }

    public void setCounselor_id(int counselor_id) {
        this.counselor_id = counselor_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

     public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

     public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getMember_since() {
        return member_since;
    }

    public void setMember_since(Date member_since) {
        this.member_since = member_since;
    }
   
    
    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public String getSubjectname() {
        return subjectname;
    }

     public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

     public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

     public int getCounselor_idfk() {
        return counselor_idfk;
    }

    public void setCounselor_idfk(int counselor_idfk) {
        this.counselor_idfk = counselor_idfk;
    }

   public String selectSub() {
      try {
         doSelect();
         return "done";
      }
      catch (Exception ex) {
         Logger.getLogger("com.salmon").log(Level.SEVERE, "login failed", ex);
         return "error";
      }   
   }

   public void doSelect() {
      Query query = em.createQuery(
         "SELECT s FROM Subject s WHERE s.subjectname = :subjectname")
         .setParameter("subjectname", subjectname);
      @SuppressWarnings("unchecked")
      List<Subject> result = query.getResultList();
      if (result.size() == 1) {
          existIn=true;
         Subject s = result.get(0);
         counselor_idfk = s.getCounselor_idfk();
         description=s.getDescription();
      }
      Query queryC = em.createQuery(
         "SELECT c FROM Counselor c WHERE c.counselor_id = :counselor_id")
         .setParameter("counselor_id", counselor_idfk);
      List<Counselor> resultC = queryC.getResultList();
      if (resultC.size() == 1) {
          existIn=true;
         Counselor c = resultC.get(0);
         first_name=c.getFirst_name();
         last_name=c.getLast_name();
         telephone=c.getTelephone();
         email=c.getEmail();
         member_since=c.getMember_since();
      }
   }

   public void insert() {
       Counselor c = new Counselor();
           c.setFirst_name(first_name);
           c.setLast_name(last_name);
           c.setEmail(email);
           c.setTelephone(telephone);
           c.setMember_since(member_since);
           em.persist(c);
           addOK = true;
   }
}


