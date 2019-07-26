package cn.yayatao.vanitas.postgresql.datagram.general;

import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.IFrontDatagram;
import cn.yayatao.vanitas.postgresql.datagram.IServerDatagram;

//		CopyDone (F & B)
//		Byte1('c')
//		标识这条信息是一个 COPY 结束指示器。
//		
//		Int32(4)
//		以字节计的消息内容长度，包括长度本身。
public class CopyDone implements IServerDatagram,IFrontDatagram{
	private char mark = 'c';

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
