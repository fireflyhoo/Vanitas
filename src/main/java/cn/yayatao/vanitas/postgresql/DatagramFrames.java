package cn.yayatao.vanitas.postgresql;

import java.util.ArrayList;
import java.util.List;

/***
 * 拆分数据包
 * @author Huyahui
 *
 */
public class DatagramFrames {
	/**
	 * 数据包长度
	 */
	public int length;
	
	/***
	 * 数据帧
	 */
	List<byte[]> frames = new ArrayList<>();
	
	/**
	 * 边角料
	 */
	byte[] offcut;

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public List<byte[]> getFrames() {
		return frames;
	}

	public void addFrame(byte[] frame){
		if(this.frames == null){
			this.frames = new ArrayList<>();
		} 
		this.frames.add(frame);
	}
	
	public void setFrames(List<byte[]> frames) {
		this.frames = frames;
	}

	public byte[] getOffcut() {
		return offcut;
	}

	public void setOffcut(byte[] offcut) {
		this.offcut = offcut;
	}
}
