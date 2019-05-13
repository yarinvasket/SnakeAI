import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileNotFoundException;

public class ObjectIO {
	// copied from:
	// https://examples.javacodegeeks.com/core-java/io/fileoutputstream/how-to-write-an-object-to-file-in-java/
	public static void writeObjToFile(Object obj, String filePath) throws FileNotFoundException {
		try {
			FileOutputStream fileOut = new FileOutputStream(filePath);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(obj);
			objectOut.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// copied from:
	// https://examples.javacodegeeks.com/core-java/io/file/how-to-read-an-object-from-file-in-java/
	public static Object readObjFromFile(String filePath) throws FileNotFoundException {
		try {
			FileInputStream fileIn = new FileInputStream(filePath);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);

			Object obj = objectIn.readObject();

			objectIn.close();
			return obj;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
