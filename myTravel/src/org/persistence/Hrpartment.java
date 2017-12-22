package org.persistence;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "Hrpartment")
@TableGenerator(name = "hrpartment")
public class Hrpartment implements Serializable {

	private static final long serialVersionUID = 1L;

	public Hrpartment() {
	}

	@Id
	private long id;
	@ElementCollection
	private Collection<String> name;
	private String email;
	private String address;
	private String homeid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Collection<String> getName() {
		return name;
	}

	public void setName(Collection<String> param) {
		this.name = param;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String param) {
		this.email = param;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String param) {
		this.address = param;
	}

	public String getHomeid() {
		return homeid;
	}

	public void setHomeid(String param) {
		this.homeid = param;
	}

}