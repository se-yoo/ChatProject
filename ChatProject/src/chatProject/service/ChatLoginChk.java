package chatProject.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import chatProject.vo.UserVO;

public class ChatLoginChk {
	public UserVO getLoginUser(String id, String pw) throws Exception {
		UserVO user = new UserVO();
		
		Connection conn = ChatSQLConn.getConnection();
        Statement stmt = null;
		
		ResultSet rs = null;
	    stmt = conn.createStatement();
	    String sql = "select user_id, name, auth from user_info where user_id = '"
	    		+ id + "' and user_pw = '" + pw + "';";
	    rs = stmt.executeQuery(sql);
	     
	    if(rs.next()){
	    	user.setUserId(rs.getString("user_id"));
	    	user.setName(rs.getString("name"));
	    	user.setAuth(rs.getString("auth"));
	    }
         
		return user;
	}
}
