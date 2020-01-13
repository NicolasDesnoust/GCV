package gcv.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
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
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import gcv.beans.Activity;
import gcv.beans.Nature;
import gcv.beans.Person;
import gcv.dao.Dao;
import gcv.util.LazyActivityDataModel;
import gcv.util.LazyPersonDataModel;
import gcv.util.SortActivityByYearDesc;

@ManagedBean(name = "activityController")
@ViewScoped
public class ActivityController {

	@EJB(beanName = "jpadao")
	private Dao dao;

	private Activity selectedActivity;
	
	public Activity getSelectedActivity() {
		return selectedActivity;
	}

	public void setSelectedActivity(Activity selectedActivity) {
		this.selectedActivity = selectedActivity;
	}

	private LazyDataModel<Activity> lazyModel;

	private Collection<Activity> activities;
	
	@ManagedProperty(value="#{personController}")
	private PersonController personController;
	
	public void setPersonController(PersonController personController) {
		this.personController = personController;
	}

	public LazyDataModel<Activity> getLazyModel() {
		return lazyModel;
	}

	@PostConstruct
	public void init() {
		System.out.println("Create " + this);

		lazyModel = new LazyActivityDataModel(dao);
	
		Person selectedPerson = personController.getSelectedPerson();
		
		if (selectedPerson != null) {
			activities = selectedPerson.getCv(); 
		}
		activities = new ArrayList<Activity>(); 
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
		System.out.println(person.getCv().size()+" activities");
		return person.getCv().size();
	}

	public void applyFilters() {
		lazyModel = new LazyActivityDataModel(dao);
		// Remplacer la datagrid par le composant qui va contenir les activités
		// DataGrid dataGrid = (DataGrid)
		// FacesContext.getCurrentInstance().getViewRoot().findComponent("form:personsGrid");
		// dataGrid.loadLazyData();
		// dataGrid.setFirst(0);
	}

	/**
	 * Methode showActivity : Fonction d'affichage d'une personne et ses activités
	 * 
	 * @param person
	 * @return la page showActivity.
	 */
	public String showActivity(Person personActivity) {

		System.out.println("Redirecting to showActivity page...");
		return "showActivity";
	}
	
	/**
	 * Methode showActivities : Fonction d'affichage de tous les personnes
	 * 
	 * @return la page showActivities
	 */
	public String showActivities() {
		System.out.println("Redirecting to showActivities page...");
		return "showActivities";
	}


	/**
	 * Methode editActivity : Fonction de modification des informations d'une
	 * activité
	 * 
	 * @return la page showActivity.
	 */
	public String editActivity() {
		dao.update(Activity.class);
		return "showActivity";
	}

	/**
	 * Methode createActivity : Fonction de modification des informations d'une
	 * activité
	 * 
	 * @return la page showActivity.
	 */
	public String createActivity() {
		dao.create(Activity.class);
		return "showActivity";
	}

	public Collection<Activity> getActivities() {
		return activities;
	}

}