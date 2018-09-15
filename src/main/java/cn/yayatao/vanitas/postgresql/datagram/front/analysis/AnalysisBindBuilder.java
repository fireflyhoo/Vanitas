package cn.yayatao.vanitas.postgresql.datagram.front.analysis;

import java.io.IOException;
import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.Datagram;
import cn.yayatao.vanitas.postgresql.datagram.IBuilder;
import cn.yayatao.vanitas.postgresql.datagram.front.Bind;
import cn.yayatao.vanitas.postgresql.datagram.front.Bind.Argument;
import cn.yayatao.vanitas.postgresql.utils.PIOUtils;
/***
 * 绑定参数包
 * @author fireflyhoo
 *
 */
public class AnalysisBindBuilder implements IBuilder {

	@Override
	public Datagram build(byte[] data) {

		Bind bind = new Bind();
		ByteBuffer buffer = ByteBuffer.wrap(data);
		bind.setMark((char) buffer.get());
		bind.setLength(buffer.getInt());

		try {
			String targetName = PIOUtils.redString(buffer, UTF_8);
			String sourceName = PIOUtils.redString(buffer, UTF_8);
			bind.setSourceName(sourceName);
			bind.setTargetName(targetName);
		} catch (IOException e) {
			throw new IllegalArgumentException("can't analysis bind packet", e);
		}

		bind.setArgumentTypesNumber(buffer.getShort());

		short[] argumentTypes = new short[buffer.getShort()];
		for (short i = 0; i < bind.getArgumentTypesNumber(); i++) {
			argumentTypes[i] = buffer.getShort();
		}
		bind.setArgumentTypes(argumentTypes);
		bind.setArgumentsNubmer(buffer.getShort());
		Argument[] arguments = new Argument[bind.getArgumentTypesNumber()];
		for (int i = 0; i < arguments.length; i++) {
			Argument argument = new Argument();
			argument.setLength(buffer.getInt());
			byte[] _data = new byte[argument.getLength()];
			buffer.get(_data);
			argument.setData(_data);
			arguments[i] = argument;
		}
		bind.setArguments(arguments);

		short returnFieldTypesNumber = buffer.getShort();
		bind.setReturnFieldTypesNumber(returnFieldTypesNumber);
		short[] returnFieldTypes = new short[returnFieldTypesNumber];
		for (short i = 0; i < returnFieldTypesNumber; i++) {
			returnFieldTypes[i] = buffer.getShort();
		}
		bind.setReturnFieldTypes(returnFieldTypes);
		if (buffer.hasRemaining()) {
			throw new IllegalArgumentException("this frame data is to long");
		}
		return bind;
	}

}
