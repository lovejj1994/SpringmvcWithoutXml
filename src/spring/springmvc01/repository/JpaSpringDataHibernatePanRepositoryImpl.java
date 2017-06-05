package spring.springmvc01.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import spring.springmvc01.jpaspringdata.JpaSpringDataHibernatePan;

public class JpaSpringDataHibernatePanRepositoryImpl implements JpaSpringDataHibernatePanRepositoryExtender{

	//注入entityManager
	@PersistenceContext
	private EntityManager em;    
	
	@Override
	public List<JpaSpringDataHibernatePan> query(String name,int id){
		StringBuffer query = new StringBuffer();
		query.append("FROM JpaSpringDataHibernatePan pan ");
		
		if(id==-1 && !"".equals(name)){
			query.append(" WHERE pan.name=:name");
		}else if(id!=-1 && "".equals(name)){
			query.append(" WHERE pan.id=:id");
		}else{
			query.append(" WHERE pan.id=:id AND pan.name=:name");
		}
		
		Query createQuery = em.createQuery(query.toString());
		
		if(query.indexOf(":id")>0){
			createQuery.setParameter("id", id);
		}
		
		if(query.indexOf(":name")>0){
			createQuery.setParameter("name", name);
		}
		
		List<JpaSpringDataHibernatePan> resultList = createQuery.getResultList();
		return resultList;
	}

}
