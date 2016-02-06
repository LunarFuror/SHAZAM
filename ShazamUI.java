import java.util.Scanner;
import java.io.File;

public class ShazamUI {
	private static DataMemoryPart[][] dataMemory = new DataMemoryPart[0x400][0xF];
	private static InstructionMemoryPart[][] instructionMemory = new InstructionMemoryPart[0x800][0xF];
	
	public ShazamUI(){
		
	}
	
	public void run(){
		String choice = "";
		Scanner userIn = new Scanner(System.in);
		
		//Basically run all the things
		while(choice.compareTo("exit") != 0){
			printMenu();
			choice = userIn.next().toLowerCase();
			switch(choice){
				case "clear": 
					clear();
					break;
				case "dump": 
					dump();
					break;
				case "load": 
					load();
					break;
				case "exit": 
					break;
				default: break;
			}
		}
		userIn.close();
	}
	
	//Prints the menu
	public void printMenu(){
		System.out.println("Type your choice and hit enter\n" +
				"Clear\n" +
				"Dump\n" +
				"Load\n" +
				"Exit");
	}
	
	//Clear all the memory!
	public void clear(){
		//clear instructions
		for(int i = 0x0; i < 0x800; i++){
			for(int j = 0x0; j < 0xF; j++){
				instructionMemory[i][j] = new InstructionMemoryPart((byte)0x0,(byte)0x0,(byte)0x0,(byte)0x0);
			}
		}
		
		//clear data
		for(int i = 0x0; i < 0x400; i++){
			for(int j = 0x0; j < 0xF; j++){
				dataMemory[i][j] = new DataMemoryPart((byte)0x6,(byte)0x3,(byte)0x0,(byte)0x0,(byte)0x0);
			}
		}
		
		System.out.println("Memory Cleared");
	}
	
	public void dump(){
		//write memory to output.txt
	}
	
	public void load(){
		File input = null;
		Scanner fileIn = null;
		try{
			input = new File("Load.in.txt");
			fileIn = new Scanner(input);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		//read instructions
		while(fileIn.hasNextLine()){
			String[] insLine = fileIn.nextLine().toLowerCase().split(" ");
			switch (insLine[0]){
				case "i": 
					break;
				case "d": 
					break;
			}
		}
		fileIn.close();
	}
}
