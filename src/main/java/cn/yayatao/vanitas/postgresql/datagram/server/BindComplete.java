package cn.yayatao.vanitas.postgresql.datagram.server;

import java.nio.ByteBuffer;

//		BindComplete (B)
//		Byte1('2')
//		标识消息为一个绑定结束标识符
//		
//		Int32(4)
//		以字节记的消息长度，包括长度本身。
public class BindComplete implements IServerDatagram {

	private char mark = '2';

	private int length = 4;

	@Override
	public byte[] toByteArrays() {
		ByteBuffer buffer = ByteBuffer.allocate(size());
		buffer.put((byte) mark);
		buffer.putInt(length);
		return buffer.array();
	}

	@Override
	public int size() {
		return 5;
	}

	@Override
	public void reviseLength() {
	}

}
