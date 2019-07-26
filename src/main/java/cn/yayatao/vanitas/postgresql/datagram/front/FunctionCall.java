package cn.yayatao.vanitas.postgresql.datagram.front;

import cn.yayatao.vanitas.postgresql.datagram.IFrontDatagram;

import java.nio.ByteBuffer;

//		FunctionCall (F)
//		Byte1('F')
//		标识消息是一个函数调用。
//		
//		Int32
//		以字节记的消息内容的长度，包括长度本身。
//		
//		Int32
//		声明待调用的函数的对象标识(OID)。
//		
//		Int16
//		后面跟着的参数格式代码的数目(用下面的 C 表示)。它可以是零，表示没有参数，或者是所有参数都试用缺省格式(文本)；或者是一，这种情况下声明的格式代码应用于所有参数；或者它可以等于参数的实际个数。
//		
//		Int16[C]
//		参数格式代码。目前每个必须是零(文本)或者一(二进制)。
//		
//		Int16
//		声明提供给函数的参数个数
//		
//		然后，每个参数都出现下面字段对：
//		
//				Int32
//				以字节记的参数值的长度(不包括长度自己)。可以为零。一个特殊的例子是，-1 表示一个 NULL 参数值。如果是 NULL ，则没有参数字节跟在后面。
//				
//				Byten
//				参数的值，格式是用相关的格式代码表示的。n 是上面的长度。
//		
//		在最后一个参数之后，出现下面的字段：
//		
//		Int16
//		函数结果的格式代码。目前必须是零(文本)或者一(二进制)。
public class FunctionCall implements IFrontDatagram {
	private char mark = 'E';

	private int length;

	private int oid;

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

	/***
	 * 参数
	 */
	private Argument[] arguments;

	/**
	 * 返回类型
	 */
	private short returnType;

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

	public int getOid() {
		return oid;
	}

	public void setOid(int oid) {
		this.oid = oid;
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

	public short getArgumentsNubmer() {
		return argumentsNubmer;
	}

	public void setArgumentsNubmer(short argumentsNubmer) {
		this.argumentsNubmer = argumentsNubmer;
	}

	public Argument[] getArguments() {
		return arguments;
	}

	public void setArguments(Argument[] arguments) {
		this.arguments = arguments;
	}

	public short getReturnType() {
		return returnType;
	}

	public void setReturnType(short returnType) {
		this.returnType = returnType;
	}

	@Override
	public byte[] toByteArrays() {
		if (this.length <= 0) {
			reviseLength();
		}
		ByteBuffer buffer = ByteBuffer.allocate(size());
		buffer.put((byte) mark);
		buffer.putInt(length);
		buffer.putInt(oid);
		buffer.putShort(argumentTypesNumber);
		for (int i = 0; i < argumentTypesNumber; i++) {
			buffer.putShort(argumentTypes[i]);
		}
		buffer.putShort(argumentsNubmer);
		for (int i = 0; i < argumentsNubmer; i++) {
			buffer.putInt(arguments[i].length);
			buffer.put(arguments[i].data);
		}
		buffer.putShort(returnType);
		return buffer.array();
	}

	@Override
	public int size() {
		return length + 1;
	}

	@Override
	public void reviseLength() {
		int currLength = 4 /* length self */
				+ 4 /* oid */
				+ 2 /* argumentTypesNumber */
				+ argumentTypesNumber * 2 /* argumentTypes */
				+ 2; /* argumentsNubmer */
		for (int i = 0; i < argumentsNubmer; i++) {
			currLength += 4;
			currLength += arguments[i].length;
		}
		currLength += 4; /* return type */

		this.length = currLength;
	}

}
