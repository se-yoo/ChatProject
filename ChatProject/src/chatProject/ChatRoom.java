package chatProject;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import chatProject.service.ChatMgt;
import chatProject.service.ChatSocketSedng;
import chatProject.vo.ChatRoomVO;
import chatProject.vo.ChatVO;
import chatProject.vo.UserVO;

public class ChatRoom extends JFrame implements ActionListener{
	
	private JTextField message = new JTextField(10);
	private JButton sendBtn = new JButton("전송");
	private JPanel p1 = new JPanel();
	private JPanel chatList = new JPanel();
    private JScrollPane chatListScroll;
	private UserVO userInf;
	private ChatSocketSedng css;
    private ChatRoomVO chatRoomVO;
	private ChatMgt chatMgt = new ChatMgt();
	private Font normalFont = new Font("맑은 고딕", Font.PLAIN, 13);
	
	public ChatRoom (Socket socket, UserVO userInf, ChatRoomVO chatRoomVO) {
		super("채팅 문의");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
        this.userInf = userInf;
        this.chatRoomVO = chatRoomVO;
        
        css = new ChatSocketSedng(socket);
        
        chatList.setBackground(Color.WHITE);
        chatList.setLayout( new BoxLayout(chatList, BoxLayout.Y_AXIS));
        chatListScroll = new JScrollPane(chatList);
        chatListScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        chatListScroll.setSize(350, 300);
        
        add("Center", chatListScroll);

        p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
        message.setFont(normalFont);
        sendBtn.setFont(normalFont);
        sendBtn.setPreferredSize(new Dimension(50,20));
        sendBtn.setBackground(new Color(188,159,202));
        sendBtn.setForeground(Color.white);
        sendBtn.setBorder(new LineBorder(new Color(188,159,202)));
        
        p1.add(message);
        p1.add(sendBtn);
        add("South", p1);
        
        readAllMessage();
        loadRecentMessage();

        sendBtn.addActionListener(this);
        message.addActionListener(this);
        chatListScroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				// TODO Auto-generated method stub
                JScrollBar src = (JScrollBar) e.getSource();
                src.setValue(src.getMaximum());
				
			}
		});
		setBounds(500, 300, 350, 500);
        setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
       //메세지 입력없이 전송버튼만 눌렀을 경우
       if(message.getText().equals("")){
    	   return;
       }
       ChatVO chat = new ChatVO();
       chat.setChatRoomSeq(chatRoomVO.getChatRoomSeq());
       chat.setMsgSendngId(userInf.getUserId());
       chat.setMsgSendngName(userInf.getName());
	   chat.setMsgRcvId(chatRoomVO.getAdminId());
       chat.setMsgCtnt(message.getText());
       
       if(chatRoomVO.getOwnAuth().equals("admin")) {
    	   chat.setMsgRcvId(chatRoomVO.getUserId());
       }
       
       makeJLabel(chat);
       css.sendMsg(userInf, chat);
       message.setText("");
	}
	
	public void loadRecentMessage() {		
		try {
			List<ChatVO> results = chatMgt.getChatList(chatRoomVO);
			
			for(int i=0;i<results.size();i++) {
				makeJLabel(results.get(i));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void readAllMessage() {
		try {
			chatMgt.setReadMessages(chatRoomVO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public JLabel makeJLabel(ChatVO chatVO) {
		String jlabelCont = "";
		if(chatVO.getMsgSendngId().equals(userInf.getUserId())) {
			jlabelCont = "<html><div style=\"width: 240px;text-align:right;\">"
	        		+"<div style=\"display:inline; padding:0 5px;margin-top:5px;margin-right:5px;margin-bottom:5px;border-right:3px solid #bc9fca;\">" 
	        		+ chatVO.getMsgCtnt().replaceAll("(\r\n|\n)", "<br />") + "</div></div></html>";
		}
		else {
			jlabelCont = "<html><div style=\"width: 240px;text-align:left;\">"
					+"<div style=\"padding-left:5px;\">"+chatVO.getMsgSendngName()+"</div>"
	        		+"<div style=\"display:inline; padding:0 5px;margin-top:5px;margin-left:5px;margin-bottom:5px;border-left:3px solid #e9e181;\">" 
	        		+ chatVO.getMsgCtnt().replaceAll("(\r\n|\n)", "<br />") + "</div></div></html>";
		}
        JLabel jLabel = new JLabel(jlabelCont);
        jLabel.setFont(normalFont);
        
        chatList.add(jLabel);
        chatList.revalidate();
        chatListScroll.revalidate();
        
        return jLabel;
    }

	public void print(String line) {
		String[] gubun = line.split("[(]구분자[)]");

		ChatVO chatVO = new ChatVO();
		chatVO.setMsgRcvId(gubun[0]);
		chatVO.setMsgSendngName(gubun[1]);
		chatVO.setMsgCtnt(gubun[2]);
		
		makeJLabel(chatVO);
		readAllMessage();		
	}
}
