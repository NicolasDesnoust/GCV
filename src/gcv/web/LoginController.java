package gcv.web;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.HttpSession;

import org.apache.catalina.manager.util.SessionUtils;

import gcv.beans.Person;
import gcv.business.User;
import gcv.dao.Dao;

@ManagedBean (name= "loginController")
@RequestScoped
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
			return "login";
		}
		System.err.println("Login success !");
		return "home";
	}
	
	public String showLogin() {
		System.err.println("Redirecting to login page ...");
		return "login";
	}
	
	public String logoutUser() {
		
		userManager.logout();
		
		return "home";
	}
	
	public boolean isUserConnected() {
		return userManager.isConnected();
	}
}
