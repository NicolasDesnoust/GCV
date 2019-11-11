package gcv.dao;

import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;

import gcv.beans.Activity;
import gcv.beans.Person;

@Stateless(name = "jpadao", description = "Méthodes CRUD pour les beans du projet.")
public class JpaDao extends JpaGenericDao implements Dao {
		// Récupère les personnes liées au titre d'une activité.
		// TODO: verifier si on a pas deux fois la meme personne si elle contient
		// deux activités qui matchent la recherche
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
			
			//TODO : A commenter si on ne souhaite pas avoir plusieurs fois une même personne
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
}
