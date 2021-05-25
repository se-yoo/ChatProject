package chatProject;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import chatProject.service.ChatLoginChk;
import chatProject.service.ChatMgt;
import chatProject.vo.ChatRoomVO;
import chatProject.vo.ChatVO;
import chatProject.vo.UserVO;

public class ChatLogin extends JFrame implements ActionListener{

	private JTextField userId;
	private JPasswordField userPw;
	private JPanel loginPane = new JPanel();
	private JButton loginBtn = new JButton("로그인");
	private JLabel title = new JLabel("채팅 문의 프로그램");
	private Font titleFont = new Font("맑은 고딕", Font.PLAIN, 20);
	private Font normalFont = new Font("맑은 고딕", Font.PLAIN, 13);

	private String sID = "";
	private String sPW = "";
	
	public ChatLogin()
	{
		setTitle("로그인");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setBounds(500, 300, 350, 500);
		loginPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(loginPane);
		loginPane.setLayout(null);
		loginPane.setBackground(Color.white);

		title.setBounds(0, 150, 350, 20);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setForeground(new Color(188,159,202));
		title.setFont(titleFont);
		loginPane.add(title);
		
		JLabel lblId = new JLabel("ID");
		lblId.setHorizontalAlignment(SwingConstants.CENTER);
		lblId.setBounds(10, 200, 47, 15);
		lblId.setFont(normalFont);
		loginPane.add(lblId);

		userId = new JTextField();
		userId.setText("");
		userId.setBounds(50, 197, 170, 21);
		userId.setFont(normalFont);
		loginPane.add(userId);
		userId.setColumns(10);

		JLabel lblPw = new JLabel("PW");
		lblPw.setHorizontalAlignment(SwingConstants.CENTER);
		lblPw.setBounds(10, 225, 47, 15);
		lblPw.setFont(normalFont);
		loginPane.add(lblPw);

		userPw = new JPasswordField();
		userPw.setText("");
		userPw.setBounds(50, 222, 170, 21);
		userPw.setFont(normalFont);
		loginPane.add(userPw);
		userPw.setColumns(10);

		userId.addActionListener(this);
		userPw.addActionListener(this);
		loginBtn.addActionListener(this);
		loginBtn.setFont(normalFont);
		loginBtn.setBounds(225, 197, 90, 46);
		loginBtn.setBackground(new Color(188,159,202));
		loginBtn.setForeground(Color.white);
		loginBtn.setBorder(new LineBorder(new Color(188,159,202)));
		loginPane.add(loginBtn);
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 로그인 했을 때 작업
		GetIdandPw(userId, userPw); // 입력된 값 각각 넘겨 주기
		ChatLoginChk chatLoginChk = new ChatLoginChk();
		UserVO user = new UserVO();
		try {
			user = chatLoginChk.getLoginUser(sID, sPW);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		if (user.getAuth().equals("")) { // 로그인 실패
			JOptionPane.showMessageDialog(null, "아이디 또는 비밀번호를 확인 후\n다시 로그인해주세요.");
		} else {
			Socket socket=null;
			ChatMgt cm = new ChatMgt();
			ChatRoomVO srchCrvo = new ChatRoomVO();
			ChatRoomVO chatRoomVO = new ChatRoomVO();
			srchCrvo.setOwnAuth(user.getAuth());
			
			try {
				SocketThread st = new SocketThread();
				socket=new Socket("localhost",3000);
				// 소켓에 계정 정보 보내기
				PrintWriter pw=new PrintWriter(socket.getOutputStream(),true);
				pw.println(user.getUserId());
				
				if (user.getAuth().equals("admin")){
					srchCrvo.setAdminId(user.getUserId());
					
					ChatList cl = new ChatList(st, socket, user, srchCrvo);
					st.cl= cl;
				}
				else if (user.getAuth().equals("user")){
					srchCrvo.setUserId(user.getUserId());
					chatRoomVO = cm.getChatRoom(srchCrvo);
					if(chatRoomVO.getChatRoomSeq().equals(""))
					{
						cm.insertChatRoom(srchCrvo);
						chatRoomVO = cm.getChatRoom(srchCrvo);
					}
					
					ChatRoom cr = new ChatRoom(socket, user, chatRoomVO);
					st.cr= cr;
				}
				
				st.socket=socket;
				st.start();
				
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			/*finally { 
				try { 
					if (socket!=null&&!socket.isClosed()) { 
						socket.close();//다 쓴 소켓 닫기
					} 
				} catch (Exception e2) {
					e2.printStackTrace();
				} 
			}*/
			
			setVisible(false);
		}
	}
	
	private void GetIdandPw(JTextField userId, JTextField userPw) {
		// TODO Auto-generated method stub
		sID = userId.getText();
		sPW = userPw.getText();
	}

	class SocketThread extends Thread { 
		Socket socket;
		public ChatRoom cr = null;
		public ChatList cl = null;
		
		public void run() { 
			try { 
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
				String line = null; 
				while((line = reader.readLine()) != null) {
					if(cl!=null)cl.print(line);
					if(cr!=null)cr.print(line);
				} 
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
