package spring.springmvc01.repository;

import java.util.List;

import spring.springmvc01.bean.Pan;

public interface PanRepository {

	void save(Pan home);

	Pan findHomeByUserName(String username);

	List<String> findRolesByUserName(String username);
}
