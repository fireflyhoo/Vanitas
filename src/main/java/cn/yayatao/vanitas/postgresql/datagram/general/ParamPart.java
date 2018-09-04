package cn.yayatao.vanitas.postgresql.datagram.general;

/**
 * 启动报文参数
 * 
 * @author Huyahui
 *
 */
public class ParamPart {
	String key;
	String value;

	public ParamPart(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
