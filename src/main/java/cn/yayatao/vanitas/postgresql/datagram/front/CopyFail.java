package cn.yayatao.vanitas.postgresql.datagram.front;

import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.IFrontDatagram;
import cn.yayatao.vanitas.postgresql.datagram.general.ByteUtils;

//		CopyFail (F)
//		Byte1('f')
//		标识这条消息是一个 COPY 失败指示器。
//		
//		Int32
//		以字节记的消息内容的长度，包括长度本身。
//		
//		String
//		一个报告失败原因的错误信息。
public class CopyFail implements IFrontDatagram {
	private char mark = 'f';

	private int length;

	private String failReason;

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

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	@Override
	public byte[] toByteArrays() {
		if (this.length <= 0) {
			reviseLength();
		}
		ByteBuffer buffer = ByteBuffer.allocate(size());
		buffer.put((byte) mark);
		buffer.putInt(length);
		buffer.put(ByteUtils.stringToBytes(failReason, true));
		return buffer.array();
	}

	@Override
	public int size() {
		return length + 1;
	}

	@Override
	public void reviseLength() {
		int currLength = 4/* length self */
				+ ByteUtils.getStringLength(failReason);
		this.length = currLength;
	}
}
