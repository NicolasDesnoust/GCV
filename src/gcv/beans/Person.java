package gcv.beans;

import java.io.Serializable;
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

//import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Person implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id()
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer PersonID;

	@Basic(optional = false)
	@Column(name = "first_name", length = 50, nullable = false)
	private String firstName;

	@Basic(optional = false)
	@Column(name = "last_name", length = 50, nullable = false)
	private String lastName;

	@Basic(optional = false)
	@Column(name = "mail", length = 255, nullable = false)
	private String mail;

	@Basic(optional = false)
	@Column(name = "web_site", length = 255, nullable = false)
	private String webSite;

	@Temporal(TemporalType.DATE)
	@Column(name = "birth_date")
	//TODO : trouver un équivalent pour l'annotation suivante, pas de spring ici.
	// @DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date birthDate;

	@Basic(optional = false)
	@Column(name = "password", length = 20, nullable = false)
	private String password;

	//TODO: Revoir le cascade
	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.EAGER, mappedBy = "owner")
	private Set<Activity> cv;

	public Person() {
		super();
	}

	public Person(String firstName, String lastName, String mail, String webSite, Date birthDate,
			String password) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.mail = mail;
		this.webSite = webSite;
		this.birthDate = birthDate;
		this.password = password;
	}

	public Integer getPersonID() {
		return PersonID;
	}

	public void setPersonID(Integer personID) {
		PersonID = personID;
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

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Collection<Activity> getCv() {
		return cv;
	}

	public void setCv(Set<Activity> cv) {
		this.cv = cv;
	}

	public void addActivity(Activity activity) {
		if (cv == null) {
			cv = new HashSet<Activity>();
		}
		cv.add(activity);
		activity.setOwner(this);
	}

	public boolean removeActivity(Activity activity) {
		if (cv != null) {
			if (cv.contains(activity)) {
				cv.remove(activity);
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString() {
		return "Person [PersonID = " + PersonID + ", firstName = " + firstName + ", lastName = " + lastName
				+ ", mail = " + mail + ", webSite = " + webSite + ", birthDate = " + birthDate + ", password = "
				+ password + ", cv = " + cv + "]";
	}

}
