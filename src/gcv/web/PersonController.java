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
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.component.datagrid.DataGrid;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;
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

	private Person selectedPerson = null;
	private String firstName = "", lastName = "", activityTitle = "";
	
	public Person getSelectedPerson() {
		return selectedPerson;
	}

	public void setSelectedPerson(Person selectedPerson) {
		this.selectedPerson = selectedPerson;
	}

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

	private LazyDataModel<Person> lazyModel;

	public LazyDataModel<Person> getLazyModel() {
		return lazyModel;
	}

	@PostConstruct
	public void init() {
		System.out.println("Create " + this);
		
		lazyModel = new LazyPersonDataModel(dao, firstName, lastName, activityTitle);
	}
	
	public void applyFilters() {
		lazyModel = new LazyPersonDataModel(dao, firstName, lastName, activityTitle);
		/*FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext ec = context.getExternalContext();
		final HttpServletRequest request = (HttpServletRequest) ec.getRequest();
		request.getSession(false).setMaxInactiveInterval(30);
		System.out.printf("Session timeout defined at code level : %s\n", request.getSession(false).getMaxInactiveInterval());
		*/DataGrid dataGrid = (DataGrid)  FacesContext.getCurrentInstance().getViewRoot().findComponent("form:personsGrid");
		dataGrid.loadLazyData();
		dataGrid.setFirst(0);
	}
	

	/**
	 * Methode showPersons : Fonction d'affichage de tous les personnes
	 * 
	 * @return la page showPersons.
	 */
	public String showPersons() {
		System.out.println("Redirecting to showPersons page...");
		return "showPersons";
	}

	/**
	 * Methode showPerson : Fonction d'affichage d'une personne et ses activités
	 * 
	 * @param person
	 * @return la page showPerson.
	 */
	public String showPerson(Person person) {
		selectedPerson = person;

		System.out.println("Redirecting to showPerson page...");
		return "showPerson";
	}

	/**
	 * Methode editPerson : Fonction de modification des informations d'une personne
	 * 
	 * @return la page showPerson.
	 */
	public String editPerson() {
		dao.update(Person.class);
		return "showPerson";
	}
}