package gcv.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.naming.NamingException;

import org.primefaces.component.datagrid.DataGrid;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import gcv.beans.Activity;
import gcv.beans.Nature;
import gcv.beans.Person;
import gcv.dao.Dao;
import gcv.util.LazyPersonDataModel;
import gcv.util.SortActivityByYearDesc;

@ManagedBean(name = "personController")
@SessionScoped
public class PersonController {

	@EJB(beanName = "jpadao")
	private Dao dao;

	private Person thePerson = new Person();
	private String firstName = "", lastName = "", activityTitle = "";
	
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public List<String> getLangs() {
		return langs;
	}

	public void setLangs(List<String> langs) {
		this.langs = langs;
	}

	private String lang; 
    private List<String> langs;  

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getActivityTitle() {
		return activityTitle;
	}

	public void setActivityTitle(String activityTitle) {
		this.activityTitle = activityTitle;
	}

	public void setThePerson(Person thePerson) {
		this.thePerson = thePerson;
	}

	public Person getThePerson() {
		return thePerson;
	}

	private LazyDataModel<Person> lazyModel;

	public LazyDataModel<Person> getLazyModel() {
		return lazyModel;
	}

	@PostConstruct
	public void init() {
		System.out.println("Create " + this);
		
		langs = new ArrayList<String>();
        langs.add("Français");
        langs.add("Anglais");
		
		lazyModel = new LazyPersonDataModel(dao, firstName, lastName, activityTitle);
		// personsCount = dao.getRowsCount(Person.class);

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

	public Collection<Person> getPersonsByFirstname(String input) {
		System.out.println(input);
		Collection<Person> persons = dao.findByStringProperty(Person.class, "firstName", "%" + "input" + "%");

		return persons;
	}

	public List<Activity> getLastActivities(Person person, int size) {
		ArrayList<Activity> cv;

		cv = new ArrayList<Activity>(person.getCv());
		Collections.sort(cv, new SortActivityByYearDesc());

		if (cv.size() < size) {
			return cv;
		}

		return cv.subList(0, size - 1);
	}

	public int getActivitiesCount(Person person) {
		if (person == null) {
			System.out.println("personne null");
			return 0;
		}
		if (person.getCv() == null) {
			System.out.println("cv null");
			return 0;
		}
		return person.getCv().size();
	}
	
	public void applyFilters() {
		lazyModel = new LazyPersonDataModel(dao, firstName, lastName, activityTitle);
		
		DataGrid dataGrid = (DataGrid)  FacesContext.getCurrentInstance().getViewRoot().findComponent("form:personsGrid");
		dataGrid.loadLazyData();
		dataGrid.setFirst(0);
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
	 * public String newCourse() { theCourse = new Course(); return
	 * "editCourse?faces-redirect=true"; }
	 */
}