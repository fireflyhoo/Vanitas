package cn.yayatao.vanitas.postgresql.datagram.front.analysis;

import java.io.IOException;
import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.Datagram;
import cn.yayatao.vanitas.postgresql.datagram.IBuilder;
import cn.yayatao.vanitas.postgresql.datagram.front.Parse;
import cn.yayatao.vanitas.postgresql.utils.PIOUtils;

public class AnalysisParseBuilder implements IBuilder {

	@Override
	public Datagram build(byte[] data) {
		ByteBuffer buffer = ByteBuffer.wrap(data);

		char mark = (char) buffer.get();
		Parse parse = new Parse();
		parse.setMark(mark);
		parse.setLength(buffer.getInt());
		try {
			parse.setName(PIOUtils.redString(buffer, UTF_8));
			parse.setSql(PIOUtils.redString(buffer, UTF_8));
		} catch (IOException e) {
			new IllegalArgumentException("can't analsis Parse package");
		}
		parse.setArgumentTypeOidsNumber(buffer.getShort());
		int[] argumentTypeOids = new int[parse.getArgumentTypeOidsNumber()];
		for (int i = 0; i < parse.getArgumentTypeOidsNumber(); i++) {
			argumentTypeOids[i] = buffer.getInt();
		}
		parse.setArgumentTypeOids(argumentTypeOids);

		return parse;
	}

}
