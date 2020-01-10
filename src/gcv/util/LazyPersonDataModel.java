package gcv.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import gcv.beans.Activity;
import gcv.beans.Person;
import gcv.dao.Dao;

public class LazyPersonDataModel extends LazyDataModel<Person> {

	private Dao dao;
	
	private static final long serialVersionUID = 1L;
	private String firstName, lastName, activityTitle;

	public LazyPersonDataModel(Dao dao, String firstName, String lastName, String activityTitle) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.activityTitle = activityTitle;
		this.dao = dao;
	}

	@Override
	public List<Person> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		if(dao == null)
			System.out.println("nulllll");
		List<Person> persons = dao.readAllBetweenWithFilters(first, pageSize, firstName, lastName, activityTitle);
		setRowCount(dao.getRowsCount(firstName, lastName, activityTitle));
		
		return persons;
	}
}
