package cn.yayatao.vanitas.postgresql.datagram.server;

import cn.yayatao.vanitas.postgresql.datagram.IServerDatagram;

import java.nio.ByteBuffer;

//		CopyOutResponse (B)
//		Byte1('H')
//		标识这条消息是一条 StartCopyOut(开始拷贝进出)响应消息。这条消息后面将跟着一条拷贝出数据消息。
//		
//		Int32
//		以字节记的消息内容的长度，包括它自己。
//		
//		Int8
//		0 表示全部拷贝格式都是文本(数据行由换行符分隔，字段由分隔字符分隔等等)。1 表示所有拷贝格式都是二进制的(类似于 DataRow 格式)。参阅 COPY 获取更多信息。
//		
//		Int16
//		要拷贝的数据的字段的数目(在下面的 N 说明)。
//		
//		Int16[N]
//		每个字段要试用的格式代码。目前每个都必须是零(文本)或者一(二进制)。如果全部的拷贝格式都是文本，那么所有的都必须是零。
public class CopyOutResponse implements IServerDatagram {
	private char mark = 'H';

	private int length;
	
	private byte format;
	
	private short columnsNumber;
	
	private short[] columnTypes;

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

	public byte getFormat() {
		return format;
	}

	public void setFormat(byte format) {
		this.format = format;
	}

	public short getColumnsNumber() {
		return columnsNumber;
	}

	public void setColumnsNumber(short columnsNumber) {
		this.columnsNumber = columnsNumber;
	}

	public short[] getColumnTypes() {
		return columnTypes;
	}

	public void setColumnTypes(short[] columnTypes) {
		this.columnTypes = columnTypes;
	}

	@Override
	public byte[] toByteArrays() {
		if(length == 0){
			reviseLength();
		}
		ByteBuffer buffer = ByteBuffer.allocate(size());
		buffer.put((byte)mark);
		buffer.putInt(length);
		buffer.put(format);
		buffer.putShort(columnsNumber);
		if(columnTypes != null){
			for(short typ : columnTypes){
				buffer.putShort(typ);
			}
		}
		return buffer.array();
	}

	@Override
	public int size() {
		
		return length +1;
	}

	@Override
	public void reviseLength() {
		this.length = 4 /*length*/ + 1/*format*/ + 2 /*columnsNumber*/ + 2*columnsNumber /*columnTypes*/;
	}
}
