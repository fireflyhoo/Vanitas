package org.kiigo.vanitas.net.nio;

import java.nio.ByteBuffer;

public interface IoHander {
	void hander(ByteBuffer bf ,int length);
}
