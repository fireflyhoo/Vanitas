package cn.yayatao.vanitas.postgresql.datagram;
/**
 * 数据包
 * @author Huyahui
 *
 */
public interface Datagram {

	/**
	 * 数据包序列化byte数据
	 * @return
	 */
	public byte[] toByteArrays();
	
	/**
	 * 数据包真实字节数
	 * @return
	 */
	public int size();
	
	
	/**
	 * 修正数据包长度 用于构建出来的数据包设置长度
	 */
	public void reviseLength();
}
