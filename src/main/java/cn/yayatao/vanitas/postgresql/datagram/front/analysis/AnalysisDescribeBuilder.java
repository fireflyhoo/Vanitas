package cn.yayatao.vanitas.postgresql.datagram.front.analysis;

import java.io.IOException;
import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.Datagram;
import cn.yayatao.vanitas.postgresql.datagram.IBuilder;
import cn.yayatao.vanitas.postgresql.datagram.front.Describe;
import cn.yayatao.vanitas.postgresql.datagram.front.Describe.DescribeType;
import cn.yayatao.vanitas.postgresql.utils.PIOUtils;

public class AnalysisDescribeBuilder implements IBuilder {

	@Override
	public Datagram build(byte[] data) {
		Describe describe = new Describe();
		ByteBuffer buffer = ByteBuffer.wrap(data);
		describe.setMark((char) buffer.get());
		describe.setLength(buffer.getInt());
		describe.setType(((char) buffer.get() == 'S') ? DescribeType.precompile_statement : DescribeType.entrance);
		try {
			describe.setName(PIOUtils.redString(buffer, UTF_8));
		} catch (IOException e) {
			throw new IllegalArgumentException("can't analysis Describe package");
		}
		return describe;
	}

}
