package org.kiigo.vanitas.net.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.kiigo.vanitas.net.SocketAcceptor;
import org.kiigo.vanitas.net.SocketIOReactor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NIOAcceptor extends Thread implements SocketAcceptor{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NIOAcceptor.class); 
	
	private final ServerSocketChannel serverChannel;
	private final int port;
	private final Selector selector;
	private final AtomicBoolean closed = new AtomicBoolean(false);
	private final NIOReactor reactor;
	
	public NIOAcceptor(String name,int port,SocketIOReactor reactor) throws Exception {
		super(name);
		this.selector = Selector.open();
		this.port = port;
		this.serverChannel = ServerSocketChannel.open();
		this.serverChannel.configureBlocking(false);
		this.serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		this.serverChannel.bind(new InetSocketAddress("0.0.0.0", this.port));
		this.reactor = (NIOReactor) reactor;
	}

	public void shutdown() {
		closed.set(true);
		this.selector.wakeup();
	}

	@Override
	public void run() {
		while(!closed.get()){//服务器未关闭
			try {
				if(this.selector.select() == 0){
					continue;
				}
				Set<SelectionKey> keys = this.selector.selectedKeys();
				Iterator<SelectionKey> iterator = keys.iterator();
				while(iterator.hasNext()){
					SelectionKey key = iterator.next();
					if(key.isValid() && key.isAcceptable()){
						doAcceptor(key);
						iterator.remove();
					}
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
			
		}
		try {
			this.serverChannel.close();
			this.selector.close();
		} catch (Exception e) {
			LOGGER.error("关闭连接器出错",e);
		}
	}

	
	/**
	 * 进行连接
	 * @param key
	 * @throws IOException 
	 */
	private void doAcceptor(SelectionKey key) throws IOException {
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
		SocketChannel channel =  serverSocketChannel.accept();
		channel.configureBlocking(false);
		FrontConnection con = new FrontConnection(channel);
		this.reactor.register(con);
	}

	public int getPort() {
		return this.port;
	}

	

}
