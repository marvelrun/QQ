package io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ObjectOutputStream extends DataOutputStream {
	B2o b2o = new B2o();
	public ObjectOutputStream(OutputStream out) {
		super(out);
	}

	public void writeObject(Object object) {
		try {
			byte[] b =b2o.toByteArray(object);
			this.writeInt(b.length);
			this.write(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
