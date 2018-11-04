package cn.yayatao.vanitas.postgresql.datagram.server;

import java.nio.ByteBuffer;

//		ReadyForQuery (B)
//		Byte1('Z')
//		标识消息类型。在后端为新的查询循环准备好的时候，总会发送 ReadyForQuery
//		
//		Int32(5)
//		以字节计的消息内容长度，包括长度本身。
//		
//		Byte1
//		当前后端事务状态指示器。可能的值是空闲状况下的'I'(不在事务块里)；在事务块里是'T'；或者在一个失败的事务块里是'E'(在事务块结束之前，任何查询都将被拒绝)。

public class ReadyForQuery implements IServerDatagram {

	private char mark = 'Z';

	private int length = 5;

	private byte state;

	@Override
	public byte[] toByteArrays() {
		ByteBuffer buffer = ByteBuffer.allocate(size());
		buffer.put((byte) mark);
		buffer.putInt(length);
		buffer.put(state);
		return buffer.array();
	}

	@Override
	public int size() {
		return 6;
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

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

}
