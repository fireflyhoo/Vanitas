package cn.yayatao.vanitas.net;


/**
 * 网络连接器，负责网络连接。
 * @author fireflyhoo
 *
 */
public interface SocketAcceptor {
	
	public void start();
	
	public void shutdown();
	
	public int getPort();
	
	public String getName();
}
