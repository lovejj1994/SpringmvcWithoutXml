package spring.springmvc01.repository;

import spring.springmvc01.bean.HibernatePan;

public interface HibernatePanRepository {

	void save(HibernatePan home);

	HibernatePan findHomeByUserName(int id);
}
