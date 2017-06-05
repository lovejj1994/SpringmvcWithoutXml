package spring.springmvc01.repository;

import spring.springmvc01.jpahibernate.JpaHibernatePan;

public interface JpaHibernatePanRepository {

	void save(JpaHibernatePan home);

	JpaHibernatePan findHomeByUserName(int id);
}
