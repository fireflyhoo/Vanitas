package org.kiigo.vanitas;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.kiigo.vanitas.net.SocketAcceptor;
import org.kiigo.vanitas.net.SocketIOReactor;
import org.kiigo.vanitas.net.nio.NIOAcceptor;
import org.kiigo.vanitas.net.nio.NIOReactor;

/**
 * 服务入口
 * 
 * @author fireflyhoo
 *
 */
public class VanitasServer {

	public static ExecutorService getExecutor() {
		return executor;
	}

	private static ExecutorService executor = Executors.newCachedThreadPool(new ThreadFactory() {
		private AtomicInteger count = new AtomicInteger(0);

		@Override
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r, "thread-bus-" + count.incrementAndGet());
			return thread;
		}
	});

	public static class StartServer {

		public static void main(String[] args) throws Exception {
			SocketIOReactor ioReactor = new NIOReactor("NIO-Reactor-01");
			SocketAcceptor acceptor = new NIOAcceptor("PostgreSQL-VanitasServer", 54320, ioReactor);
			acceptor.start();
			ioReactor.start();
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				@Override
				public void run() {
					System.out.println("退出");
				}
			}));
		}
	}

}
