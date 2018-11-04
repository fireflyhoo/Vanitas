package cn.yayatao.vanitas.postgresql.datagram.server;

import java.nio.ByteBuffer;

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
