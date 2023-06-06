import java.sql.Connection;
import java.sql.DriverManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
@WebServlet("/del")
public class delete extends HttpServlet{
	public void service(HttpServletRequest req,HttpServletResponse res) throws IOException
	{
		res.setContentType("text/html");
		PrintWriter out=res.getWriter();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/Reminder","root","Naga26@123");
			String name;
			String rname=req.getParameter("name");
			String s2="Select rname from table1";
			Statement stm=con.createStatement();
			ResultSet set=stm.executeQuery(s2);
			PreparedStatement ptmt=con.prepareStatement("delete from table1 where rname=?");
			while(set.next())
			{
				name=set.getString(1);
				if(name.equals(rname))
				{
					ptmt.setString(1,name);
					System.out.println("jdn");
					ptmt.executeUpdate();
				}
			}
			res.sendRedirect("delete.jsp");
		}
		catch(Exception e)
		{
			out.println(e+"<br>");
		}
	}

}
