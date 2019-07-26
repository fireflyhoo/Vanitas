package cn.yayatao.vanitas.postgresql.datagram.server;

import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.IServerDatagram;
import cn.yayatao.vanitas.postgresql.datagram.general.ByteUtils;

//		NoticeResponse (B)
//		Byte1('N')
//		标识这条消息是一个通知
//		
//		Int32
//		以字节计的消息内容长度，包括长度本身。
//		
//		消息体由一个或多个标识字段组成，后面跟着字节零作为中止符。字段可以以任何顺序出现。对于每个字段，都有下面的东西：
//		
//		Byte1
//		一个标识字段类型的代码；如果为零，那么它就是消息终止符，并且后面不会跟着字符串。目前定义的字段类型在节44.5里列出。因为将来可能会增加更多字段类型，所以前端应该将不识别的字段安静地忽略掉。
//		
//		String
//		字段值
public class NoticeResponse implements IServerDatagram {
	
	private char mark = 'N';

	private int length ;
	
	private byte code;
	
	private String msg;

	@Override
	public byte[] toByteArrays() {
		if (length == 0) {
			reviseLength();
		}
		ByteBuffer buffer = ByteBuffer.allocate(size());
		buffer.put((byte) mark);
		buffer.putInt(length);
		buffer.put(code);
		if (code != 0) {
			buffer.put(ByteUtils.stringToBytes(msg, true));
		}
		return buffer.array();
	}

	@Override
	public int size() {
		return length + 1;
	}

	@Override
	public void reviseLength() {
		int _length = 4 + 1;
		if(code != 0){
			_length += ByteUtils.getStringLength(msg);
		}
		this.length  = _length;		
	}
	
	
	

}
