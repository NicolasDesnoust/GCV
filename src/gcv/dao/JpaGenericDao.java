package gcv.dao;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;

@Stateless(name = "genericDao", description = "Méthodes CRUD réutilisables.")
public class JpaGenericDao implements GenericDao {

	@PersistenceContext(unitName = "GCV_MySQL")
	EntityManager em;

	private final int batchSize = 25;

	@PostConstruct
	public void start() {
		System.out.println("Starting " + this);
	}

	@PreDestroy
	public void stop() {
		System.out.println("Stopping " + this);
	}

	@Override
	public <T> T read(Class<T> clazz, Object id) {
		T entity = em.find(clazz, id);
		if (entity == null)
			System.err.println("Entity not found.");
		else
			System.err.println("Entity found.");

		return entity;
	}

	@Override
	public <T> Collection<T> readAll(Class<T> clazz) {
		// Récupère une instance de la classe CriteriaBuilder
		CriteriaBuilder cb = em.getCriteriaBuilder();
		// Definition d'un nouveau type de requete
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		// clause FROM de la requete
		Root<T> root = cq.from(clazz);
		// clause SELECT de la requete
		cq.select(root);

		// Preparation de la requete pour execution
		TypedQuery<T> q = em.createQuery(cq);
		// Execution de la requete
		List<T> resultList = q.getResultList();

		if (resultList.isEmpty())
			System.err.println("Entities from " + clazz.getName() + " not found.");
		else
			System.err.println("Entities from " + clazz.getName() + " found.");

		return resultList;
	}

	@Override
	public <T> T create(T entity) {
		em.persist(entity);
		System.err.println("Entity added.");
		return (entity);
	}

	@Override
	public <T> void createAll(T[] entity) {
		int compt = 0;
		for (int i = 0; i < entity.length; i++) {
			em.persist(entity[i]);
			compt++;
			
			if (compt % batchSize == 0) {
				// Flush a batch of inserts and release memory.
				em.flush();
				em.clear();
			}
		}
		System.err.println("Entities added.");
	}

	@Override
	public <T> T update(T entity) {
		entity = em.merge(entity);
		System.err.println("Entity updated.");
		return entity;
	}

	@Override
	public <T> void remove(Class<T> clazz, Object id) {
		T entity = em.find(clazz, id);
		if (entity != null) {
			em.remove(entity);
		}
		System.err.println("Entity removed.");
	}

	@Override
	public <T> Collection<T> findByStringProperty(Class<T> clazz, String propertyName, String propertyValue) {
		// Récupère une instance de la classe CriteriaBuilder
		CriteriaBuilder cb = em.getCriteriaBuilder();
		// Definition d'un nouveau type de requete
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		// clause FROM de la requete
		Root<T> root = cq.from(clazz);

		// Recuperation du metamodel
		Metamodel m = em.getMetamodel();
		// Recuperation de l'attribut id du groupe
		EntityType<T> Person_ = m.entity(clazz);
		SingularAttribute<T, String> propertyValueMeta = Person_.getDeclaredSingularAttribute(propertyName,
				String.class);

		// clause WHERE de la requete
		cq.where(cb.like(root.get(propertyValueMeta), propertyValue));

		// Preparation de la requete pour execution
		TypedQuery<T> q = em.createQuery(cq);
		// Execution de la requete
		List<T> resultList = q.getResultList();

		if (resultList.isEmpty())
			System.err.println("No " + clazz.getName() + " found.");
		else
			System.err.println(resultList.size() + " " + clazz.getName() + " found.");

		return resultList;
	}
}