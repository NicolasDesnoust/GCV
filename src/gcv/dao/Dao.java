package gcv.dao;

import java.util.Collection;

import javax.ejb.Local;

import gcv.beans.Person;

@Local
public interface Dao extends GenericDao {

	Collection<Person> findAllPersonsWithActivityEntitled(String title);

}
