package gcv.business;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import gcv.beans.Activity;
import gcv.beans.Person;
import gcv.dao.ISpecificDao;

@Stateless(name = "access")
/* EJB stateless qui offre des fonctions d'accès et de recherche. */
public class Access implements IAccess {

	@EJB(beanName = "specificDao")
	ISpecificDao dao;
	
	@Override
	public Collection<Person> findAllPersonsByFirstName(String input) {
		return dao.findByStringProperty(Person.class, "firstName", "%" + input + "%");
	}

	@Override
	public Collection<Person> findAllPersonsByLastName(String input) {
		return dao.findByStringProperty(Person.class, "lastName", "%" + input + "%");
	}

	@Override
	public Collection<Person> findAllPersonsByActivity(String input) {
		return dao.findAllPersonsWithActivityEntitled("%" + input + "%");
	}

	@Override
	public Person readPerson(Person person) {
		return dao.read(Person.class, person);
	}

	@Override
	public Activity readActivity(Activity activity) {
		return dao.read(Activity.class, activity);
	}

	@Override
	public Collection<Person> readAllPersons() {
		return dao.readAll(Person.class);
	}

	@Override
	public Collection<Activity> readAllActivities() {
		return dao.readAll(Activity.class);
	}
}
