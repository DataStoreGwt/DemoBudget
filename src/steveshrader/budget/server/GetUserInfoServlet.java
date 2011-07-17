package steveshrader.budget.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;


import com.google.appengine.api.users.UserServiceFactory;

// to validate user
public class GetUserInfoServlet extends HttpServlet
{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException 
    {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if(user == null) 
		{
			 
		}
		else
		{
			resp.getWriter().println(user.getNickname());
			resp.getWriter().println(user.getUserId());
			resp.getWriter().println(UserServiceFactory.getUserService().createLogoutURL("/"));
		}

	}
  
}
