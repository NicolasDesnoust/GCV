package gcv.dao;

import java.util.Collection;

import javax.ejb.Local;

import gcv.beans.Person;

@Local
public interface ISpecificDao extends IGenericDao {

	Collection<Person> findAllPersonsWithActivityEntitled(String title);

}
