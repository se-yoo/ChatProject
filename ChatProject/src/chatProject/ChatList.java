package chatProject;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import chatProject.ChatLogin.SocketThread;
import chatProject.service.ChatMgt;
import chatProject.vo.ChatRoomVO;
import chatProject.vo.UserVO;

public class ChatList extends JFrame{
	
	private JPanel listPane = new JPanel();
	private JScrollPane listScrollPane = new JScrollPane();
	private Font normalFont = new Font("맑은 고딕", Font.PLAIN, 13);
	private Socket socket;
	private UserVO userInf;
    private ChatRoomVO chatRoomVO;
    private SocketThread st;
	
	public ChatList(SocketThread st, Socket socket, UserVO userInf, ChatRoomVO chatRoomVO)
	{
		super("문의 목록");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		this.st=st;
        this.socket = socket;
        this.userInf = userInf;
        this.chatRoomVO = chatRoomVO;
        
        listPane.setBackground(Color.WHITE);
        BoxLayout boxLayout = new BoxLayout(listPane, BoxLayout.Y_AXIS);
        listPane.setLayout(boxLayout);
        listScrollPane = new JScrollPane(listPane);
        listScrollPane.setSize(350, 300);
        add("Center", listScrollPane);

        loadRooms();

		setBounds(500, 300, 350, 500);
        setVisible(true);
	}

	public void loadRooms() {
		ChatMgt chatMgt = new ChatMgt();
		
		try {
			List<ChatRoomVO> results = chatMgt.getChatRoomList(chatRoomVO);
			
			for(int i=0;i<results.size();i++) {
				makeJLabel(results.get(i));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public JLabel makeJLabel(final ChatRoomVO chatRoom) {
		String jlabelCont = "";
		String jLabelContUnread ="";
		if(Integer.parseInt(chatRoom.getUnReadCnt())>0){
			jLabelContUnread="<span style=\"width:16px;height:16px;color:white;background:red;border-radius:9px;text-align:center\">"
				+chatRoom.getUnReadCnt()+"</span>";
		}
		
		jlabelCont = "<html><div style=\"padding: 5px;border-bottom:1px solid black;width:250px\"><div style=\"font-weight: bold\">"
				+chatRoom.getUserName()+"&nbsp;"+jLabelContUnread+"</div><div>"+chatRoom.getRecentChat().getMsgCtnt()
				+"</div><div style=\"color: gray\">"+chatRoom.getRecentChat().getSendngTime()+"</div></div></html>";
		
        JLabel jLabel = new JLabel(jlabelCont);
        jLabel.setFont(normalFont);
        
        jLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e)  
            { 
            	ChatRoom cr = new ChatRoom(socket, userInf, chatRoom);
            	setVisible(false);
            	st.cl = null;
            	st.cr = cr;
            }  
        });
        listPane.add(jLabel);
        listPane.revalidate();
        listScrollPane.revalidate();
        
        return jLabel;
    }
	
	public void print(String line) {
		listPane.removeAll();
        listPane.revalidate();
        listScrollPane.revalidate();
		loadRooms();
	}
}
