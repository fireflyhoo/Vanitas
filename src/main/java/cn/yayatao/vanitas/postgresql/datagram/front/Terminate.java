package cn.yayatao.vanitas.postgresql.datagram.front;

import cn.yayatao.vanitas.postgresql.datagram.IFrontDatagram;

import java.nio.ByteBuffer;

//		Terminate (F)
//		Byte1('X')
//		标识消息是一个终止消息。
//		
//		Int32(4)
//		以字节计的消息内容长度，包括长度本身。

public class Terminate implements IFrontDatagram {
	
	private char mark = 'X';

	private int length = 4;
	
	
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
		this.length = 4;
	}


}
