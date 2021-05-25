package chatProject.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import chatProject.vo.UserVO;

public class ChatSocketServer{
	ServerSocket serverSocket;
	Vector<SocketThread> vc;
	
	public ChatSocketServer() {
		try {
			serverSocket = new ServerSocket(3000); 
			
			vc = new Vector<>(); 
			// 메인 스레드는 소켓을 accept() 하고 vector에 담는 역할을 한다. 
			while (true) { 
				Socket socket = serverSocket.accept(); // 클라이언트의 요청을 받는다.
				SocketThread st = new SocketThread(socket);
				st.start(); 
				vc.add(st); 
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class SocketThread extends Thread {
		Socket socket; 
		String id; 
		BufferedReader reader; 
		PrintWriter writer; 
		
		public SocketThread(Socket socket) { 
			this.socket = socket; 
		}
		
		public void run() { 
			try { 
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8")); 
				writer = new PrintWriter(socket.getOutputStream(), true); 
				
				String line = null; 
				while ((line = reader.readLine()) != null) {
					router(line); 
				} 
			} catch (Exception e) {
				vc.remove(socket);
				//e.printStackTrace(); 
			} 
		} 
		
		public void router(String line) { 
			String[] gubun = line.split("[(]구분자[)]");
			
			if(gubun.length==1) {
				id=line;
				return;
			}
			String otherId = gubun[0];
			privateChat(otherId, line);
		} 
		
		public void privateChat(String otherId, String line) { 
			for (SocketThread socketThread : vc) {
				if (socketThread.id.equals(otherId)) { 
					socketThread.writer.println(line); 
				}  
			} 
		}
	}
	
	public static void main(String[] args) { 
		new ChatSocketServer(); 
	}
}
