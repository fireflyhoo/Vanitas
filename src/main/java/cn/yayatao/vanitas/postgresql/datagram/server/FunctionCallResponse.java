package cn.yayatao.vanitas.postgresql.datagram.server;

import cn.yayatao.vanitas.postgresql.datagram.IServerDatagram;

import java.nio.ByteBuffer;

//		FunctionCallResponse (B)
//		Byte1('V')
//		标识这条消息是一个函数调用结果
//		
//		Int32
//		以字节计的消息内容长度，包括长度本身。
//		
//		Int32
//		以字节记的函数结果值的长度(不包括长度本身)。可以为零。一个特殊的情况是 -1 表示 NULL 函数结果。如果是 NULL 则后面没有数值字节跟随。
//		
//		Byten
//		函数结果的值，格式是相关联的格式代码标识的。n 是上面的长度。
public class FunctionCallResponse implements IServerDatagram {
	

	private char mark = 'V';

	private int length;
	
	// NULL is -1
	private int resultLength;
	
	private byte[] result;

	@Override
	public byte[] toByteArrays() {
		ByteBuffer buffer = ByteBuffer.allocate(size());
		buffer.put((byte)mark);
		buffer.putInt(length);
		buffer.putInt(resultLength);
		buffer.put(result);
		return buffer.array();
	}

	@Override
	public int size() {
		return length + 1;
	}

	@Override
	public void reviseLength() {
		int _length = 4/*length*/ + 4 /*resultLength*/ + result.length;
		this.length = _length;
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

	public int getResultLength() {
		return resultLength;
	}

	public void setResultLength(int resultLength) {
		this.resultLength = resultLength;
	}

	public byte[] getResult() {
		return result;
	}

	public void setResult(byte[] result) {
		this.result = result;
	}
	
	
}
