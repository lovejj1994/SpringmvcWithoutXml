package spring.springmvc01.jpahibernate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "jpa_hibernate_pan")
public class JpaHibernatePan {

	@Id
	int id;

	String name;

	String passWord;

	int enabled;

	public JpaHibernatePan() {
	}

	public JpaHibernatePan(int id, String name, String passWord, int enabled) {
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
