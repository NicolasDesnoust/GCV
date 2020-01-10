package gcv.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;

import gcv.beans.Activity;
import gcv.beans.Person;

/**
 * Implémentation de la partie non-générique de la couche DAO.
 * 
 * Bean Stateless implémentant les spécifications de la couche DAO et héritant
 * des fonctions génériques.
 * 
 * @author Nicolas DESNOUST
 * @author Serigne Bassirou Mbacké LY
 * 
 * @see gcv.dao.Dao
 */
@Stateless(name = "jpadao", description = "Méthodes CRUD pour les beans du projet.")
public class JpaDao extends JpaGenericDao implements Dao {
	/**
	 * Récupère une collection de personnes à partir du titre d'une de leurs
	 * activités. Cette méthode recherche dans la base de données toutes les
	 * activités ayant un titre similaire à celui passé en paramètre et retourne les
	 * personnes propriétaires de ces activités. La collection ne peut contenir deux
	 * fois la mëme personne.
	 * 
	 * @param title Le titre complet ou partiel d'activités à trouver.
	 * @return La collection des personnes correspondant à la recherche.
	 */
	@Override
	public Collection<Person> findAllPersonsWithActivityEntitled(String title) {
		// Récupère une instance de la classe CriteriaBuilder
		CriteriaBuilder cb = em.getCriteriaBuilder();
		// Definition d'un nouveau type de requete
		CriteriaQuery<Person> cq = cb.createQuery(Person.class);
		// clause FROM de la requete
		Root<Person> root = cq.from(Person.class);

		// Recuperation du metamodel
		Metamodel m = em.getMetamodel();
		// Recuperation de l'attribut cv de Person
		EntityType<Person> Person_ = m.entity(Person.class);
		SetAttribute<Person, Activity> cvMeta = Person_.getDeclaredSet("cv", Activity.class);

		// navigation vers l'entite Person
		Join<Person, Activity> join = root.join(cvMeta);

		// Recuperation de l'attribut title de activity
		EntityType<Activity> Activity_ = m.entity(Activity.class);
		SingularAttribute<Activity, String> titleMeta = Activity_.getDeclaredSingularAttribute("title", String.class);

		// clause WHERE de la requete
		cq.where(cb.like(join.get(titleMeta), title));

		cq.distinct(true);

		// Preparation de la requete pour execution
		TypedQuery<Person> q = em.createQuery(cq);
		// Execution de la requete
		List<Person> resultList = q.getResultList();

		if (resultList.isEmpty())
			System.err.println("No persons found.");
		else
			System.err.println(resultList.size() + " persons found.");

		return resultList;
	}

	/**
	 * Recherche d'une personne dans une base de données via son email. L'email est
	 * supposé unique.
	 * 
	 * @param mail L'email unique de la personne à rechercher.
	 * @return L'entité trouvée ou null.
	 */
	@Override
	public Person readPersonByMail(String mail) {
		// Récupère une instance de la classe CriteriaBuilder
		CriteriaBuilder cb = em.getCriteriaBuilder();
		// Definition d'un nouveau type de requete
		CriteriaQuery<Person> cq = cb.createQuery(Person.class);
		// clause FROM de la requete
		Root<Person> root = cq.from(Person.class);

		// Recuperation du metamodel
		Metamodel m = em.getMetamodel();
		// Recuperation de l'attribut id du groupe
		EntityType<Person> Person_ = m.entity(Person.class);
		SingularAttribute<Person, String> mailMeta = Person_.getDeclaredSingularAttribute("mail", String.class);

		// clause WHERE de la requete
		cq.where(cb.equal(root.get(mailMeta), mail));

		// Preparation de la requete pour execution
		TypedQuery<Person> q = em.createQuery(cq);
		// Execution de la requete
		List<Person> resultList = q.getResultList();

		if (resultList.isEmpty()) {
			System.err.println("No person found with specified mail.");
			return null;
		}

		System.err.println("A person was found with specified mail.");
		return resultList.get(0);
	}

