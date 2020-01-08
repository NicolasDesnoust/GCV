package gcv.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.naming.NamingException;

import gcv.beans.Person;
import gcv.dao.Dao;

@ManagedBean(name = "personController")
@SessionScoped
public class PersonController {

	@EJB(beanName = "jpadao")
	Dao dao;
	
	Person thePerson = new Person();

	public void setThePerson(Person thePerson) {
		this.thePerson = thePerson;
	}

	public Person getThePerson() {
		return thePerson;
	}

	@PostConstruct
	public void init() {
		System.out.println("Create " + this);

		/*
		 * persons = dao.readAll(Person.class);
		 * 
		 * System.out.println("Persons:"); for (Person p : persons) {
		 * System.out.println(p); }
		 */
		// Nous injectons le test dans le container pour que
		// l'annotation @EJB soit traitée
		// EJBContainer.createEJBContainer().getContext().bind("inject", this);
	}

	public Collection<Person> getPersons() {
		Collection<Person> persons = dao.findByStringProperty(Person.class, "lastName", "%NOUS%");
		return persons;
	}

	public String showPersons() {
		System.out.println("Redirecting to showPersons page...");
		return "showPersons";
	}
	
	public String showPerson(Person person) {
		thePerson = person;
	
		System.out.println("Redirecting to showPerson page...");
		return "showPerson";
	}
/*
	public String newCourse() {
		theCourse = new Course();
		return "editCourse?faces-redirect=true";
	}*/
}