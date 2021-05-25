package chatProject.vo;

public class ChatVO {
	private String chatRoomSeq = "";
	private String msgSeq = "";
	private String msgSendngId = "";
	private String msgRcvId = "";
	private String msgSendngName = "";
	private String msgRcvName = "";
	private String msgCtnt = "";
	private String sendngTime = "";
	private String rcvTime = "";
	
	public String getChatRoomSeq() {
		return chatRoomSeq;
	}
	public void setChatRoomSeq(String chatRoomSeq) {
		this.chatRoomSeq = chatRoomSeq;
	}
	public String getMsgSeq() {
		return msgSeq;
	}
	public void setMsgSeq(String msgSeq) {
		this.msgSeq = msgSeq;
	}
	public String getMsgSendngId() {
		return msgSendngId;
	}
	public void setMsgSendngId(String msgSendngId) {
		this.msgSendngId = msgSendngId;
	}
	public String getMsgRcvId() {
		return msgRcvId;
	}
	public void setMsgRcvId(String msgRcvId) {
		this.msgRcvId = msgRcvId;
	}
	public String getMsgCtnt() {
		return msgCtnt;
	}
	public void setMsgCtnt(String msgCtnt) {
		this.msgCtnt = msgCtnt;
	}
	public String getSendngTime() {
		return sendngTime;
	}
	public void setSendngTime(String sendngTime) {
		this.sendngTime = sendngTime;
	}
	public String getRcvTime() {
		return rcvTime;
	}
	public void setRcvTime(String rcvTime) {
		this.rcvTime = rcvTime;
	}
	public String getMsgSendngName() {
		return msgSendngName;
	}
	public void setMsgSendngName(String msgSendngName) {
		this.msgSendngName = msgSendngName;
	}
	public String getMsgRcvName() {
		return msgRcvName;
	}
	public void setMsgRcvName(String msgRcvName) {
		this.msgRcvName = msgRcvName;
	}
}
