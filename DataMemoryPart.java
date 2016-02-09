public class DataMemoryPart {
	byte ib1,ib2,ib3,ib4;
	DataMemoryPart(){
		ib1 = 0x0;
		ib2 = 0x0;
		ib3 = 0x0;
		ib4 = 0x0;
	}
	
	DataMemoryPart(byte nib1,byte nib2,byte nib3,byte nib4){
		ib1 = nib1;
		ib2 = nib2;
		ib3 = nib3;
		ib4 = nib4;
	}

	public byte getIB1() {
		return ib1;
	}

	public void setIB1(byte ib1) {
		this.ib1 = ib1;
	}

	public byte getIB2() {
		return ib2;
	}

	public void setIB2(byte ib2) {
		this.ib2 = ib2;
	}

	public byte getIB3() {
		return ib3;
	}

	public void setIB3(byte ib3) {
		this.ib3 = ib3;
	}

	public byte getIB4() {
		return ib4;
	}

	public void setIB4(byte ib4) {
		this.ib4 = ib4;
	}
	
	public byte[] getByteArray(){
		byte[] output = {ib1,ib2,ib3,ib4};
		return output;
	}
	
	public String ToString(){
		String output = "";
		output = Integer.toHexString(ib1) + Integer.toHexString(ib2) + Integer.toHexString(ib3) + Integer.toHexString(ib4);
		return output;
	}
}
