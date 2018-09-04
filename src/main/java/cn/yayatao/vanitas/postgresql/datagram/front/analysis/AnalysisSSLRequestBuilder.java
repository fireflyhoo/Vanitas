package cn.yayatao.vanitas.postgresql.datagram.front.analysis;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import javax.net.ssl.SSLContext;

import cn.yayatao.vanitas.postgresql.datagram.Datagram;
import cn.yayatao.vanitas.postgresql.datagram.IBuilder;
import cn.yayatao.vanitas.postgresql.datagram.front.SSLRequest;

public class AnalysisSSLRequestBuilder implements IBuilder {

	@Override
	public Datagram build(byte[] data) {
		SSLRequest request = new SSLRequest();
		ByteBuffer buf = ByteBuffer.wrap(data);
		request.setLength(buf.getInt());
		request.setSslMagic(buf.getInt());
		return request;
	}

}
