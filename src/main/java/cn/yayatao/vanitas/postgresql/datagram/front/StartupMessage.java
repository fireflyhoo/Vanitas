package cn.yayatao.vanitas.postgresql.datagram.front;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import cn.yayatao.vanitas.postgresql.datagram.IFrontDatagram;
import cn.yayatao.vanitas.postgresql.datagram.general.ByteUtils;
import cn.yayatao.vanitas.postgresql.datagram.general.ParamPart;

//		StartupMessage (F)
//		Int32
//		以字节计的消息内容长度，包括长度本身。
//		
//		Int32(196608)
//		协议版本号。高 16 位是主版本号(对这里描述的协议而言是 3)。低 16 位是次版本号(对于这里描述的协议而言是 0)。
//		
//		协议版本号后面跟着一个或多个参数名和值字符串的配对。要求在最后一个名字/数值对后面有个字节零。参数可以以任意顺序出现。user 是必须的，其它都是可选的。每个参数都是这样声明的：
//		
//		String
//		参数名。目前可以识别的名字是：
//		
//		user
//		用于连接的数据库用户名。必须；无缺省。
//		
//		database
//		要连接的数据库。缺省是用户名。
//		
//		options
//		给后端的命令行参数。这个特性已经废弃，更好的方法是设置单独的运行时参数。
//		
//		除了上面的外，在后端启动的时候可以设置的任何运行时参数都可以列出来。这样的设置将在后端启动的时候附加(在分析了命令行参数之后，如果有的话)。这些值将成为会话缺省。
//		
//		String
//		参数值

public class StartupMessage implements IFrontDatagram {

	private int length;
	private int major; // 协议主版本
	private int minor; // 协议子版本

	private List<ParamPart> params;

	@Override
	public int size() {
		if (length > 0) {
			return length;
		}
		return length;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getMajor() {
		return major;
	}

	public void setMajor(int major) {
		this.major = major;
	}

	public int getMinor() {
		return minor;
	}

	public void setMinor(int minor) {
		this.minor = minor;
	}

	public List<ParamPart> getParams() {
		return params;
	}

	public void setParams(List<ParamPart> params) {
		this.params = params;
	}

	@Override
	public byte[] toByteArrays() {
		ByteArrayOutputStream out = new ByteArrayOutputStream(length);
		try {
			fillDatagram(out);
			if (length != out.size()) {
				throw new IllegalArgumentException("datagram length is invalid");
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("this StartupMessage datagram can`t serialize", e);
		}
		return out.toByteArray();
	}

	@Override
	public void reviseLength() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			fillDatagram(out);
		} catch (IOException e) {
		}
		int _length = out.size();
		this.length = _length;
	}

	private void fillDatagram(ByteArrayOutputStream out) throws IOException {
		out.write(ByteUtils.intToBytes(length));
		out.write(ByteUtils.shortToByte((short) major));
		out.write(ByteUtils.shortToByte((short) minor));
		for (ParamPart part : params) {
			out.write(ByteUtils.stringToBytes(part.getKey(), true));
			out.write(ByteUtils.stringToBytes(part.getValue(), true));
		}
	}
}
