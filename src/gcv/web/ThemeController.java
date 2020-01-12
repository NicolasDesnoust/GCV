package gcv.web;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import java.io.IOException;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.Cookie;
import org.omnifaces.util.Faces;
import org.primefaces.event.CloseEvent;

@Named("themeController")
@SessionScoped
public class ThemeController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject @Cookie
	private String currentTheme = null;
	
	private final String DEFAULT_THEME = "luna-green";
	private String librairy;

	private static List<String> themes;

	static {
		themes = new LinkedList<String>();
        themes.add("nova-light");
        themes.add("nova-dark");
        themes.add("nova-colored");
        themes.add("luna-blue");
        themes.add("luna-amber");
        themes.add("luna-green");
        themes.add("luna-pink");
        themes.add("omega");
	}

	@PostConstruct
	public void init() throws IOException {
		if (currentTheme == null) {
			System.out.println("currentTheme is null, set to default.");
			currentTheme = DEFAULT_THEME;
		} else {
			System.out.println("currentTheme is initialized based on cookie :" + currentTheme);
		}
		librairy = currentTheme;
	}

	public List<String> getThemes() {
		return themes;
	}

	public String getCurrentTheme() {
		return currentTheme;
	}

	public void setCurrentTheme(String currentTheme) {
		System.out.println("currentTheme set to " + currentTheme);
		this.currentTheme = currentTheme;

	}
	
	public void changeCurrentTheme() throws IOException {
	
		Faces.addResponseCookie("currentTheme", currentTheme, 2_592_000);
		System.out.println("saving " + currentTheme);
		System.out.println("new value of cookie : " + Faces.getRequestCookie("currentTheme"));
		librairy = currentTheme;
	}

	public String getLibrairy() {
		return librairy;
	}

	public void setLibrairy(String librairy) {
		this.librairy = librairy;
	}
}