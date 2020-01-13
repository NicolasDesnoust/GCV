package gcv.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
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
@SessionScoped
public class ActivityController implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB(beanName = "jpadao")
	private Dao dao;

	private Activity selectedActivity = new Activity();

	public Activity getSelectedActivity() {
		return selectedActivity;
	}

	public void setSelectedActivity(Activity selectedActivity) {
		this.selectedActivity = selectedActivity;
	}

	private LazyDataModel<Activity> lazyModel;

	private Collection<Activity> activities;

	@ManagedProperty(value = "#{personController}")
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
		} else {
			activities = new ArrayList<Activity>();
		}
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
		System.out.println(person.getCv().size() + " activities");
		return person.getCv().size();
	}
	
	public int getActivitiesCount(Collection<Activity> activities, String nature) {
		int compt = 0;
		System.out.println(nature);
		
		if (activities == null)
			return 0;
		
		Iterator<Activity> it = activities.iterator();
		
		while(it.hasNext()) {
			if (it.next().getNature().toString().equals(nature)) {
				compt++;
			}
		}

		return compt;
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
	 * Methode editActivity : Fonction de modification des informations d'une
	 * activité
	 * 
	 * @return la page showActivity.
	 */
	public String editActivity() {
		dao.update(Activity.class);
		return "show-activity";
	}
	

	/**
	 * Methode showCreateActivity : Fonction dd'accès à la vue de création
	 * d'activités.
	 * 
	 * @return la page createActivity.
	 */
	public String showCreateActivity() {
		selectedActivity = new Activity();
		
		return "create-activity";
	}

	/**
	 * Methode createActivity : Fonction de modification des informations d'une
	 * activité
	 * 
	 * @return la page showActivity.
	 */
	public String createActivity(Activity activity, Person owner) {
		activity.setOwner(owner);
		dao.create(activity);
		
		
		
		return personController.showPerson(owner);
	}
	
	/**
	 * Methode showEditActivity : Controleur de la page d'édition des informations
	 * d'une activité.
	 * 
	 * @return la page editActivity.
	 */
	public String showEditActivity(Activity activity) {
		selectedActivity = activity;
		return "edit-activity";
	}

	public Collection<Activity> getActivities() {
		Person selectedPerson = personController.getSelectedPerson();

		if (selectedPerson != null) {
			return selectedPerson.getCv();
		}
		return new ArrayList<Activity>();

	}
	
	/**
	 * Methode updateActivity : Fonction de modification des informations d'une activity
	 * 
	 * @return la page updateActivity.
	 */
	public String updateActivity(Activity activity) {
		dao.update(activity);
		
		return personController.showPerson(activity.getOwner());
	}
	
	/**
	 * Methode deleteActivity : Fonction de suppression d'une activity
	 * 
	 */
	public void deleteActivity(Activity activity) {
		System.out.println(activity.getActivityID());
		dao.remove(Activity.class, activity.getActivityID());
	}
	
	
}