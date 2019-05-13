
public class Main {

	public static void main(String[] args) {
		String filePath = "Population";
		Population population;
		try {
			population = (Population) ObjectIO.readObjFromFile(filePath);
		} catch (Exception e) {
			Game snakeGame = new SnakeGame();
			population = new Population(1000, snakeGame);
		}
		while (true)
			population.killMutate();
	}

}
