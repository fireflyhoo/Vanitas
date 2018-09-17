package cn.yayatao.vanitas.postgresql.datagram.front.analysis;

import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.Datagram;
import cn.yayatao.vanitas.postgresql.datagram.IBuilder;
import cn.yayatao.vanitas.postgresql.datagram.front.Terminate;

public class AnalysisTerminateBuilder implements IBuilder {

	@Override
	public Datagram build(byte[] data) {
		Terminate terminate = new Terminate();

		ByteBuffer buffer = ByteBuffer.wrap(data);
		terminate.setMark((char) buffer.get());
		terminate.setLength(buffer.getInt());
		return terminate;
	}

}
