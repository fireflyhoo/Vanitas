package cn.yayatao.vanitas;

import java.io.IOException;
import java.nio.channels.Selector;

public class SelectTest {
	public static void main(String[] args) {
		for(int i=0;i<100000;i++){
			try {
				Selector selector =  Selector.open();
				System.out.println("open"+i+"      "+selector);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
