package cn.yayatao.vanitas;

import java.net.Socket;
import java.util.Scanner;

public class EchoIOTest {
	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("127.0.0.1", 54320);
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("读命令");
			String cmd = scanner.nextLine();
			System.out.println("读到命令为" + cmd);
			if ("exit".equalsIgnoreCase(cmd)) {
				return;
			}
			if (cmd.getBytes().length == 0) {
				System.out.println("输入命令为空字符忽略");
				continue;
			}
			long start = System.currentTimeMillis();
			socket.getOutputStream().write(cmd.getBytes());
			System.out.println("写入完成!");

			byte[] inBytes = new byte[1024];
			System.out.println("读取返回结果....");
			socket.getInputStream().read(inBytes);
			System.out.println("返回结果:" + new String(inBytes));
			System.out.println("获取返回耗时:" + (System.currentTimeMillis() - start));
		}
	}
}
