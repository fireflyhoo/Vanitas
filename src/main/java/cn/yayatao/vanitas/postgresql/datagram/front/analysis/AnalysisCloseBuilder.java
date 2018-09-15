package cn.yayatao.vanitas.postgresql.datagram.front.analysis;

import java.io.IOException;
import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.Datagram;
import cn.yayatao.vanitas.postgresql.datagram.IBuilder;
import cn.yayatao.vanitas.postgresql.datagram.front.Close;
import cn.yayatao.vanitas.postgresql.datagram.front.Close.CloseType;
import cn.yayatao.vanitas.postgresql.utils.PIOUtils;

public class AnalysisCloseBuilder implements IBuilder{

	@Override
	public Datagram build(byte[] data) {
		Close close = new Close();
		ByteBuffer buffer = ByteBuffer.wrap(data);
		close.setMark((char)buffer.get());
		close.setLength(buffer.getInt());
		close.setType('S'== (char)buffer.get()? CloseType.precompile_statement: CloseType.entrance);
		try {
			close.setName(PIOUtils.redString(buffer, UTF_8));
		} catch (IOException e) {
			throw new IllegalArgumentException("can't analysis close package", e);
		}
		
		return close;
	}

}
