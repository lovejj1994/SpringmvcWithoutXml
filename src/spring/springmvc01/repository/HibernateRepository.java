package spring.springmvc01.repository;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import spring.springmvc01.bean.HibernatePan;

@Repository
public class HibernateRepository implements HibernatePanRepository {

	@Autowired
	private LocalSessionFactoryBean localSessionFactoryBean;

	private Session getCurrentSession() {
		return localSessionFactoryBean.getObject().getCurrentSession();
	}

	@Override
	@Transactional
	public void save(HibernatePan pan) {
		getCurrentSession().save(pan);
	}

	@Override
	@Transactional(readOnly=true)
	public HibernatePan findHomeByUserName(int id) {
		return (HibernatePan) getCurrentSession().get(HibernatePan.class, id);
	}
}
