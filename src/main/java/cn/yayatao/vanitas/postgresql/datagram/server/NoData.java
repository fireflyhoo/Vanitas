package cn.yayatao.vanitas.postgresql.datagram.server;

import cn.yayatao.vanitas.postgresql.datagram.IServerDatagram;

import java.nio.ByteBuffer;

//		NoData (B)
//		Byte1('n')
//		标识这条消息是一个无数据指示器
//		
//		Int32(4)
//		以字节计的消息内容长度，包括长度本身。
public class NoData implements IServerDatagram {
	
	private char mark = 'n';

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
