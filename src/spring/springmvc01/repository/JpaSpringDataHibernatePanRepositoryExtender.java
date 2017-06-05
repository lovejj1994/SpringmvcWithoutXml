package spring.springmvc01.repository;

import java.util.List;

import spring.springmvc01.jpaspringdata.JpaSpringDataHibernatePan;

//扩展springdata，让springdata可以用jpa的EntityManager,完成jpa不容易完成的功能，比如自定义编写sql语句
public interface JpaSpringDataHibernatePanRepositoryExtender{

	List<JpaSpringDataHibernatePan> query(String name, int id);
	
}
