package cn.yayatao.vanitas.postgresql.datagram.front;

import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.general.ByteUtils;

//		
//		PasswordMessage (F)
//		Byte1('p')
//		标识这条消息是一个口令响应。
//		
//		Int32
//		以字节计的消息内容长度，包括长度本身。
//		
//		String
//		口令(如果要求了，就是加密后的)。

public class PasswordMessage implements IFrontDatagram {
	
	private char mark = 'p';

	private int length;
	
	/**
	 * 密码
	 */
	private String password;
	
	

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public byte[] toByteArrays() {
		if(length <=0){
			reviseLength();
		}
		ByteBuffer buffer = ByteBuffer.allocate(size());
		
		buffer.put((byte)mark);
		buffer.putInt(length);
		buffer.put(ByteUtils.stringToBytes(password, true));
		return buffer.array();
	}

	@Override
	public int size() {
		return length+1;
	}

	@Override
	public void reviseLength() {

		int currLength = 4 /*length self*/
				+ ByteUtils.getStringLength(password);
		this.length = currLength;
	}

}
