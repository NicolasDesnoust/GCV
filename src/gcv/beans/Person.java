package gcv.beans;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import gcv.util.PasswordHash;

/**Bean représentant une personne.
 * Ce dernier peut ëtre persisté dans une base de données.
 * 
 * @author Nicolas DESNOUST
 * @author Serigne Bassirou Mbacké LY
 */
@Entity
public class Person implements Serializable {

	/**Facilite la gestion des versions de cette classe (en terme de données sérialisées).
	 * A incrémenter pour une nouvelle version si jamais il est nécessaire de stocker des
	 * données sérialisées qui doivent survivent à plusieurs versions du code.
	 */
	private static final long serialVersionUID = 1L;

	/**Identifiant unique d'une personne. Equivalent de la clé primaire au niveau base de données.
	 * Il est généré automatiquement. Il ne s'agit pas du login utilisé pour se connecter.
	 */
	@Id()
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer PersonID;

	/**Prénom d'une personne.
	 * Il est obligatoire.
	 */
	@Basic(optional = false)
	@Column(name = "first_name", length = 50, nullable = false)
	private String firstName;

	/**Nom de famille d'une personne.
	 * Il est obligatoire.
	 */
	@Basic(optional = false)
	@Column(name = "last_name", length = 50, nullable = false)
	private String lastName;

	/**Adresse mail d'une personne.
	 * Elle est obligatoire car elle sert de login.
	 */
	@Basic(optional = false)
	@Column(name = "mail", length = 255, nullable = false)
	private String mail;

	/**Site web d'une personne.
	 * Il n'est pas obligatoire.
	 */
	@Column(name = "web_site", length = 255)
	private String webSite;

	/**Date de naissance d'une personne.
	 * Elle est obligatoire.
	 */
	@Temporal(TemporalType.DATE)
	@Basic(optional = false)
	@Column(name = "birth_date")
	//TODO : trouver un équivalent pour l'annotation suivante, pas de spring ici.
	// @DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date birthDate;

	/**Hash du mot de passe d'une personne.
	 * Il est obligatoire.
	 */
	@Basic(optional = false)
	@Column(name = "password_hash", length = 102, nullable = false)
	private String passwordHash;

	//TODO: Revoir le cascade
	/**CV d'une personne sous forme d'une collection d'activités.
	 * Il est obligatoire et chargé directement lors d'une lecture depuis une base de données.
	 */
	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.EAGER, mappedBy = "owner")
	private Set<Activity> cv;

	/** Construit une personne vide.
	 */
	public Person() {
		super();
	}

	/** Construit entièrement une personne. 
	 *  L'identifiant est ajouté automatiquement. Le mot de passe est haché par l'appel
	 *  du setter de passwordHash.
	 *  
	 * @param firstName Le prénom d'une personne.
	 * @param lastName Le nom de famille d'une personne.
	 * @param mail L'adresse mail d'une personne.
	 * @param webSite Le site web d'une personne.
	 * @param birthDate La date de naissance d'une personne.
	 * @param password Le hash du mot de passe d'une personne.
	 */
	public Person(String firstName, String lastName, String mail, String webSite, Date birthDate,
			String password) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.mail = mail;
		this.webSite = webSite;
		this.birthDate = birthDate;
		setPassword(password);
	}
	
	/** Retourne l'identifiant de la personne.
	 * @return L'identifiant de la personne.
	 */
	public Integer getPersonID() {
		return PersonID;
	}

	/** Redéfini l'identifiant de la personne. Ce setter est présent pour respecter les
	 * conventions sur les beans entité. Il n'est pas nécessaire puisque l'identifiant
	 * est attribué automatiquement.
	 * @param personID Le nouvel identifiant de la personne.
	 */
	public void setPersonID(Integer personID) {
		PersonID = personID;
	}

	/** Retourne le prénom de la personne.
	 * @return Le prénom de la personne.
	 */
	public String getFirstName() {
		return firstName;
	}

	/** Redéfini le prénom de la personne.
	 * @param firstName Le nouveau prénom de la personne.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/** Retourne le nom de famille de la personne.
	 * @return Le nom de famille de la personne.
	 */
	public String getLastName() {
		return lastName;
	}

	/** Redéfini le nom de famille de la personne.
	 * @param lastName Le nouveau nom de famille de la personne.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/** Retourne l'adresse mail de la personne.
	 * @return L'adresse mail de la personne.
	 */
	public String getMail() {
		return mail;
	}

	/** Redéfini l'adresse mail de la personne.
	 * @param mail La nouvelle adresse mail de la personne.
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/** Retourne le site web de la personne.
	 * @return Le site web de la personne.
	 */
	public String getWebSite() {
		return webSite;
	}

	/** Redéfini le site web de la personne.
	 * @param webSite Le nouveau site web de la personne.
	 */
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	/** Retourne la date de naissance de la personne.
	 * @return La date de naissance de la personne.
	 */
	public Date getBirthDate() {
		return birthDate;
	}

	/** Redéfini la date de naissance de la personne.
	 * @param birthDate La nouvelle date de naissance de la personne.
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	/** Retourne le hash du mot de passe de la personne.
	 * @return Le hash du mot de passe de la personne.
	 */
	public String getPasswordHash() {
		return passwordHash;
	}

	/** Redéfini le hash du mot de passe de la personne.
	 * @param password Le nouveau mot de passe de la personne à hacher.
	 */
	public void setPassword(String password) {
		try {
			passwordHash = PasswordHash.createHash(password);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** Retourne le CV d'une personne sous forme de collection d'activités.
	 * @return Le CV d'une personne sous forme de collection d'activités.
	 */
	public Collection<Activity> getCv() {
		return cv;
	}

	/** Redéfini le CV de la personne à partir d'une collection d'activités.
	 * @param cv Le nouveau CV de la personne.
	 */
	public void setCv(Set<Activity> cv) {
		this.cv = cv;
	}

	/**Permet d'ajouter une nouvelle activité au CV de la personne.
	 * 
	 * @param activity La nouvelle activité à ajouter.
	 */
	public void addActivity(Activity activity) {
		if (cv == null) {
			cv = new HashSet<Activity>();
		}
		cv.add(activity);
		activity.setOwner(this);
	}

	/** Supprime une activité du CV de la personne.
	 * 
	 * @param activity L'activité à supprimer.
	 * @return true si la suppression a bien eu lieu, false sinon.
	 */
	public boolean removeActivity(Activity activity) {
		if (cv != null) {
			if (cv.contains(activity)) {
				cv.remove(activity);
				activity.setOwner(null);
				return true;
			}
		}

		return false;
	}

	/** Redéfinition de la méthode toString() de la classe Object pour afficher une personne
	 *  de façon personnalisée.
	 *  
	 *  @see Object#toString()
	 */
	@Override
	public String toString() {
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");

		return "Person [PersonID = " + PersonID + ", firstName = " + firstName + ", lastName = " + lastName
				+ ", mail = " + mail + ", webSite = " + webSite + ", birthDate = " + formater.format(birthDate) + ", cv = " + cv + "]";
	}

}
