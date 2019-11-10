package gcv.business;

import java.util.Collection;

import javax.ejb.Local;

import gcv.beans.Activity;
import gcv.beans.Person;

@Local
/* Interface d'EJB stateless qui offre des fonctions d'accès et de recherche. */
public interface IAccess {

	/*********************************************************
	******   Opérations sans authentification requise	******
	*********************************************************/
	
	/* Lectures spécifiques */
	Collection<Person> findAllPersonsByFirstName(String input);
	Collection<Person> findAllPersonsByLastName(String input);
	Collection<Person> findAllPersonsByActivity(String input);
	
	/* Lectures standard */
	Person readPerson(Person person);
	Activity readActivity(Activity activity);
	Collection<Person> readAllPersons();
	Collection<Activity> readAllActivities();
}