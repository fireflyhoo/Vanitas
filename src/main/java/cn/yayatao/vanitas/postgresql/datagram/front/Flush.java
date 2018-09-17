package cn.yayatao.vanitas.postgresql.datagram.front;

import java.nio.ByteBuffer;

//		Flush (F)
//		Byte1('H')
//		标识消息识一条 Flush 命令。
//		
//		Int32(4)
//		以字节记的消息内容的长度，包括长度本身。

public class Flush implements IFrontDatagram {

	private char mark = 'H';

	private int length;

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

	@Override
	public byte[] toByteArrays() {
		if (this.length <= 0) {
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
		this.length = 4;

	}

}
