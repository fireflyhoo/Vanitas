package cn.yayatao.vanitas;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class VanitasServerTest {
	public static void main(String[] args) throws Exception {
		new Thread(new Runnable(){

			public void run() {
				for(int i= 0;i < 5000; i++){
					try {
						Socket socket = new Socket("127.0.0.1", 8800);
						socket.getOutputStream().write("heeoo".getBytes());
						System.out.println(Thread.currentThread().getName()+i);
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
			
		}).start();
		new Thread(new Runnable(){

			public void run() {
				for(int i= 0;i < 5000; i++){
					try {
						Socket socket = new Socket("127.0.0.1", 8800);
						System.out.println(Thread.currentThread().getName()+i);
						socket.getOutputStream().write("heeoo".getBytes());
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
			
		}).start();
		new Thread(new Runnable(){

			public void run() {
				for(int i= 0;i < 5000; i++){
					try {
						Socket socket = new Socket("127.0.0.1", 8800);
						System.out.println(Thread.currentThread().getName()+i);
						socket.getOutputStream().write("heeoo".getBytes());
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
			
		}).start();
	
		System.in.read();
		
	}
}
