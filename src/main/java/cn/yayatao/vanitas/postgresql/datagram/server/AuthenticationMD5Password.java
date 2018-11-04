package cn.yayatao.vanitas.postgresql.datagram.server;

import java.nio.ByteBuffer;

//		AuthenticationMD5Password (B)
//		Byte1('R')
//		标识这条消息是一个认证请求。
//		
//		Int32(12)
//		以字节记的消息内容的长度，包括长度本身。
//		
//		Int32(5)
//		声明需要一个 MD5 加密的口令。
//		
//		Byte4
//		加密口令的时候使用的盐粒。
public class AuthenticationMD5Password implements IServerDatagram{
	
	private char mark = 'R';
	
	private int length = 12;
	
	private int authType = 5;
	
	private byte[] salt = new byte[]{10,18,90,28};
	
	

	@Override
	public byte[] toByteArrays() {
		ByteBuffer buffer = ByteBuffer.allocate(size());
		buffer.put((byte)mark);
		buffer.putInt(length);
		buffer.putInt(authType);
		buffer.put(salt);
		return buffer.array();
	}

	@Override
	public int size() {
		return 13;
	}

	@Override
	public void reviseLength() {
	}

}
