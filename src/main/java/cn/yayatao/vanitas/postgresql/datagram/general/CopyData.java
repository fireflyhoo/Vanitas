package cn.yayatao.vanitas.postgresql.datagram.general;

import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.IFrontDatagram;
import cn.yayatao.vanitas.postgresql.datagram.IServerDatagram;

//		CopyData (F & B)
//		Byte1('d')
//		标识这条消息是一个 COPY 数据。
//		
//		Int32
//		以字节记的消息内容的长度，包括长度本身。
//		
//		Byten
//		COPY 数据流的一部分的数据。从后端发出的消息总是对应一个数据行，但是前端发出的消息可以任意分割数据流。

public class CopyData implements IServerDatagram, IFrontDatagram {
	private char mark = 'c';

	private int length;

	private byte[] data;
	
	

	@Override
	public byte[] toByteArrays() {
		if (length == 0) {
			reviseLength();
		}
		ByteBuffer buffer = ByteBuffer.allocate(size());
		buffer.put((byte) mark);
		buffer.putInt(length);
		buffer.put(data);
		return buffer.array();
	}

	@Override
	public int size() {
		return length + 1;
	}

	@Override
	public void reviseLength() {
		this.length = data.length + 4/* length */;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public char getMark() {
		return mark;
	}

	public void setMark(char mark) {
		this.mark = mark;
	}

}
