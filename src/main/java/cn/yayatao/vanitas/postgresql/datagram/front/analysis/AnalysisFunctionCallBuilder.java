package cn.yayatao.vanitas.postgresql.datagram.front.analysis;

import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.Datagram;
import cn.yayatao.vanitas.postgresql.datagram.IBuilder;
import cn.yayatao.vanitas.postgresql.datagram.front.Argument;
import cn.yayatao.vanitas.postgresql.datagram.front.FunctionCall;

public class AnalysisFunctionCallBuilder implements IBuilder {

	@Override
	public Datagram build(byte[] data) {
		FunctionCall call = new FunctionCall();
		ByteBuffer buffer = ByteBuffer.wrap(data);
		call.setMark((char) buffer.get());
		call.setLength(buffer.getInt());
		call.setOid(buffer.getInt());
		call.setArgumentTypesNumber(buffer.getShort());
		short[] argumentTypes = new short[call.getArgumentTypesNumber()];
		for (int i = 0; i < argumentTypes.length; i++) {
			argumentTypes[i] = buffer.getShort();
		}
		call.setArgumentTypes(argumentTypes);
		call.setArgumentsNubmer(buffer.getShort());
		Argument[] arguments = new Argument[call.getArgumentsNubmer()];
		for (int i = 0; i < arguments.length; i++) {
			Argument argument = new Argument();
			argument.setLength(buffer.getInt());
			byte[] _data = new byte[argument.getLength()];
			buffer.get(_data);
			argument.setData(_data);
			arguments[i] = argument;
		}
		call.setArguments(arguments);

		call.setReturnType(buffer.getShort());
		return call;
	}

}
