package cn.yayatao.vanitas.postgresql.datagram.server;

import cn.yayatao.vanitas.postgresql.datagram.IServerDatagram;

import java.nio.ByteBuffer;

//			AuthenticationOk (B)
//			Byte1('R')
//			标识该消息是一条认证请求。
//			
//			Int32(8)
//			以字节记的消息内容长度，包括这个长度本身。
//			
//			Int32(0)
//			声明该认证是成功的。
public class AuthenticationOk implements IServerDatagram {
	
	private char mark = 'R';
	
	private int length = 8;
	
	private int authType = 0;

	

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
