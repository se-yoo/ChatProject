package chatProject.service;

import java.sql.Connection;
import java.sql.DriverManager;

public class ChatSQLConn {
	public static Connection getConnection(){
		Connection conn = null;
		
	    String url ="jdbc:mysql://localhost:3306/chat_project?useUnicode=true&characterEncoding=euckr";
	    String username="root";
	    String password="apmsetup";
	        
        try{
        	Class.forName("com.mysql.jdbc.Driver");
            conn=DriverManager.getConnection(url,username,password);
 
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return conn;
    }
}
