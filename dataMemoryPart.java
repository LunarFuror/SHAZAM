package test;

public class dataMemoryPart {
	byte db1,db2,db3,db4,db5;
	dataMemoryPart(){
		db1 = 0x0;
		db2 = 0x0;
		db3 = 0x0;
		db4 = 0x0;
		db5 = 0x0;
	}
	
	dataMemoryPart(byte nib1,byte nib2,byte nib3,byte nib4,byte nib5){
		db1 = nib1;
		db2 = nib2;
		db3 = nib3;
		db4 = nib4;
		db5 = nib5;
	}

	public byte getDB1() {
		return db1;
	}

	public void setDB1(byte db1) {
		this.db1 = db1;
	}

	public byte getDB2() {
		return db2;
	}

	public void setDB2(byte db2) {
		this.db2 = db2;
	}

	public byte getDB3() {
		return db3;
	}

	public void setDB3(byte db3) {
		this.db3 = db3;
	}

	public byte getDB4() {
		return db4;
	}

	public void setDB4(byte db4) {
		this.db4 = db4;
	}

	public byte getDB5() {
		return db5;
	}

	public void setDB5(byte db5) {
		this.db5 = db5;
	}
	
	public byte[] getByteArray(){
		byte[] output = {db1,db2,db3,db4,db5};
		return output;
	}
}
