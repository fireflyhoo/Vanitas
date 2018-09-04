package cn.yayatao.vanitas.postgresql.datagram.front;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import cn.yayatao.vanitas.postgresql.datagram.general.ByteUtils;
import cn.yayatao.vanitas.postgresql.datagram.general.ParamPart;

public class StartupMessage implements IFrontDatagram {

	private int length;
	private int major; // 协议主版本
	private int minor; // 协议子版本

	private List<ParamPart> params;

	@Override
	public int size() {
		if (length > 0) {
			return length;
		}
		return length;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getMajor() {
		return major;
	}

	public void setMajor(int major) {
		this.major = major;
	}

	public int getMinor() {
		return minor;
	}

	public void setMinor(int minor) {
		this.minor = minor;
	}

	public List<ParamPart> getParams() {
		return params;
	}

	public void setParams(List<ParamPart> params) {
		this.params = params;
	}

	@Override
	public byte[] toByteArrays() {
		ByteArrayOutputStream out = new ByteArrayOutputStream(length);
		try {
			fillDatagram(out);
			if (length != out.size()) {
				throw new IllegalArgumentException("datagram length is invalid");
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("this StartupMessage datagram can`t serialize", e);
		}
		return out.toByteArray();
	}

	@Override
	public void reviseLength() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int _length = out.size();
		this.length = _length;
	}

	private void fillDatagram(ByteArrayOutputStream out) throws IOException {
		out.write(ByteUtils.intToBytes(length));
		out.write(ByteUtils.shortToByte((short) major));
		out.write(ByteUtils.shortToByte((short) minor));
		for (ParamPart part : params) {
			out.write(ByteUtils.stringToBytes(part.getKey(), true));
			out.write(ByteUtils.stringToBytes(part.getValue(), true));
		}
	}
}
