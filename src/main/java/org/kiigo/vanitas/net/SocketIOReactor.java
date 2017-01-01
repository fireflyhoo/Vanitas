package org.kiigo.vanitas.net;

import org.kiigo.vanitas.net.nio.FrontConnection;

/**
 * 网络IO事件相应
 * @author fireflyhoo
 *
 */
public interface SocketIOReactor {
	
	public void register(FrontConnection channel);
	
	public void start();

}
