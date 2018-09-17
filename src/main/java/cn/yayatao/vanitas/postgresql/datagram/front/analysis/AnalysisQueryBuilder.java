package cn.yayatao.vanitas.postgresql.datagram.front.analysis;

import java.io.IOException;
import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.Datagram;
import cn.yayatao.vanitas.postgresql.datagram.IBuilder;
import cn.yayatao.vanitas.postgresql.datagram.front.Query;
import cn.yayatao.vanitas.postgresql.utils.PIOUtils;

public class AnalysisQueryBuilder implements IBuilder {

	@Override
	public Datagram build(byte[] data) {
		ByteBuffer buffer = ByteBuffer.wrap(data);
		char mark = (char)buffer.get();
		Query query = new Query();
		query.setMark(mark);
		query.setLength(buffer.getInt());
		try {
			query.setSql(PIOUtils.redString(buffer, UTF_8));
		} catch (IOException e) {
			throw new IllegalArgumentException("can't analysis  Query package");
		}
		return query;
	}

}
