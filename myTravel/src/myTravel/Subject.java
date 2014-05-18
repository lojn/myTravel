
package myTravel;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Subject implements Serializable{
   @Id
   private int subject_id;
   private String subjectname;
   private String description;
   private int	counselor_idfk;

   public Subject() {} // Required by JPA
   public Subject(int id,String name,String des,int idfk){
	   this.subject_id=id;
	   this.subjectname=name;
	   this.description=des;
	   this.counselor_idfk=idfk;
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
}
