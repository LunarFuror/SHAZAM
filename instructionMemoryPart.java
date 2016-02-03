public class instructionMemoryPart {
	byte b1,b2,b3,b4;
	instructionMemoryPart(){
		b1 = 0x0;
		b2 = 0x0;
		b3 = 0x0;
		b4 = 0x0;
	}
	
	instructionMemoryPart(byte nb1,byte nb2,byte nb3,byte nb4){
		b1 = nb1;
		b2 = nb2;
		b3 = nb3;
		b4 = nb4;
	}

	public byte getB1() {
		return b1;
	}

	public void setB1(byte b1) {
		this.b1 = b1;
	}

	public byte getB2() {
		return b2;
	}

	public void setB2(byte b2) {
		this.b2 = b2;
	}

	public byte getB3() {
		return b3;
	}

	public void setB3(byte b3) {
		this.b3 = b3;
	}

	public byte getB4() {
		return b4;
	}

	public void setB4(byte b4) {
		this.b4 = b4;
	}
	
	public byte[] getByteArray(){
		byte[] output = {b1,b2,b3,b4};
		return output;
	}
}
