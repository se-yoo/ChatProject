package chatProject.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import chatProject.vo.ChatRoomVO;
import chatProject.vo.ChatVO;

public class ChatMgt {
	public List<ChatRoomVO> getChatRoomList(ChatRoomVO chatRoomVO) throws Exception {
		List<ChatRoomVO> results = new ArrayList<>();
		
		Connection conn = ChatSQLConn.getConnection();
        Statement stmt = null;
		
		ResultSet rs = null;
	    stmt = conn.createStatement();
	    String sql = "SELECT *, (SELECT NAME FROM user_info i WHERE i.user_id=r.user_id) AS user_name, "
	    		+"(SELECT COUNT(msg_seq) FROM chat_msg cc WHERE cc.chat_room_seq=r.chat_room_seq "
	    		+"AND rcv_time IS NULL AND msg_send_id != '"+chatRoomVO.getAdminId()+"' )AS un_read_cnt FROM chat_room r, chat_msg c "
	    		+"WHERE r.chat_room_seq=c.chat_room_seq AND c.msg_seq=(SELECT MAX(msg_seq) FROM chat_msg cc WHERE "
	    		+"cc.chat_room_seq = r.chat_room_seq) AND ADMIN_ID = '"+chatRoomVO.getAdminId()+"' ORDER BY send_time DESC";
	    
	    rs = stmt.executeQuery(sql);
	     
	    while(rs.next()){
	    	ChatRoomVO newRoom = new ChatRoomVO();
	    	newRoom.setChatRoomSeq(rs.getString(1));
	    	newRoom.setUserId(rs.getString(2));
	    	newRoom.setAdminId(rs.getString(3));
	    	newRoom.getRecentChat().setMsgSeq(rs.getString(4));
	    	newRoom.getRecentChat().setMsgCtnt(rs.getString(8));
	    	newRoom.getRecentChat().setSendngTime(rs.getString(9));
	    	newRoom.setUserName(rs.getString(11));
	    	newRoom.setUnReadCnt(rs.getString(12));
	    	newRoom.setOwnAuth("admin");
	    	
	    	results.add(newRoom);
	    }
         
		return results;
	}
	
	public ChatRoomVO getChatRoom(ChatRoomVO chatRoomVO) throws Exception {
		ChatRoomVO result = new ChatRoomVO();
		
		Connection conn = ChatSQLConn.getConnection();
        
		Statement stmt = null;
		ResultSet rs = null;
	    stmt = conn.createStatement();
	    String addSql="";
	    
	    if(!chatRoomVO.getChatRoomSeq().equals(""))
	    {
	    	addSql = "where chat_room_seq = "+chatRoomVO.getChatRoomSeq();
	    }
	    else if(!chatRoomVO.getUserId().equals(""))
	    {
	    	addSql = "where user_id = '"+chatRoomVO.getUserId()+"'";
	    }
	    
	    String sql = "SELECT chat_room_seq, user_id, admin_id, "
	    		+"(SELECT NAME FROM user_info u WHERE u.user_id=c.user_id) AS user_name, "
	    		+"(SELECT NAME FROM user_info u WHERE u.user_id=c.admin_id) AS admin_name "
	    		+"FROM chat_room c "+addSql+";";
	    rs = stmt.executeQuery(sql);
	     
	    if(rs.next()){
	    	result.setChatRoomSeq(rs.getString("chat_room_seq"));
	    	result.setUserId(rs.getString("user_id"));
	    	result.setAdminId(rs.getString("admin_id"));
	    	result.setUserName(rs.getString("user_name"));
	    	result.setAdminName(rs.getString("admin_name"));
	    	result.setOwnAuth(chatRoomVO.getOwnAuth());
	    }
		
		return result;
	}
	
	public void insertChatRoom(ChatRoomVO chatRoomVO) throws Exception {
		
		Connection conn = ChatSQLConn.getConnection();
		
		Statement stmt = null;
	    stmt = conn.createStatement();
	    
		// 새로 방을 만들 경우 제일 활동이 적은 관리자와 매칭
		String adminSelectSql = "(SELECT user_id FROM("
					+"SELECT *, (SELECT COUNT(*) FROM chat_msg WHERE msg_send_id=user_id) AS msg_cnt "
					+"FROM user_info WHERE auth='admin' "
					+"ORDER BY msg_cnt ASC) TB LIMIT 1)";
		
		String sql = "INSERT INTO chat_room (user_id, admin_id) VALUES ('"
				+chatRoomVO.getUserId()+"', "+adminSelectSql+");";
		
		stmt.executeUpdate(sql);
	}
	
	public List<ChatVO> getChatList(ChatRoomVO chatRoomVO) throws Exception {
		List<ChatVO> results = new ArrayList<>();
		
		Connection conn = ChatSQLConn.getConnection();
        Statement stmt = null;
		
		ResultSet rs = null;
	    stmt = conn.createStatement();
	    String sql = "SELECT *,(SELECT NAME FROM user_info WHERE user_id=msg_send_id) AS msg_send_name FROM "
	    		+"(SELECT * FROM chat_msg WHERE chat_room_seq="+chatRoomVO.getChatRoomSeq()
	    		+" ORDER BY MSG_SEQ DESC LIMIT 0, 30) TB ORDER BY msg_seq ASC";
	    rs = stmt.executeQuery(sql);
	     
	    while(rs.next()){
	    	ChatVO newChat = new ChatVO();
	    	newChat.setMsgSeq(rs.getString(1));
	    	newChat.setChatRoomSeq(rs.getString(2));
	    	newChat.setMsgSendngId(rs.getString(3));
	    	newChat.setMsgRcvId(rs.getString(4));
	    	newChat.setMsgCtnt(rs.getString(5));
	    	newChat.setSendngTime(rs.getString(6));
	    	newChat.setRcvTime(rs.getString(7));
	    	newChat.setMsgSendngName(rs.getString(8));
	    	
	    	results.add(newChat);
	    }
         
		return results;
	}
	
	public void insertChatMessage(ChatVO chatVO) throws Exception {
		
		Connection conn = ChatSQLConn.getConnection();
		
		Statement stmt = null;
	    stmt = conn.createStatement();
		
		String sql = "INSERT INTO chat_msg (chat_room_seq, msg_send_id, msg_rcv_id, msg_cont, send_time) VALUES ('"
				+chatVO.getChatRoomSeq()+"', '"+chatVO.getMsgSendngId()+"', '"+chatVO.getMsgRcvId()+"'"
				+",'"+chatVO.getMsgCtnt()+"', NOW());";
		
		stmt.executeUpdate(sql);
	}
	
	public void setReadMessages(ChatRoomVO chatRoomVO) throws Exception {
		String readUserId=chatRoomVO.getUserId();
		
		if(chatRoomVO.getOwnAuth().equals("admin"))
		{
			readUserId=chatRoomVO.getAdminId();
		}

		Connection conn = ChatSQLConn.getConnection();
		
		Statement stmt = null;
	    stmt = conn.createStatement();

		String sql = "UPDATE chat_msg set rcv_time = now() WHERE rcv_time is null AND "
				+"chat_room_seq = "+chatRoomVO.getChatRoomSeq()+" AND msg_send_id <> '"+readUserId+"';";
		
		stmt.executeUpdate(sql);
	}
}
