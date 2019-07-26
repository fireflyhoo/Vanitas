package cn.yayatao.vanitas.postgresql.datagram.server;

import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.IServerDatagram;
import cn.yayatao.vanitas.postgresql.datagram.general.ByteUtils;

//	ParameterStatus (B)
//		Byte1('S')
//		标识这条消息是一个运行时参数状态报告
//		
//		Int32
//		以字节计的消息内容长度，包括长度本身。
//		
//		String
//		被报告的运行时参数的名字
//		
//		String
//		参数的当前值
public class ParameterStatus implements IServerDatagram {
	private char mark = 'S';

	private int length;

	private String name;

	private String value;

	@Override
	public byte[] toByteArrays() {
		if (length == 0) {
			reviseLength();
		}
		ByteBuffer buffer = ByteBuffer.allocate(size());
		buffer.put((byte) mark);
		buffer.putInt(length);

		buffer.put(ByteUtils.stringToBytes(name, true));
		buffer.put(ByteUtils.stringToBytes(value, true));
		return buffer.array();
	}

	@Override
	public int size() {
		return length + 1;
	}

	@Override
	public void reviseLength() {
		int _length = 4/* length */ + ByteUtils.getStringLength(name) + ByteUtils.getStringLength(value);
		this.length = _length;
	}

}
