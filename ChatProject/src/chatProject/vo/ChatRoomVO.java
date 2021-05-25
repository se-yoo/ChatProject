package chatProject.vo;

public class ChatRoomVO {
	private String chatRoomSeq = "";
	private String userId = "";
	private String userName = "";
	private String adminId = "";
	private String adminName = "";
	private String ownAuth = "";
	private String unReadCnt = "";
	private ChatVO recentChat = new ChatVO();
	
	public String getChatRoomSeq() {
		return chatRoomSeq;
	}
	public void setChatRoomSeq(String chatRoomSeq) {
		this.chatRoomSeq = chatRoomSeq;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	public String getOwnAuth() {
		return ownAuth;
	}
	public void setOwnAuth(String ownAuth) {
		this.ownAuth = ownAuth;
	}
	public String getUnReadCnt() {
		return unReadCnt;
	}
	public void setUnReadCnt(String unReadCnt) {
		this.unReadCnt = unReadCnt;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	public ChatVO getRecentChat() {
		return recentChat;
	}
	public void setRecentChat(ChatVO recentChat) {
		this.recentChat = recentChat;
	}
}
