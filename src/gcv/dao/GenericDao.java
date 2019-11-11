package gcv.dao;

import java.util.Collection;

import javax.ejb.Local;

import gcv.beans.Person;

@Local
public interface GenericDao {

	<T> T read(Class<T> clazz, Object id);
	
	<T> Collection<T> readAll(Class<T> clazz);

	<T> T create(T entity);

	<T> T update(T entity);

	<T> void remove(Class<T> clazz, Object id);

	<T> Collection<T> findByStringProperty(Class<T> clazz, String propertyName, String propertyValue);
}
