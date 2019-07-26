package cn.yayatao.vanitas.postgresql.datagram.front;

import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.IFrontDatagram;
import cn.yayatao.vanitas.postgresql.datagram.general.ByteUtils;

//		Describe (F)
//		Byte1('D')
//		标识消息是一个 Describe(描述)命令。
//		
//		Int32
//		以字节记的消息内容的长度，包括字节本身。
//		
//		Byte1
//		'S'描述一个预备语句；或者'P'描述一个入口。
//		
//		String
//		要描述的预备语句或者入口的名字(或者一个空字符串，就会选取未命名的预备语句或者入口)。
public class Describe implements IFrontDatagram {

	private char mark = 'D';

	private int length;

	private DescribeType type;

	private String name;

	public static enum DescribeType {
		/***
		 * 预编译语句
		 */
		precompile_statement('S'),

		/**
		 * 入口
		 */
		entrance('P');

		private char value;

		public char getValue() {
			return value;
		}

		private DescribeType(char value) {
			this.value = value;
		}
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

	public DescribeType getType() {
		return type;
	}

	public void setType(DescribeType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public byte[] toByteArrays() {
		if (this.length <= 0) {
			reviseLength();
		}
		ByteBuffer buffer = ByteBuffer.allocate(size());
		buffer.put((byte) mark);
		buffer.putInt(length);
		buffer.put((byte) type.value);
		buffer.put(ByteUtils.stringToBytes(name, true));
		return buffer.array();
	}

	@Override
	public int size() {
		return length + 1;
	}

	@Override
	public void reviseLength() {
		int currLength = 4/* length self */
				+ 1/* type */
				+ ByteUtils.getStringLength(name);
		this.length = currLength;
	}

}
