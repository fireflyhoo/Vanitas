package cn.yayatao.vanitas.postgresql.datagram.front.analysis;

import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.Datagram;
import cn.yayatao.vanitas.postgresql.datagram.IBuilder;
import cn.yayatao.vanitas.postgresql.datagram.front.Sync;

public class AnalysisSyncBuilder implements IBuilder {

	@Override
	public Datagram build(byte[] data) {
		Sync sync = new Sync();
		ByteBuffer buffer = ByteBuffer.wrap(data);
		sync.setMark((char) buffer.get());
		sync.setLength(buffer.getInt());
		return sync;
	}

}
