package cn.yayatao.vanitas.postgresql.datagram.front.analysis;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import cn.yayatao.vanitas.postgresql.datagram.Datagram;
import cn.yayatao.vanitas.postgresql.datagram.IBuilder;
import cn.yayatao.vanitas.postgresql.datagram.front.StartupMessage;
import cn.yayatao.vanitas.postgresql.datagram.general.ParamPart;
import cn.yayatao.vanitas.postgresql.utils.PIOUtils;



public class AnalysisStartupMessageBuilder implements IBuilder {

	@Override
	public Datagram build(byte[] data) {
		ByteBuffer buffer = ByteBuffer.wrap(data);
		StartupMessage startupMessage = new StartupMessage();
		startupMessage.setLength(buffer.getInt());
		startupMessage.setMajor(buffer.getShort());
		startupMessage.setMinor(buffer.getShort());
		int offset = 8;
		List<String> strs = new ArrayList<>();
		for (; offset < data.length;) {
			try {
				String str = PIOUtils.redString(buffer, offset, UTF_8);
				offset =offset + str.getBytes().length +1; //字符会多个 '/0' 分割
				strs.add(str);
			} catch (Exception e) {
			}
		}
		List<ParamPart> params = new ArrayList<>();
		for (int i = 0; i < strs.size(); i++) {
			if (i % 2 == 0 && (i + 1 < strs.size())) {

				ParamPart item = new ParamPart(strs.get(i), strs.get(i + 1));
				params.add(item);
			}
		}
		startupMessage.setParams(params);
		return startupMessage;
	}

}
