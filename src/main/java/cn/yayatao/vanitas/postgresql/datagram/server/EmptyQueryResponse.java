package cn.yayatao.vanitas.postgresql.datagram.server;

import cn.yayatao.vanitas.postgresql.datagram.IServerDatagram;

import java.nio.ByteBuffer;

//		Byte1('I')
//		标识这条消息是对一个空查询字符串的响应。这个消息替换了 CommandComplete
//		
//		Int32(4)
//		以字节记的消息内容长度，包括它自己。
public class EmptyQueryResponse implements IServerDatagram {

	private char mark = 'I';

	private int length = 4;

	@Override
	public byte[] toByteArrays() {
		if (length <= 0) {
			reviseLength();
		}
		ByteBuffer buffer = ByteBuffer.allocate(size());
		buffer.put((byte) mark);
		buffer.putInt(length);
		return buffer.array();
	}

	@Override
	public int size() {
		return length + 1;
	}

	@Override
	public void reviseLength() {
	}

}
