package chatProject.service;

import java.io.PrintWriter;
import java.net.Socket;

import chatProject.ChatList;
import chatProject.ChatRoom;
import chatProject.vo.ChatVO;
import chatProject.vo.UserVO;

public class ChatSocketSedng {
	Socket socket;
	ChatRoom cr;
	ChatList cl;
	String message;
	
	public ChatSocketSedng(Socket socket) {
		this.socket = socket;
	}
	
	public void sendMsg(UserVO user, ChatVO chat) {

        try{
        	ChatMgt cm = new ChatMgt();
        	PrintWriter pw=new PrintWriter(socket.getOutputStream(),true);
			String str = "";
        	
			cm.insertChatMessage(chat);
			
			str= chat.getMsgRcvId()+"(구분자)"
					+chat.getMsgSendngName()+"(구분자)"+chat.getMsgCtnt();
			
			pw.println(str);
		}catch(Exception ie){
			System.out.println(ie.getMessage());
		}
	}
}
