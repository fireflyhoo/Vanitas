package cn.yayatao.vanitas.postgresql.datagram.front.analysis;

import java.io.IOException;
import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.Datagram;
import cn.yayatao.vanitas.postgresql.datagram.IBuilder;
import cn.yayatao.vanitas.postgresql.datagram.front.Execute;
import cn.yayatao.vanitas.postgresql.utils.PIOUtils;

public class AnalysisExecuteBuilder implements IBuilder {

	@Override
	public Datagram build(byte[] data) {
		ByteBuffer buffer = ByteBuffer.wrap(data);
		Execute execute = new Execute();
		execute.setMark((char)buffer.get());
		execute.setLength(buffer.getInt());
		try {
			execute.setName(PIOUtils.redString(buffer, UTF_8));
		} catch (IOException e) {
			throw new IllegalArgumentException("can't analysis execute package");
		}
		execute.setMaxReturnRowSize(buffer.getInt());
		return execute;
	}

}
