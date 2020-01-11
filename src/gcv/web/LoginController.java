package gcv.web;

import java.util.Locale;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.catalina.manager.util.SessionUtils;

import gcv.beans.Person;
import gcv.business.User;
import gcv.dao.Dao;

@ManagedBean (name= "loginController")
@SessionScoped
public class LoginController {
	
	@EJB(beanName = "jpadao")
	private Dao dao;
	
	@EJB(beanName = "user")
	private User userManager;
	
	private String login = "", password = "";
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String loginUser() {
		boolean connected = userManager.login(login, password);
		System.out.println(login +  " " + password);
		if (! connected) {
			System.err.println("Login failure.");
			System.err.println(userManager.isConnected());
			return "login";
		}
		System.err.println(userManager.isConnected());
		System.err.println("Login success !");
		return "home";
	}
	
	public String showLogin() {
		System.err.println("Redirecting to login page ...");
		return "login?faces-redirect=true";
	}
	
	public String logoutUser() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext ec = context.getExternalContext();
		final HttpServletRequest request = (HttpServletRequest) ec.getRequest();
		request.getSession(false).invalidate();
		
		userManager.logout();
		
		return "home";
	}

	public boolean isUserConnected() {
		return userManager.isConnected();
	}
	
	public Person getCurrentUser() {
		String currentLogin;
		Person currentUser = null;

		if (isUserConnected()) {
			currentLogin = userManager.getLogin();
			currentUser = dao.readPersonByMail(currentLogin);
		}
		
		return currentUser;
	}
}
