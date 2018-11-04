package cn.yayatao.vanitas.postgresql.datagram.server;

import java.nio.ByteBuffer;

//		
//		CopyInResponse (B)
//		Byte1('G')
//		标识这条消息是一条 StartCopyIn(开始拷贝进入)响应消息。前端现在必须发送一条拷贝入数据。如果还没准备好做这些事情，那么发送一条 CopyFail 消息。
//		
//		Int32
//		以字节记的消息内容的长度，包括长度本身。
//		
//		Int8
//		0 表示全部的 COPY 格式都是文本的(数据行由换行符分隔，字段由分隔字符分隔等等)。1 表示都是二进制的(类似 DataRow 格式)。参阅 COPY 获取更多信息。
//		
//		Int16
//		数据中要拷贝的字段数(由下面的 N 解释)。
//		
//		Int16[N]
//		每个字段将要用的格式代码，目前每个都必须是零(文本)或者一(二进制)。如果全部拷贝格式都是文本的，那么所有的都必须是零。
public class CopyInResponse implements IServerDatagram{

	private char mark = 'G';

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
