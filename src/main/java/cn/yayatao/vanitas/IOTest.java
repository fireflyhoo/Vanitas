package cn.yayatao.vanitas;

import java.net.Socket;
import java.util.Scanner;

public class IOTest {
	public static void main(String[] args) throws Exception {
		@SuppressWarnings("resource")
		Socket socket = new Socket("127.0.0.1", 8800);
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		while(true){
			String cmd =  scanner.nextLine();
			if("exit".equalsIgnoreCase(cmd)){
				return;
			}
			socket.getOutputStream().write(cmd.getBytes());
			socket.getOutputStream().flush();
		}		
	}
}
