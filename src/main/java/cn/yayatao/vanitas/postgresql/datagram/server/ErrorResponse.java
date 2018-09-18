package cn.yayatao.vanitas.postgresql.datagram.server;

import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.general.ByteUtils;

//		ErrorResponse (B)
//		Byte1('E')
//		标识消息是一条错误。
//		
//		Int32
//		以字节记的消息内容的长度，包括长度本身。
//		
//		消息体由一个或多个标识出来的字段组成，后面跟着一个字节零作为终止符。 字段可以以任何顺序出现。对于每个字段都有下面的东西：
//		
//		Byte1
//		一个标识字段类型的代码；如果为零，这就是消息终止符并且不会跟着有字串。 目前定义的字段类型在 Section 43.5 列出。 因为将来可能增加更多的字段类型，所以前端应该不声不响地忽略不认识类型的字段。
//		
//		String
//		字段值。
public class ErrorResponse implements IServerDatagram {

	private char mark = 'E';

	private int length;

	private byte code;

	private String errReason;

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

	public byte getCode() {
		return code;
	}

	public void setCode(byte code) {
		this.code = code;
	}

	public String getErrReason() {
		return errReason;
	}

	public void setErrReason(String errReason) {
		this.errReason = errReason;
	}

	@Override
	public byte[] toByteArrays() {
		if (length <= 0) {
			reviseLength();
		}
		ByteBuffer buffer = ByteBuffer.allocate(size());
		buffer.put((byte) mark);
		buffer.putInt(length);
		buffer.put(code);
		if (code != 0) {
			buffer.put(ByteUtils.stringToBytes(errReason, true));
		}
		return buffer.array();
	}

	@Override
	public int size() {
		return length + 1;
	}

	@Override
	public void reviseLength() {
		int currLength = 4 /* length self */
				+ 1;/* code */
		if (code != 0) {
			currLength += ByteUtils.getStringLength(errReason);
		}
		this.length = currLength;
	}
}
