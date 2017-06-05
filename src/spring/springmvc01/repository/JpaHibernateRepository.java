package spring.springmvc01.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import spring.springmvc01.jpahibernate.JpaHibernatePan;

@Repository
public class JpaHibernateRepository implements JpaHibernatePanRepository {

	//注入entityManager
	@PersistenceContext
	private EntityManager em;    

	
	@Transactional
	public void update(JpaHibernatePan pan) {
		em.merge(pan);
	}

	@Override
	@Transactional(readOnly=true)
	public JpaHibernatePan findHomeByUserName(int id) {
		return em.find(JpaHibernatePan.class, id);
	}

	@Override
	@Transactional
	public void save(JpaHibernatePan pan) {
		em.persist(pan);
	}
}
