package gcv.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.omnifaces.filter.HttpFilter;
import org.omnifaces.util.Servlets;

import gcv.beans.Person;
import gcv.web.LoginController;
import gcv.web.PersonController;

@WebFilter("/user/*")
public class AuthorizationFilter extends HttpFilter {

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			FilterChain chain) throws ServletException, IOException {
		String error401url = request.getContextPath() + "/errorpages/401.xhtml";
		String loginURL = request.getContextPath() + "/login.xhtml";

		HttpSession httpSession = ((HttpServletRequest) request).getSession(false);
		LoginController loginController = (session != null) ? (LoginController) session.getAttribute("loginController")
				: null;
		PersonController personController = (session != null)
				? (PersonController) session.getAttribute("personController")
				: null;
		boolean loginRequest = request.getRequestURI().equals(loginURL);
		boolean resourceRequest = Servlets.isFacesResourceRequest(request);

		Person selectedPerson = personController.getSelectedPerson();
		Person currentUser = loginController.getCurrentUser();

		if (selectedPerson != null && currentUser != null) {
			if (selectedPerson.getPersonID().equals(currentUser.getPersonID())) {
				if (loginController != null && loginController.isUserConnected()) {
					if (!resourceRequest) { // Prevent browser from caching restricted resources. See also
											// https://stackoverflow.com/q/4194207/157882
						Servlets.setNoCacheHeaders(response);
					}

					chain.doFilter(request, response); // So, just continue request.
					return;
				}
			}
		}

		Servlets.facesRedirect(request, response, error401url);
	}

}
