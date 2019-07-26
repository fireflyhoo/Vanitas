package cn.yayatao.vanitas.postgresql.datagram.server;

import cn.yayatao.vanitas.postgresql.datagram.IServerDatagram;

import java.nio.ByteBuffer;

public class AuthenticationCleartextPassword implements IServerDatagram {
	private char mark = 'R';
	
	private int length = 8;
	
	private int authType = 3;
	
	
	
	
	

	@Override
	public byte[] toByteArrays() {
		ByteBuffer buffer= ByteBuffer.allocate(size());
		buffer.put((byte)mark);
		buffer.putInt(length);
		buffer.putInt(authType);		
		return buffer.array();
	}

	@Override
	public int size() {
		return 9;
	}

	@Override
	public void reviseLength() {
		
	}

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

	public int getAuthType() {
		return authType;
	}

	public void setAuthType(int authType) {
		this.authType = authType;
	}

}
