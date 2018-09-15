package cn.yayatao.vanitas.postgresql.datagram.front;

import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.general.ByteUtils;

//		Close (F)
//		Byte1('C')
//		标识这条消息是一个 Close 命令。
//		
//		Int32
//		以字节计的消息内容长度，包括长度本身。
//		
//		Byte1
//		'S'关闭一个准备的语句；或者'P'关闭一个入口。
//		
//		String
//		一个要关闭的预备语句或者入口的名字(一个空字符串选择未命名的预备语句或者入口)。
public class Close implements IFrontDatagram {
	private char mark;

	private int length;

	private CloseType type;

	private String name;

	public static enum CloseType {
		/***
		 * 预编译语句
		 */
		precompile_statement('S'),

		entrance('P');

		private char value;

		private CloseType(char value) {
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

	public CloseType getType() {
		return type;
	}

	public void setType(CloseType type) {
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
		if (length <= 0) {
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
				+ 1 /* close type */
				+ ByteUtils.getStringLength(name);
		this.length = currLength;
	}
}
