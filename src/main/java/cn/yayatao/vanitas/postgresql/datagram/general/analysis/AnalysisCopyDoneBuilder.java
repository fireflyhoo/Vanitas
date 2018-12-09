package cn.yayatao.vanitas.postgresql.datagram.general.analysis;

import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.Datagram;
import cn.yayatao.vanitas.postgresql.datagram.IBuilder;
import cn.yayatao.vanitas.postgresql.datagram.general.CopyDone;

public class AnalysisCopyDoneBuilder implements IBuilder {

	@Override
	public Datagram build(byte[] data) {

		ByteBuffer buffer = ByteBuffer.wrap(data);
		char mark = (char) buffer.get();
		CopyDone copyDone = new CopyDone();
		copyDone.setMark(mark);
		copyDone.setLength(buffer.getInt());
		return copyDone;
	}

}
