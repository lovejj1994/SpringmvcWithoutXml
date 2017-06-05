package spring.springmvc01.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RedisPan implements Serializable{

	int id;
	
	String name;

	String passWord;

	int enabled;

	public RedisPan() {
	}

	public RedisPan(int id,String name, String passWord, int enabled) {
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
