package cn.yayatao.vanitas.postgresql.datagram.server;

import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.general.ByteUtils;

//			NotificationResponse (B)
//			Byte1('A')
//			标识这条消息是一个通知响应
//			
//			Int32
//			以字节计的消息内容长度，包括长度本身。
//			
//			Int32
//			通知后端进程地进程 ID
//			
//			String
//			触发通知的条件的名字
//			
//			String
//			从通知进程传递过来的额外的信息。目前，这个特性还未实现，因此这个字段总是一个空字符串。
public class NotificationResponse implements IServerDatagram{
	
	private char mark = 'A';

	private int length ;
	
	private int pid;
	
	private String name;
	
	private String extMsg;
	
	
	

	public char getMark() {
		return mark;
	}

	public void setMark(char mark) {
		this.mark = mark;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExtMsg() {
		return extMsg;
	}

	public void setExtMsg(String extMsg) {
		this.extMsg = extMsg;
	}

	@Override
	public byte[] toByteArrays() {
		if(length == 0){
			reviseLength();
		}
		ByteBuffer buffer = ByteBuffer.allocate(size());
		buffer.put((byte)mark);
		buffer.putInt(length);
		buffer.putInt(pid);
		buffer.put(ByteUtils.stringToBytes(name, true));
		buffer.put(ByteUtils.stringToBytes(extMsg, true));
		return buffer.array();
	}

	@Override
	public int size() {
		return length +1 ;
	}

	@Override
	public void reviseLength() {
		int _length = 4/* length */ + 4 /* pid */ + ByteUtils.getStringLength(name) + ByteUtils.getStringLength(extMsg);
		this.length = _length;
	}
	
	
}
