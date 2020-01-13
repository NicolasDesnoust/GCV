package gcv.dao;

import java.util.ArrayList;
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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;

/**
 * Implémentation de la partie générique de la couche DAO.
 * 
 * Bean Stateless implémentant les spécifications génériques de la couche DAO.
 * Cet EJB est séparé de la partie non-générique pour être réutilisable dans des
 * projets ayant une architecture similaire.
 * 
 * @author Nicolas DESNOUST
 * @author Serigne Bassirou Mbacké LY
 * 
 * @see gcv.dao.GenericDao
 */
@Stateless(name = "genericDao", description = "Méthodes CRUD réutilisables.")
public class JpaGenericDao implements GenericDao {

	/**
	 * Interface utilisée pour intéragir avec le contexte de persistence.
	 */
	@PersistenceContext(unitName = "GCV_MySQL")
	protected EntityManager em;

	/**
	 * Constante définissant la taille d'un regroupement de requêtes.
	 * 
	 * @see gcv.dao.JpaGenericDao#createAll(Object[])
	 */
	private final int BATCH_SIZE = 25;

	/**
	 * Méthode exécutée au démarrage du bean.
	 */
	@PostConstruct
	public void start() {
		System.out.println("Starting " + this);
	}

	/**
	 * Méthode exécutée avant la destruction du bean.
	 */
	@PreDestroy
	public void stop() {
		System.out.println("Stopping " + this);
	}

	/**
	 * Méthode générique de recherche d'entité dans une base de données. Retourne
	 * une entité de la classe et l'identifiant passés en paramètres si elle existe
	 * dans la base de données. Retourne null sinon.
	 * 
	 * @param clazz La classe de l'entité à trouver.
	 * @param id    L'identifiant unique de l'entité au sein de la table.
	 * @return L'entité trouvée ou null.
	 */
	@Override
	public <T> T read(Class<T> clazz, Object id) {
		T entity = em.find(clazz, id);
		if (entity == null)
			System.err.println("Entity not found.");
		else
			System.err.println("Entity found.");

		return entity;
	}

	/**
	 * Méthode générique de recherche d'entités dans une base de données. Retourne
	 * toutes les entités de la classe passée en paramètre depuis la base de
	 * données.
	 * 
	 * @param clazz La classe des entités à récupérer.
	 * @return La collection d'entités.
	 */
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
	public <T> int getRowsCount(Class<T> clazz) {
		// Récupère une instance de la classe CriteriaBuilder
		CriteriaBuilder cb = em.getCriteriaBuilder();
		// Definition d'un nouveau type de requete
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		// clause FROM de la requete
		Root<T> root = cq.from(clazz);

		// Recuperation du metamodel
		Metamodel m = em.getMetamodel();
		EntityType<T> Clazz_ = m.entity(clazz);

		// clause SELECT de la requete
		cq.select(cb.count(root));

		// Preparation de la requete pour execution
		TypedQuery<Long> q = em.createQuery(cq);
		// Execution de la requete
		int result = q.getSingleResult().intValue();

		System.err.println(result + " entities found of " + clazz.getName() + " class.");

		return result;
	}

	/**
	 * Méthode générique de création d'entité dans une base de données. Fait
	 * persister l'entité passée en paramètre dans la base de données.
	 * 
	 * @param entity L'entité à faire persister.
	 */
	@Override
	public <T> void create(T entity) {
		em.persist(entity);
		System.err.println("Entity added.");
	}

	/**
	 * Méthode générique de création d'entités dans une base de données. Fait
	 * persister le tableau d'entités passé en paramètre dans la base de données.
	 * Les entités sont regroupées par lots de taille BATCH_SIZE pour optimiser
	 * l'insertion.
	 * 
	 * @param entity L'entité à faire persister.
	 */
	@Override
	public <T> void createAll(T[] entity) {

		for (int i = 0; i < entity.length; i++) {
			em.persist(entity[i]);

			if ((i + 1) % BATCH_SIZE == 0) {
				// Flush a batch of inserts and release memory.
				em.flush();
				em.clear();
			}
		}
		System.err.println(entity.length + " entities added.");
	}

	/**
	 * Méthode générique de mise-à-jour d'entité dans une base de données. Fusionne
	 * la version de l'entité passée en paramètre avec celle présente dans la base
	 * de données.
	 * 
	 * @param entity L'entité à mettre à jour.
	 * @return L'entité résultante de la fusion.
	 */
	@Override
	public <T> T update(T entity) {
		entity = em.merge(entity);
		System.err.println("Entity updated.");
		return entity;
	}

	/**
	 * Méthode générique de suppression d'entité dans une base de données. Recherche
	 * l'entité décrite par les paramètres dans la base de données et la supprime si
	 * elle existe.
	 * 
	 * @param clazz La classe de l'entité à supprimer.
	 * @param id    L'identifiant unique de l'entité à supprimer.
	 */
	@Override
	public <T> void remove(Class<T> clazz, Object id) {
		T entity = em.find(clazz, id);
		if (entity != null) {
			System.err.println("Entity removed.");
			em.remove(entity);
		}
	}

	/**
	 * Méthode générique de recherche d'entités dans une base de données (via un
	 * attribut). Retourne une collection d'entités de la classe <code>clazz</code>
	 * et qui ont un attribut <code>propertyName<code>
	 * similaire à <code>propertyValue</code>. Cet attribut est de type
	 * <code>String</code>.
	 * 
	 * Exemple d'utilisation Pour rechercher toutes les personnes qui ont un nom de
	 * famille contenant "SNOUS":
	 * <code>findByStringProperty(Person.class, "lastName", "%SNOUS%");</code>
	 * 
	 * @param clazz         La classe des entités à rechercher.
	 * @param propertyName  Le nom de l'attribut des entités.
	 * @param propertyValue La valeur de la propriété (critère de recherche).
	 * @return La collection d'entités correspondant aux critères de recherche.
	 */
	@Override
	public <T> Collection<T> findByStringProperty(Class<T> clazz, String propertyName, String propertyValue, int limit) {
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
		if (limit != -1) {
			q.setMaxResults(limit);
		}
		// Execution de la requete
		List<T> resultList = q.getResultList();

		if (resultList.isEmpty())
			System.err.println("No " + clazz.getName() + " found.");
		else
			System.err.println(resultList.size() + " " + clazz.getName() + " found.");

		return resultList;
	}
		
	@Override
	public <T> List<T> readAllBetween(Class<T> clazz, int start, int maxResults) {
		// Récupère une instance de la classe CriteriaBuilder
		CriteriaBuilder cb = em.getCriteriaBuilder();
		// Definition d'un nouveau type de requete
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		// clause FROM de la requete
		Root<T> root = cq.from(clazz);
		
		// Recuperation du metamodel
		Metamodel m = em.getMetamodel();
		EntityType<T> Clazz_ = m.entity(clazz);
		
		// clause SELECT de la requete
		cq.select(root);
				
		// Preparation de la requete pour execution
		TypedQuery<T> q = em.createQuery(cq);
		
		q.setFirstResult(start);
		q.setMaxResults(maxResults);

		// Execution de la requete
		List<T> resultList = q.getResultList();

		if (resultList.isEmpty())
			System.err.println("Entities from " + clazz.getName() + " not found.");
		else
			System.err.println("Entities from " + clazz.getName() + " found.");

		return resultList;
	}
}