	@Override
	public List<Person> readAllBetweenWithFilters(int start, int maxResults, String firstName,
			String lastName, String activityTitle) {
		final List<Predicate> predicates = new ArrayList<Predicate>();
		
		// Récupère une instance de la classe CriteriaBuilder
		CriteriaBuilder cb = em.getCriteriaBuilder();
		// Definition d'un nouveau type de requete
		CriteriaQuery<Person> cq = cb.createQuery(Person.class);
		// clause FROM de la requete
		Root<Person> root = cq.from(Person.class);

		// Recuperation du metamodel
		Metamodel m = em.getMetamodel();
		EntityType<Person> Person_ = m.entity(Person.class);
		
		if (! activityTitle.equals("")) {
			// Recuperation de l'attribut cv de Person
			SetAttribute<Person, Activity> cvMeta = Person_.getDeclaredSet("cv", Activity.class);
			// navigation vers l'entite Person
			Join<Person, Activity> join = root.join(cvMeta);
			// Recuperation de l'attribut title de activity
			EntityType<Activity> Activity_ = m.entity(Activity.class);
			SingularAttribute<Activity, String> titleMeta = Activity_.getDeclaredSingularAttribute("title", String.class);
			
			predicates.add(cb.like(join.get(titleMeta), "%" + activityTitle + "%"));
		}
		if (! firstName.equals("")) {
			SingularAttribute<Person, String> firstNameMeta = 
					Person_.getDeclaredSingularAttribute("firstName", String.class);
			
			predicates.add(cb.like(root.get(firstNameMeta), "%" + firstName + "%"));
		}
		
		if (! lastName.equals("")) {
			SingularAttribute<Person, String> lastNameMeta = 
					Person_.getDeclaredSingularAttribute("lastName", String.class);
			
			predicates.add(cb.like(root.get(lastNameMeta), "%" + lastName + "%"));
		}

		cq.distinct(true);

		// clause WHERE de la requete
		CriteriaQuery<Person> whereResult = cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

		// clause SELECT de la requete
		whereResult.select(root);

		// Preparation de la requete pour execution
		TypedQuery<Person> q = em.createQuery(cq);

		q.setFirstResult(start);
		q.setMaxResults(maxResults);

		// Execution de la requete
		List<Person> resultList = q.getResultList();

		if (resultList.isEmpty())
			System.err.println("Entities from " + Person.class.getName() + " not found.");
		else
			System.err.println("Entities from " + Person.class.getName() + " found.");

		return resultList;
	}
	
	@Override
	public int getRowsCount(String firstName,
			String lastName, String activityTitle) {
		final List<Predicate> predicates = new ArrayList<Predicate>();
		
		// Récupère une instance de la classe CriteriaBuilder
		CriteriaBuilder cb = em.getCriteriaBuilder();
		// Definition d'un nouveau type de requete
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		// clause FROM de la requete
		Root<Person> root = cq.from(Person.class);

		// Recuperation du metamodel
		Metamodel m = em.getMetamodel();
		EntityType<Person> Person_ = m.entity(Person.class);
		
		if (! activityTitle.equals("")) {
			// Recuperation de l'attribut cv de Person
			SetAttribute<Person, Activity> cvMeta = Person_.getDeclaredSet("cv", Activity.class);
			// navigation vers l'entite Person
			Join<Person, Activity> join = root.join(cvMeta);
			// Recuperation de l'attribut title de activity
			EntityType<Activity> Activity_ = m.entity(Activity.class);
			SingularAttribute<Activity, String> titleMeta = Activity_.getDeclaredSingularAttribute("title", String.class);
			
			predicates.add(cb.like(join.get(titleMeta), "%" + activityTitle + "%"));
		}
		if (! firstName.equals("")) {
			SingularAttribute<Person, String> firstNameMeta = 
					Person_.getDeclaredSingularAttribute("firstName", String.class);
			
			predicates.add(cb.like(root.get(firstNameMeta), "%" + firstName + "%"));
		}
		
		if (! lastName.equals("")) {
			SingularAttribute<Person, String> lastNameMeta = 
					Person_.getDeclaredSingularAttribute("lastName", String.class);
			
			predicates.add(cb.like(root.get(lastNameMeta), "%" + lastName + "%"));
		}

		cq.distinct(true);

		// clause WHERE de la requete
		CriteriaQuery<Long> whereResult = cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

		// clause SELECT de la requete
		whereResult.select(cb.count(root));

		// Preparation de la requete pour execution
		TypedQuery<Long> q = em.createQuery(cq);
		// Execution de la requete
		int result = q.getSingleResult().intValue();

		System.err.println(result + " entities found of " + Person.class.getName() + " class.");

		return result;
	}
}
