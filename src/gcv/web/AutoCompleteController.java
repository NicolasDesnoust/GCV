package gcv.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import gcv.beans.Activity;
import gcv.beans.Person;
import gcv.dao.Dao;

@Named
@RequestScoped
public class AutoCompleteController {

	@EJB(beanName = "jpadao")
	private Dao dao;
	
	private final int AUTOCOMPLETE_LIMIT = 5;

	public List<String> completeLastName(String query) {
		List<String> lastNames = new ArrayList<String>();
		
    	System.err.println("completing last name...");
        Collection<Person> results = dao.findByStringProperty(Person.class, "lastName", "%" + query + "%", AUTOCOMPLETE_LIMIT);
        
        for(Person person : results) {
        	lastNames.add(person.getLastName());
        }
         
        return lastNames;
    }
	
	public List<String> completeFirstName(String query) {
		List<String> firstNames = new ArrayList<String>();
		
    	System.err.println("completing first name...");
        Collection<Person> results = dao.findByStringProperty(Person.class, "firstName", "%" + query + "%", AUTOCOMPLETE_LIMIT);
        
        for(Person person : results) {
        	firstNames.add(person.getFirstName());
        }
         
        return firstNames;
    }
	
	public List<String> completeActivitiesTitles(String query) {
		List<String> titles = new ArrayList<String>();
		
    	System.err.println("completing first name...");
        Collection<Activity> results = dao.findByStringProperty(Activity.class, "title", "%" + query + "%", AUTOCOMPLETE_LIMIT);
        
        for(Activity activity : results) {
        	titles.add(activity.getTitle());
        }
         
        return titles;
    }
}