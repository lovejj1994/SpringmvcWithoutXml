package spring.springmvc01.jpaspringdata;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "jpa_springdata_hibernate_pan")
public class JpaSpringDataHibernatePan implements Serializable{

	@Id
	int id;

	String name;

	String passWord;

	int enabled;

	public JpaSpringDataHibernatePan() {
	}

	public JpaSpringDataHibernatePan(int id, String name, String passWord, int enabled) {
		super();
		this.id = id;
		this.name = name;
		this.passWord = passWord;
		this.enabled = enabled;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
