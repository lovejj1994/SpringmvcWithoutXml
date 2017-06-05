package spring.springmvc01.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import spring.springmvc01.jpaspringdata.JpaSpringDataHibernatePan;
import spring.springmvc01.repository.JpaSpringDataHibernatePanRepository;

@Service
public class SpringDataJpaService implements BaseService{

	
	@Autowired
	private JpaSpringDataHibernatePanRepository jpaSpringDataHibernatePanRepository;

	
	@Transactional
	public void save(JpaSpringDataHibernatePan entities){
		jpaSpringDataHibernatePanRepository.save(entities);
	}
	
	@Transactional(readOnly=true)
	public JpaSpringDataHibernatePan findOne(int id,String name){
		return jpaSpringDataHibernatePanRepository.findByNameOrId(name, id);
	}
	@Transactional(readOnly=true)
	public JpaSpringDataHibernatePan findOne(int id){
		return jpaSpringDataHibernatePanRepository.findOne(id);
	}
	@Cacheable(value="JpaSpringDataHibernatePanCache",keyGenerator="mykeyGenerator")
	@Transactional(readOnly=true)
	//角色认证时如果前戳不是ROLE_会自动补上，所以数据库的角色名应该以ROLE_*为准
	@PreAuthorize("hasRole('ADMIN')")
	public List<JpaSpringDataHibernatePan> findOne(String name,int id){
		return jpaSpringDataHibernatePanRepository.query(name, id);
	}
}
