package spring.springmvc01.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.springmvc01.jpaspringdata.JpaSpringDataHibernatePan;

public interface JpaSpringDataHibernatePanRepository extends JpaRepository<JpaSpringDataHibernatePan, Integer>
															,JpaSpringDataHibernatePanRepositoryExtender{
	JpaSpringDataHibernatePan findByNameOrId(String name,int id);
}
