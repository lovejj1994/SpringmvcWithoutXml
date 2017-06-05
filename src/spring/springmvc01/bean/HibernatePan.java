package spring.springmvc01.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Table;

@Entity(name = "hibernate_pan")
@Table(appliesTo = "hibernate_pan")
public class HibernatePan {

	@Id
	int id;

	String name;

	String passWord;

	int enabled;

	public HibernatePan() {
	}

	public HibernatePan(int id, String name, String passWord, int enabled) {
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
