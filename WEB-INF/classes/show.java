import java.sql.Connection;
import java.sql.DriverManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
@WebServlet("/show")
public class show extends HttpServlet{
	public void service(HttpServletRequest req,HttpServletResponse res) throws IOException
	{
		res.setContentType("text/html");
		PrintWriter out=res.getWriter();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/Reminder","root","Naga26@123");
			String name,tt,desc,dd;
			String s2="Select rname,dd,tt,rdesc from table1";
			Statement stm=con.createStatement();
			ResultSet set=stm.executeQuery(s2);
			int c=1;
			while(set.next())
			{
				if(c==1)
				{
					out.println("\n<br>-----------------Reminders----------------<br>");
					c++;
				}
					name=set.getString(1);
					dd=set.getString(2);
					tt=set.getString(3);
					desc=set.getString(4);
					out.println("\n Name          : "+name+"<br>");
					out.println("\n Date          : "+dd+"<br>");
					out.println("\n Description   : "+desc+"<br>");
					out.println("\n Time          : "+tt+"<br>");
					out.println("\n------------------------------------------<br>");
				}
		}
		catch(Exception e)
		{
			out.println(e+"<br>");
		}
		out.println("<body bgcolor=\"BlanchedAlmond\"></body>");
		out.println("<br><b>Redirect to main page 'click me'</b><br><input type='button' onclick=\"window.location='index.html'\" value='Click Me!'>");
	}

}
