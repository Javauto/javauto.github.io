import java.io.*;

public class compile {
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Error: please include one or more file names to compile! For help visit http://javAuto.org/"); 
		}
		for (String argFileName : args) {
			if (!argFileName.toLowerCase().endsWith(".javauto")) {
				System.out.println("Error: only accepts \".javauto\" files!");
				System.exit(1);
			}
			if (argFileName.contains("\\")) {
				System.out.println("Error: please only use forward slashes ( / ) in filepath");
				System.exit(1);
			}
		}
		for (String arg : args) {
			compile(arg);
		}
	}
	
	private static void compile ( String inPath, String outPath ) {		
		File f = new File(inPath);

		if(!f.exists()) {
			System.out.println("Error: couldn't find file \"" + inPath + "\"\nAre you sure it exists?");
			System.exit(1);
		}
		
		try {
			String cName;
			//get class name
			String fileName = outPath.replace(".java", "");
			if (outPath.contains("/")) {
				String[] pathParts = fileName.split("[/]+");
				int index = pathParts.length - 1;
				cName = pathParts[index];
			} else {
				cName = fileName;
			}
			
			File directory = new File(fileName + "/");
			if (directory.exists()) {
				System.out.println("Error: the directory " + fileName + "/" + " already exists. Please place " + inPath + " in an empty directory and try again.");
				System.exit(1);
			}
			//System.out.println
			boolean success = (new File(fileName + "/")).mkdir();
			if (!success) {
				System.out.println("Directory creation failed! Check permissions and try again.");
				System.exit(1);
			}
			
			BufferedReader br = new BufferedReader(new FileReader(inPath));
		
			//create string builders to sort and hold data
			StringBuilder uI = new StringBuilder();
			StringBuilder uF = new StringBuilder();
			StringBuilder uC = new StringBuilder();
			
			//start reading through the file
			String line = br.readLine();
			while (line != null) {
				if (line.startsWith("import ")) {
					//the line is an import
					uI.append(line);
					uI.append('\n');
					line = br.readLine();
				} else {
					if (line.startsWith("func ")) {
						//the start of a function
						line = line.replace("func ", "private static ");
						do {
							uF.append(line);
							uF.append('\n');
							line = br.readLine();
						} while (!line.endsWith("endFunc"));
						//the end of a function
						line = line.replace(" endFunc", "");
						uF.append(line);
						line = br.readLine();
					} else {
						//the line is normal code
						uC.append(line);
						if (line != null) {
							uC.append('\n');
						}
						line = br.readLine();
					}
				}
			}
			
			//construct into strings
			String uImports = uI.toString();
			String uFunctions = uF.toString();
			String uCode = uC.toString();
		
			//close file
			br.close();
			
			//read the template
			String tempPath = "template.mp3";
			InputStream tempStream = compile.class.getResourceAsStream(tempPath);
			BufferedReader tm = new BufferedReader(new InputStreamReader(tempStream, "UTF-8"));
		
			//create string builders to sort and hold data
			StringBuilder uT = new StringBuilder();
			
			//start reading through the file
			line = tm.readLine();
			while (line != null) {
				uT.append(line);
				if (line != null) {
					uT.append('\n');
				}
				line = tm.readLine();
			}			
			//construct into strings
			String uTemplate = uT.toString();
			
			String finalCode = uTemplate.replace("{<userImports>}", uImports);
			finalCode = finalCode.replace("{<userFunctions>}", uFunctions);
			finalCode = finalCode.replace("{<userMainCode>}", uCode);
			finalCode = finalCode.replace("{<userClassName>}", cName);
			
			outPath = fileName + "/" + cName + ".java";
			
			File file = new File(outPath);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(finalCode);
			bw.close();
			
			//finally, create a manifest
			String manifest = "Main-Class: " + cName + "\n\n";
			String manifestPath = fileName + "/manifest.txt";
			File manifestFile = new File(manifestPath);
			FileWriter mffw = new FileWriter(manifestFile.getAbsoluteFile());
			BufferedWriter writer = new BufferedWriter(mffw);
			writer.write(manifest);
			writer.close();
			
			System.out.print("Operation complete.");
			
		} catch ( Exception e ) {
			System.out.println("Error: couldn't compile.");
			System.exit(1);
		}
	}

	private static void compile ( String inPath ) {
		char[] inP = inPath.toCharArray();
		String lastEight;
		int end = inP.length-1;
		int i = end - 7;
		StringBuilder en = new StringBuilder();
		while (i <= end) {
			en.append(inP[i]);
			i++;
		}
		lastEight = en.toString();
		String outPath = inPath.replace(lastEight, ".java");
		compile(inPath, outPath);
	}

}