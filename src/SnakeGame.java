import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

public class SnakeGame implements Game, Serializable {
	private Block[][] board;
	private List<Float> input;
	private int xHead, yHead, xTail, yTail, xFood, yFood;
	private boolean isAlive;
	private static final long serialVersionUID = 1L;

	public SnakeGame(int boardSize) {
		board = new Block[boardSize][];
		input = new ArrayList<Float>();
		for (int i = 0; i < board.length; i++) {
			board[i] = new Block[boardSize];
			for (int j = 0; j < board[i].length; j++)
				if (i == 0 || j == 0 || i == boardSize - 1 || j == boardSize - 1)
					board[i][j] = Block.WALL;
				else
					board[i][j] = Block.NA;
		}
		xHead = (int) (Math.random() * 15) + 1;
		yHead = (int) (Math.random() * 15) + 1;
		board[xHead][yHead] = Block.SNAKE;
		xTail = xHead;
		yTail = yHead;
		generateFood();
		isAlive = true;
	}

	public void generateFood() {
		xFood = (int) (Math.random() * 15) + 1;
		yFood = (int) (Math.random() * 15) + 1;
		if (board[xFood][yFood] == Block.NA)
			board[xFood][yFood] = Block.FOOD;
		else
			generateFood();
	}

	@Override
	public void setInput(List<Float> input) {
		this.input = new ArrayList<Float>();
		for (float e : input)
			this.input.add(e);
	}

	@Override
	public List<Float> getOutput() {
		return null;
	}

	@Override
	public float getScore() {
		return 0;
	}

	@Override
	public boolean isAlive() {
		return isAlive;
	}

	@Override
	public void tick() {

	}

	@Override
	public int getInputAmount() {
		return 0;
	}

	@Override
	public int getOutputAmount() {
		return 3;
	}

	public Object clone() throws CloneNotSupportedException {
		return (SnakeGame) super.clone();
	}

}
