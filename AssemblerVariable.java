
public class AssemblerVariable {
	String location;
	String data;
	String label;
	
	public AssemblerVariable(){
		location = "";
		data = "";
		label = "";
	}
	
	public AssemblerVariable(String loc, String dat, String lab){
		setLocation(loc);
		setData(dat);
		setLabel(lab);
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		location = location.trim();
		while(location.length()<3){
			location = "0" + location;
		}
		this.location = location;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "AssemblerVariable [location=" + location + ", data=" + data + ", label=" + label + "]";
	}
}
