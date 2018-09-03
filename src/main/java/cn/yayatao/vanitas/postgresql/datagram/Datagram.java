package cn.yayatao.vanitas.postgresql.datagram;
/**
 * 数据包
 * @author Huyahui
 *
 */
public interface Datagram {

	public byte[] toByteArrays();
	
	public int size();
}
