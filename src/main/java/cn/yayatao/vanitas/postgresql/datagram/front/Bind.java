package cn.yayatao.vanitas.postgresql.datagram.front;

import cn.yayatao.vanitas.postgresql.datagram.IFrontDatagram;
import cn.yayatao.vanitas.postgresql.datagram.general.ByteUtils;

import java.nio.ByteBuffer;

//bind (F)
//Byte1('B')
//标识该信息是一个绑定命令
//
//Int32
//以字节记的消息内容的长度，包括长度本身。
//
//String
//目标入口的名字(空字符串则选取未命名的入口)
//
//String
//源预备语句的名字(空字符串则选取未命名的预备语句)
//
//Int16
//后面跟着的参数格式代码的数目(在下面的 C 中说明)。这个数值可以是零，表示没有参数，或者是参数都使用缺省格式(文本)；或者是一，这种情况下声明的格式代码应用于所有参数；或者它可以等于实际数目的参数。
//
//Int16[C]
//参数格式代码。目前每个都必须是零(文本)或者一(二进制)。
//
//Int16
//后面跟着的参数值的数目(可能为零)。这些必须和查询需要的参数个数匹配。
//
//然后，每个参数都会出现下面的字段对：
//		
//		Int32
//		参数值的长度，以字节记(这个长度并不包含长度本身)。可以为零。一个特殊的情况是，-1 表示一个 NULL 参数值。在 NULL 的情况下，后面不会跟着数值字节。
//		
//		Byten
//		参数值，格式是关联的格式代码标明的。n 是上面的长度。
//
//在最后一个参数之后，出现下面的字段：
//
//Int16
//后面跟着的结果字段格式代码数目(下面的 R 描述)。这个数目可以是零表示没有结果字段，或者结果字段都使用缺省格式(文本)；或者是一，这种情况下声明格式代码应用于所有结果字段(如果有的话)；或者它可以等于查询的结果字段的实际数目。
//
//Int16[R]
//结果字段格式代码。目前每个必须是零(文本)或者一(二进制)。
public class Bind implements IFrontDatagram {

	private char mark = 'B';

	private int length;

	private String targetName;

	private String sourceName;

	/**
	 * 0: no arguments or arguments is text format 1: arguments is binary format
	 */
	private short argumentTypesNumber;

	/**
	 * 0: text format 1: binary format
	 */
	private short[] argumentTypes;

	/**
	 * 参数个数
	 */
	private short argumentsNubmer;

	private Argument[] arguments;

	private short returnFieldTypesNumber;

	/**
	 * 0: text format 1: binary format
	 */
	private short[] returnFieldTypes;

	

	public char getMark() {
		return mark;
	}

	public void setMark(char mark) {
		this.mark = mark;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public short getArgumentTypesNumber() {
		return argumentTypesNumber;
	}

	public void setArgumentTypesNumber(short argumentTypesNumber) {
		this.argumentTypesNumber = argumentTypesNumber;
	}

	public short[] getArgumentTypes() {
		return argumentTypes;
	}

	public void setArgumentTypes(short[] argumentTypes) {
		this.argumentTypes = argumentTypes;
	}

	public Argument[] getArguments() {
		return arguments;
	}

	public void setArguments(Argument[] arguments) {
		this.arguments = arguments;
	}

	public short getReturnFieldTypesNumber() {
		return returnFieldTypesNumber;
	}

	public void setReturnFieldTypesNumber(short returnFieldTypesNumber) {
		this.returnFieldTypesNumber = returnFieldTypesNumber;
	}

	public short[] getReturnFieldTypes() {
		return returnFieldTypes;
	}

	public void setReturnFieldTypes(short[] returnFieldTypes) {
		this.returnFieldTypes = returnFieldTypes;
	}

	public short getArgumentsNubmer() {
		return argumentsNubmer;
	}

	public void setArgumentsNubmer(short argumentsNubmer) {
		this.argumentsNubmer = argumentsNubmer;
	}

	@Override
	public byte[] toByteArrays() {
		if (length == 0) {
			reviseLength(); // 重新计算包长度
		}
		ByteBuffer buffer = ByteBuffer.allocate(size());
		buffer.put((byte) mark);
		buffer.putInt(length);
		buffer.put(ByteUtils.stringToBytes(targetName, true));
		buffer.put(ByteUtils.stringToBytes(sourceName, true));

		buffer.putShort(argumentTypesNumber);
		for (int i = 0; i < argumentTypesNumber; i++) {
			buffer.putShort(argumentTypes[i]);
		}
		buffer.putShort(argumentsNubmer);
		for (int i = 0; i < argumentsNubmer; i++) {
			buffer.putInt(arguments[i].length);
			buffer.put(arguments[i].data);
		}
		buffer.putShort(returnFieldTypesNumber);
		for (int i = 0; i < returnFieldTypesNumber; i++) {
			buffer.putShort(returnFieldTypes[i]);
		}

		return buffer.array();
	}

	@Override
	public int size() {
		return length + 1;
	}

	@Override
	public void reviseLength() {
		int currLength = 4 /* length */
				+ ByteUtils.getStringLength(sourceName) + ByteUtils.getStringLength(targetName)
				+ 2 /* argumentTypesNumber */
				+ argumentTypesNumber * 2 + 2 /* argumentsNubmer */;

		for (int i = 0; i < argumentsNubmer; i++) {
			currLength = currLength + arguments[i].getLength() + 4;
		}
		currLength = currLength + 2/* returnFieldTypesNumber */
				+ returnFieldTypesNumber * 2;
		this.length = currLength;
	}

}
