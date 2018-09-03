package cn.yayatao.vanitas.postgresql.datagram;

public interface IBuilder {
	
	public Datagram build(byte[] data);
}
