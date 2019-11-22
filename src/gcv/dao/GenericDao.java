package gcv.dao;

import java.util.Collection;

import javax.ejb.Local;

import gcv.beans.Person;

/**Interface regroupant les prototypes des fonctions génériques de la couche DAO.
 * Cette interface se veut réutilisable et permet des opérations via JPA telles que la création,
 * la mise à jour, la suppression ou la lecture de beans.
 * 
 * 
 * @author Nicolas DESNOUST
 * @author Serigne Bassirou Mbacké LY
 * 
 * @see gcv.dao.Dao
 */
@Local
public interface GenericDao {
	
	/**
	 * Méthode générique de recherche d'entité dans une base de données.
	 * Retourne une entité de la classe et l'identifiant passés en paramètres si elle existe dans la base de données.
	 * Retourne null sinon.
	 * 
	 * @param clazz La classe de l'entité à trouver.
	 * @param id L'identifiant unique de l'entité au sein de la table.
	 * @return L'entité trouvée ou null.
	 */
	public <T> T read(Class<T> clazz, Object id);
	
	/**
	 * Méthode générique de recherche d'entités dans une base de données.
	 * Retourne toutes les entités de la classe passée en paramètre depuis la base de données.
	 * 
	 * @param clazz La classe des entités à récupérer.
	 * @return La collection d'entités.
	 */
	public <T> Collection<T> readAll(Class<T> clazz);

	/**
	 * Méthode générique de création d'entité dans une base de données.
	 * Fait persister l'entité passée en paramètre dans la base de données.
	 * 
	 * @param entity L'entité à faire persister.
	 */
	public <T> void create(T entity);

	/**
	 * Méthode générique de mise-à-jour d'entité dans une base de données.
	 * Fusionne la version de l'entité passée en paramètre avec celle présente dans la base de données.
	 * 
	 * @param entity L'entité à mettre à jour.
	 * @return L'entité résultante de la fusion.
	 */
	public <T> T update(T entity);

	/**
	 * Méthode générique de suppression d'entité dans une base de données.
	 * Recherche l'entité décrite par les paramètres dans la base de données et la supprime si elle existe.
	 * 
	 * @param clazz La classe de l'entité à supprimer.
	 * @param id L'identifiant unique de l'entité à supprimer.
	 */
	public <T> void remove(Class<T> clazz, Object id);
	
	/**
	 * Méthode générique de recherche d'entités dans une base de données (via un attribut).
	 * Retourne une collection d'entités de la classe <code>clazz</code> et qui ont un attribut <code>propertyName<code>
	 * similaire à <code>propertyValue</code>. Cet attribut est de type <code>String</code>.
	 * 
	 * Exemple d'utilisation
	 * Pour rechercher toutes les personnes qui ont un nom de famille contenant "SNOUS":
	 * <code>findByStringProperty(Person.class, "lastName", "%SNOUS%");</code>
	 * 
	 * @param clazz La classe des entités à rechercher.
	 * @param propertyName Le nom de l'attribut des entités.
	 * @param propertyValue La valeur de la propriété (critère de recherche).
	 * @return La collection d'entités correspondant aux critères de recherche.
	 */
	public <T> Collection<T> findByStringProperty(Class<T> clazz, String propertyName, String propertyValue);

	/**
	 * Méthode générique de création d'entités dans une base de données.
	 * Fait persister le tableau d'entités passé en paramètre dans la base de données.
	 * Les entités sont regroupées par lots de taille BATCH_SIZE pour optimiser l'insertion. 
	 * 
	 * @param entity L'entité à faire persister.
	 */
	public <T>	void createAll(T[] entity);
}
