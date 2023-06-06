import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
@WebServlet("/logout")
public class logout extends HttpServlet {

  private static final long serialVersionUID = 1L;
  
  protected void service(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
    	int c=1;
	PrintWriter out=response.getWriter();
    // Invalidate current HTTP session.
    // Will call JAAS LoginModule logout() method
	System.out.println("logout");
    request.getSession().invalidate();
    out.println(c);
    
  }

}