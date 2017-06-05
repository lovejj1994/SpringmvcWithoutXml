package spring.springmvc01.bean;

import java.io.Serializable;

public class Pan implements Serializable{
	String name;
	
	String passWord;
	
	int enabled;
	
	public Pan() {
	}

	public Pan(String name, String passWord, int enabled) {
		this.name = name;
		this.passWord = passWord;
		this.enabled = enabled;
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
