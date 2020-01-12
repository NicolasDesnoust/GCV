package gcv.web;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import java.io.IOException;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.Cookie;
import org.omnifaces.util.Faces;

@Named("parametersController")
@SessionScoped
public class ParametersController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject @Named("languageController")
	private LanguageController languageController;
	
	@Inject @Named("themeController")
	private ThemeController themeController;
	
	public void initParameters() {
		//languageController.init();
		//themeController.init();
	}
	
	public void applyChanges() throws IOException {
		themeController.changeCurrentTheme();
		languageController.changeCountryLocaleCode();
		Faces.refresh();
	}

}
