package cn.yayatao.vanitas.postgresql.datagram.general;

import java.nio.charset.Charset;

/***
 * 字节工具类
 * 
 * @author Huyahui
 *
 */
public class ByteUtils {

	private static final Charset UTF_8 = Charset.forName("utf-8");

	/***
	 * 短整型转成byte数组
	 * 
	 * @param x
	 * @return
	 */
	public static byte[] shortToByte(short x) {
		byte[] ret = new byte[2];
		ret[0] = (byte) (x & 0xff);
		ret[1] = (byte) ((x >> 8) & 0xff);
		return ret;
	}

	public static byte[] intToBytes(int x) {
		return new byte[] { (byte) (x >>> 24), (byte) (x >>> 16), (byte) (x >>> 8), (byte) x };
	}

	/**
	 * byte 数组转成 int 型
	 * 
	 * @param rno
	 * @return
	 */
	public static int bytesToInt(byte... rno) {
		int i = (rno[0] << 24) & 0xff000000 | (rno[1] << 16) & 0x00ff0000 | (rno[2] << 8) & 0x0000ff00
				| (rno[3] << 0) & 0x000000ff;
		return i;
	}

	/***
	 * 字符串转换成 byte数组
	 * 
	 * @param x
	 * @param fillSpace
	 *            是否在字符串最后补'\0';
	 * @return
	 */
	public static byte[] stringToBytes(String x, boolean fillSpace) {
		if(x == null){
			return new byte[]{'\0'};
		}
		byte[] ret = x.getBytes(UTF_8);
		if (fillSpace) {
			byte[] out = new byte[ret.length + 1];
			for (int i = 0; i < ret.length; i++) {
				out[i] = ret[i];
			}
			out[out.length - 1] = '\0';
			return out;
		}
		return ret;
	}

	public static int getStringLength(String x) {
		if(x == null || x.isEmpty()){
			return 1;
		}
		return x.getBytes(UTF_8).length+1;
	}

}
