package cn.yayatao.vanitas.postgresql.datagram.front;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import cn.yayatao.vanitas.postgresql.datagram.IFrontDatagram;
import cn.yayatao.vanitas.postgresql.datagram.general.ByteUtils;

//CancelRequest (F)
//Int32(16)
//以字节计的消息长度。包括长度本身。
//
//Int32(80877102)
//取消请求代码。选这个值是为了在高16位包含 1234， 低16位包含 5678。（为避免混乱，这个代码必须与协议版本号不同．）
//
//Int32
//目标后端的进程号（PID）。
//
//Int32
//目标后端的密钥（secret key）。
public class CancelRequest implements IFrontDatagram {
	private int length;
	private int cancelCode;
	private int pid;
	private int secretKey;

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getCancelCode() {
		return cancelCode;
	}

	public void setCancelCode(int cancelCode) {
		this.cancelCode = cancelCode;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(int secretKey) {
		this.secretKey = secretKey;
	}

	@Override
	public byte[] toByteArrays() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			out.write(ByteUtils.intToBytes(length));
			out.write(ByteUtils.intToBytes(cancelCode));
			out.write(ByteUtils.intToBytes(pid));
			out.write(ByteUtils.intToBytes(secretKey));
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
	}
}
