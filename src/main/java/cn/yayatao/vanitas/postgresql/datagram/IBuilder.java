package cn.yayatao.vanitas.postgresql.datagram;

import java.nio.charset.Charset;

public interface IBuilder {
	
	public static final Charset UTF_8 = Charset.forName("UTF-8");
	
	public Datagram build(byte[] data);
}
