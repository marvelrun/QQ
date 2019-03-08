package io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ObjectInputStream extends DataInputStream{
	B2o b2o = new B2o();
	public ObjectInputStream(InputStream in) {
		super(in);
	}

	public Object readObject() {
		Object o=null;
		try {
			int length=this.readInt();
			byte[] b = new byte[length];
			this.read(b);
			o=b2o.toObject(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return o;
	}

}
