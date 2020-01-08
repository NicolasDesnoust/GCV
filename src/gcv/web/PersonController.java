package gcv.web;

import java.util.ArrayList;
import java.util.Collection;
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
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import gcv.beans.Person;
import gcv.dao.Dao;

@ManagedBean(name = "personController")
@SessionScoped
public class PersonController {

	@EJB(beanName = "jpadao")
	private Dao dao;
	
	private Person thePerson = new Person();
	
	private final static int PERSONS_PER_PAGE = 12;
	private final static int MAX_PAGES = 5;
	
	private int start = 0;
	
	private int personsCount = 0;

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
		lazyModel = new LazyDataModel<Person>() {
	        private static final long serialVersionUID = 1L;

	        @Override
	        public List<Person> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
	        	List<Person> persons = dao.readAllBetween(Person.class, first, pageSize);//sorting and filtering will not be used
	            lazyModel.setRowCount(dao.getRowsCount(Person.class));
	            return persons;
	        }

	    };
		//personsCount = dao.getRowsCount(Person.class);
		
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
		Collection<Person> persons = dao.readAllBetween(Person.class, start, start + MAX_PAGES * PERSONS_PER_PAGE);
		
	
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