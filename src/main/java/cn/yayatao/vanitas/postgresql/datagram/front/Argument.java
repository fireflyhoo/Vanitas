package cn.yayatao.vanitas.postgresql.datagram.front;

public  class Argument {
	int length;
	byte[] data;

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}