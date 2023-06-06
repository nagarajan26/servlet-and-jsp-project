import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
@WebServlet("/set")
public class create extends HttpServlet{
static{
System.load("E:\\apache-tomcat-9.0.55\\webapps\\jazz\\WEB-INF\\classes\\ReminderApp.dll");
}

 
   public native int add(String name,String date,String time,int week,int day,int notspecs);
 

	public void service(HttpServletRequest req,HttpServletResponse res) throws IOException
	{
		String name=null,desc=null,date=null,time=null,email=null;
		int rtype=0,week=0,day=0,notspecs=0;
		PrintWriter out=res.getWriter();
		try {
			//Class.forName("com.mysql.cj.jdbc.Driver");
			//Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/Reminder","root","Naga26@123");
			//PreparedStatement ptmt=con.prepareStatement("insert into table1(rname,rdesc,dd,tt,wk,mon,notspecs,rtype,email) value(?,?,?,?,?,?,?,?,?)");
			name=req.getParameter("rname");
			desc=req.getParameter("rdesc");
			date=req.getParameter("date");
			time=req.getParameter("time");
			time=time+":00";
			email=req.getParameter("email");
			rtype=Integer.parseInt(req.getParameter("rtype"));
			notspecs=Integer.parseInt(req.getParameter("notspecs"));
			/*if(notspecs ==1 || notspecs==2)
			{
				week=0;
				day=0;
			}
			else if(notspecs ==3)
			{
				day=Integer.parseInt(req.getParameter("day"));
				week=0;
			}
			else
			{
				week=Integer.parseInt(req.getParameter("week"));
				day=0;
			}
			ptmt.setString(1, name);
			ptmt.setString(2, desc);
			ptmt.setString(3,date);
			ptmt.setString(4, time);
			ptmt.setInt(5,week);
			ptmt.setInt(6,day );
			ptmt.setInt(8,rtype );
			ptmt.setInt(7, notspecs);
			ptmt.setString(9,email);
			ptmt.executeUpdate();*/
                        int ret=0;
			ret=add(name,date,time,week,day,notspecs);
			if(ret==1)
			{
				System.out.println("Successfully registered task");
			}
			else
			{
				System.out.println("Error");

			}
			res.sendRedirect("create.jsp");
		 }
		catch(Exception e)
		{
			System.out.println(e);
		}
		
   }
}
