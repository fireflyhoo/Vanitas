package cn.yayatao.vanitas.postgresql.datagram.general.analysis;

import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.Datagram;
import cn.yayatao.vanitas.postgresql.datagram.IBuilder;
import cn.yayatao.vanitas.postgresql.datagram.general.CopyData;

public class AnalysisCopyDataBuilder implements IBuilder {

	@Override
	public Datagram build(byte[] data) {
		CopyData copyData = new CopyData();
		ByteBuffer buffer = ByteBuffer.wrap(data);
		copyData.setMark((char) buffer.get());
		copyData.setLength(buffer.getInt());
		byte[] dst = new byte[copyData.getLength() - 4];
		buffer.get(dst);
		copyData.setData(dst);
		return copyData;
	}

}
