package cn.yayatao.vanitas;

import java.net.Socket;
import java.util.Scanner;

public class IOTest {
	public static void main(String[] args) throws Exception {
		@SuppressWarnings("resource")
		Socket socket = new Socket("127.0.0.1", 54320);
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		while(true){
			String cmd =  scanner.nextLine();
			if("exit".equalsIgnoreCase(cmd)){
				return;
			}
			long start = System.currentTimeMillis();
			
			socket.getOutputStream().write(cmd.getBytes());
			//socket.getOutputStream().flush();
			byte[] inBytes = new byte[1024];
			socket.getInputStream().read(inBytes);
			System.out.println(new String(inBytes)); 
			System.out.println(System.currentTimeMillis() - start);
			
		}		
	}
}
