package cn.yayatao.vanitas.postgresql.datagram.server;

import cn.yayatao.vanitas.postgresql.datagram.IServerDatagram;

import java.nio.ByteBuffer;

//		CloseComplete (B)
//		Byte1('3')
//		标识消息是一个 Close 完毕指示器。
//		
//		Int32(4)
//		以字节记的消息内容的长度，包括长度本身。
public class CloseComplete implements IServerDatagram {
	private char mark = '3';

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
		return length+1;
	}

	@Override
	public void reviseLength() {
	}

}
