package cn.yayatao.vanitas.postgresql.datagram.front;

import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.IFrontDatagram;
import cn.yayatao.vanitas.postgresql.datagram.general.ByteUtils;

//		Query (F)
//		Byte1('Q')
//		标识消息是一个简单查询。
//		
//		Int32
//		以字节计的消息内容长度，包括长度本身。
//		
//		String
//		查询字符串自身
public class Query implements IFrontDatagram {

	private char mark = 'Q';

	private int length;

	private String sql;

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

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	@Override
	public byte[] toByteArrays() {
		if (length <= 0) {
			reviseLength();
		}
		ByteBuffer buffer = ByteBuffer.allocate(size());
		buffer.put((byte)mark);
		buffer.putInt(length);
		buffer.put(ByteUtils.stringToBytes(sql, true));
		return buffer.array();
	}

	@Override
	public int size() {
		return length + 1;
	}

	@Override
	public void reviseLength() {
		int currLength = 4 /* length self */
				+ ByteUtils.getStringLength(sql);
		this.length = currLength;
	}

}
