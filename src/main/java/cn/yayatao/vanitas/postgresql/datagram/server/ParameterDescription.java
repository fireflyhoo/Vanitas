package cn.yayatao.vanitas.postgresql.datagram.server;

import java.nio.ByteBuffer;

//		ParameterDescription (B)
//		Byte1('t')
//		标识消息是一个参数描述
//		
//		Int32
//		以字节计的消息内容长度，包括长度本身。
//		
//		Int16
//		语句所使用的参数的个数(可以为零)
//		
//		然后，对每个参数，有下面的东西：
//			
//			Int32
//			声明参数数据类型的对象 ID
public class ParameterDescription implements IServerDatagram{

	private char mark = 't';

	private int length ;
	
	private short argumentsNumber;
	
	private int[] argumentTypeOids;

	@Override
	public byte[] toByteArrays() {
		if (length == 0) {
			reviseLength();
		}
		ByteBuffer buffer = ByteBuffer.allocate(size());
		buffer.put((byte) mark);
		buffer.putInt(length);
		buffer.putShort(argumentsNumber);
		for (int oid : argumentTypeOids) {
			buffer.putInt(oid);
		}
		return buffer.array();
	}

	@Override
	public int size() {
		return length + 1;
	}

	@Override
	public void reviseLength() {
		this.length= 4/*length*/ + 2 /*argumentsNumber*/ + argumentTypeOids.length*4;
	}
	
	
	
	
	
	
}
