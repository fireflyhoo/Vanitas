package cn.yayatao.vanitas.postgresql.datagram.front.analysis;

import java.io.IOException;
import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.Datagram;
import cn.yayatao.vanitas.postgresql.datagram.IBuilder;
import cn.yayatao.vanitas.postgresql.datagram.front.PasswordMessage;
import cn.yayatao.vanitas.postgresql.utils.PIOUtils;

public class AnalysisPasswordMessageBuilder implements IBuilder {

	@Override
	public Datagram build(byte[] data) {
		ByteBuffer buffer = ByteBuffer.wrap(data);

		PasswordMessage message = new PasswordMessage();
		message.setMark((char) buffer.get());
		message.setLength(buffer.getInt());
		try {
			message.setPassword(PIOUtils.redString(buffer, UTF_8));
		} catch (IOException e) {
			throw new IllegalArgumentException("can't analysis PasswrodMessage package");
		}
		return message;
	}

}
