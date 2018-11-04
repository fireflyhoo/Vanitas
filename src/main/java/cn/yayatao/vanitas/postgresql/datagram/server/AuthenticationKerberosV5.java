package cn.yayatao.vanitas.postgresql.datagram.server;

import java.nio.ByteBuffer;


//		AuthenticationKerberosV5 (B)
//		Byte1('R')
//		标识该消息是一条认证请求。
//		
//		Int32(8)
//		以字节记的消息内容长度，包括长度自身。
//		
//		Int32(2)
//		声明需要 Kerberos V5 认证。
public class AuthenticationKerberosV5 implements IServerDatagram{
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
	
	

}
