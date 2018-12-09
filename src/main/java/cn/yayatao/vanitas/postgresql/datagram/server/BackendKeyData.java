package cn.yayatao.vanitas.postgresql.datagram.server;

import java.nio.ByteBuffer;

//		BackendKeyData (B)
//		Byte1('K')
//		标识该消息是一个取消键字数据。如果前端希望能够在稍后发出 CancelRequest 消息，那么它必须保存这个值。
//		
//		Int32(12)
//		以字节记的消息内容的长度，包括长度本身。
//		
//		Int32
//		后端的进程号(PID)
//		
//		Int32
//		此后端的密钥
public class BackendKeyData implements IServerDatagram{
	private char mark = 'K';
	
	private int length = 12;
	
	private int pid;
	
	private int secretKey;

	@Override
	public byte[] toByteArrays() {
		ByteBuffer buffer = ByteBuffer.allocate(size());
		buffer.put((byte)mark);
		buffer.putInt(length);
		buffer.putInt(pid);
		buffer.putInt(secretKey);
		return buffer.array();
	}

	@Override
	public int size() {
		return length +1;
	}

	@Override
	public void reviseLength() {
	}
	
	
	

}
