import java.util.Vector;

public class AssemblerProcedure {
	String location;
	String label;
	int level;
	Vector<AssemblerVariable> variableTable;
	Vector<AssemblerLabel> labelTable;
	Vector<AssemblerInstruction> instructionTable;
	
	public AssemblerProcedure(){
		location = "";
		label="";
		level=0;
		variableTable = new Vector<AssemblerVariable>();
		labelTable = new Vector<AssemblerLabel>();
		instructionTable = new Vector<AssemblerInstruction>();
	}
	
	public AssemblerProcedure(String loc, String lab, int lev){
		setLocation(loc);
		setLabel(lab);
		setLevel(lev);
		variableTable = new Vector<AssemblerVariable>();
		labelTable = new Vector<AssemblerLabel>();
		instructionTable = new Vector<AssemblerInstruction>();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public String getLevelString(){
		String output = level + "";
		while(output.length()<4){
			output = "0" + output;
		}
		return output;
	}

	public void addVariable(String label, String data){
		variableTable.add(new AssemblerVariable( (3+variableTable.size()) + "" , data, label));
	}
	
	public void addLabel(String label, String location){
		labelTable.add(new AssemblerLabel(label, location));
	}
	
	public void addInstruction(String location, String level, String label, String operand){
		instructionTable.add(new AssemblerInstruction(location, level, label, operand));
	}

	public String getVariableCount(){
		String output = Integer.toHexString(variableTable.size());
		while(output.length()<3){
			output = "0" + output;
		}
		return output;
	}
	
	public String getInstructionCount(){
		String output = Integer.toHexString(instructionTable.size());
		while(output.length()<3){
			output = "0" + output;
		}
		return output;
	}
	
	@Override
	public String toString() {
		return "AssemblerProcedure [location=" + location + ", label=" + label + ", variableTable=" + variableTable
				+ ", labelTable=" + labelTable + ", instructionTable=" + instructionTable + "]";
	}	
}
