import java.io.File;

public class Main {

	public static void main(String[] args) {
		String filePath = "Population";
		Population population;
		File f = new File(filePath);
		if (f.isFile()) {
			population = (Population) ObjectIO.readObjFromFile(filePath);
		} else {
			Game snakeGame = new SnakeGame();
			population = new Population(1000, snakeGame, 4, 75);
		}
		while (true)
			population.killMutate();
	}

}
