package cn.yayatao.vanitas.postgresql.datagram.server;

import java.nio.ByteBuffer;

//		PortalSuspended (B)
//		Byte1('s')
//		标识这条消息是一个入口挂起指示器。请注意这个消息只出现在达到一条 Execute 消息的行计数限制的时候。
//		
//		Int32(4)
//		以字节计的消息内容长度，包括长度本身。
public class PortalSuspended implements IServerDatagram {
	private char mark = 's';

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
}
