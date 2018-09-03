package cn.yayatao.vanitas.postgresql.packet;

import java.util.List;

public class StartupMessage extends PostgreSQLPacket {
	public static class ParamsItme {

		String key;

		String value;
		
		public ParamsItme(String key, String value) {
			super();
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
	private int length;
	private int major; // 协议主版本

	private char marker = PacketMarker.F_StartupMessage.getValue(); // 标准

	private int minor; // 协议子版本

	public List<ParamsItme> params; // 协议参数

	@Override
	public int getLength() {
		return length;
	}

	public int getMajor() {
		return major;
	}

	@Override
	@Deprecated
	public char getMarker() {
		return marker;
	}

	public int getMinor() {
		return minor;
	}

	public List<ParamsItme> getParams() {
		return params;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setMajor(int major) {
		this.major = major;
	}

	public void setMarker(char marker) {
		this.marker = marker;
	}

	public void setMinor(int minor) {
		this.minor = minor;
	}

	public void setParams(List<ParamsItme> params) {
		this.params = params;
	}
}
