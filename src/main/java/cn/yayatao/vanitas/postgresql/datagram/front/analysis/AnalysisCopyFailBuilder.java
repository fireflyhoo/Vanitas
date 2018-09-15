package cn.yayatao.vanitas.postgresql.datagram.front.analysis;

import java.io.IOException;
import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.Datagram;
import cn.yayatao.vanitas.postgresql.datagram.IBuilder;
import cn.yayatao.vanitas.postgresql.datagram.front.CopyFail;
import cn.yayatao.vanitas.postgresql.utils.PIOUtils;

public class AnalysisCopyFailBuilder implements IBuilder {

	@Override
	public Datagram build(byte[] data) {
		CopyFail copyFail = new CopyFail();
		ByteBuffer buffer = ByteBuffer.wrap(data);
		copyFail.setMark((char) buffer.get());
		copyFail.setLength(buffer.getInt());
		try {
			copyFail.setFailReason(PIOUtils.redString(buffer, UTF_8));
		} catch (IOException e) {
			throw new IllegalArgumentException("can't analysis CopyFail package");
		}
		return copyFail;
	}

}
