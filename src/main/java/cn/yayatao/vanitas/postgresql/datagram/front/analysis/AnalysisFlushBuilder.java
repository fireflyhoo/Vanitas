package cn.yayatao.vanitas.postgresql.datagram.front.analysis;

import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.Datagram;
import cn.yayatao.vanitas.postgresql.datagram.IBuilder;
import cn.yayatao.vanitas.postgresql.datagram.front.Flush;

public class AnalysisFlushBuilder implements IBuilder {

	@Override
	public Datagram build(byte[] data) {
		Flush flush = new Flush();
		ByteBuffer buffer = ByteBuffer.wrap(data);
		flush.setMark((char)buffer.get());
		flush.setLength(buffer.getInt());
		return flush;
	}
}
