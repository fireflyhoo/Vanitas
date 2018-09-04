package cn.yayatao.vanitas.postgresql.datagram.front;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import cn.yayatao.vanitas.postgresql.datagram.general.ByteUtils;

//SSLRequest (F)
//Int32(8)
//以字节记的消息内容的长度，包括长度本身。
//
//Int32(80877103)
//SSL 请求码。选取的数值在高16位里包含 1234，在低16位里包含 5679。 （为了避免混淆，这个编码必须和任何协议版本号不同。）
public class SSLRequest implements IFrontDatagram {
	/***
	 * 长度
	 */
	private int length;

	/**
	 * SSL 魔数
	 */
	private int sslMagic;

	@Override
	public byte[] toByteArrays() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			out.write(ByteUtils.intToBytes(length));
			out.write(ByteUtils.intToBytes(sslMagic));
		} catch (IOException e) {
			throw new IllegalArgumentException("this SSLRequest datagram can`t serialize", e);
		}
		return out.toByteArray();
	}

	@Override
	public int size() {
		return length;
	}

	@Override
	public void reviseLength() {
		this.length = 8;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getSslMagic() {
		return sslMagic;
	}

	public void setSslMagic(int sslMagic) {
		this.sslMagic = sslMagic;
	}

}
