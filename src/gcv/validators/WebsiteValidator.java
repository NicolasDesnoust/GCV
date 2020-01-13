package gcv.validators;

import org.apache.commons.validator.routines.DomainValidator;
import org.omnifaces.util.Faces;

import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("gcv.validators.WebsiteValidator")
public class WebsiteValidator implements Validator {

	@Override
	public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
		DomainValidator validator = DomainValidator.getInstance();
		String website, errorMessage;
		boolean valid;

		if (o == null) {
			return;
		}

		website = (String) o;
		
		valid = validator.isValid(website);
		if (!valid) {

			ResourceBundle bundle = ResourceBundle.getBundle("gcv.resources.messages", Faces.getLocale());
			errorMessage = bundle.getString("websiteValidator.invalid.website");

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null);
			throw new ValidatorException(msg);
		}
	}
}
