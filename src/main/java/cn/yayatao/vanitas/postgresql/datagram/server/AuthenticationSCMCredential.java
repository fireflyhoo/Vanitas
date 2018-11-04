package cn.yayatao.vanitas.postgresql.datagram.server;

import java.nio.ByteBuffer;

//		AuthenticationSCMCredential (B)
//		Byte1('R')
//		标识这条消息是一个认证请求。
//		
//		Int32(8)
//		以字节计的消息内容长度，包括长度本身。
//		
//		Int32(6)
//		声明需要一个 SCM 信任消息。

public class AuthenticationSCMCredential implements IServerDatagram {
	
	private char mark = 'R';
	
	private int length = 8;
	
	private int authType = 6;
	
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
	
}
