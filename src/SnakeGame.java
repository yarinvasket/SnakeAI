import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;

public class SnakeGame implements Game, Serializable {
	private Block[][] board;
	private List<Float> input;
	private Map<List<Integer>, Direction> directions;
	private Direction latestDirection;
	private int xHead, yHead, xTail, yTail, xFood, yFood;
	private boolean isAlive;
	private int score;
	private int tailLast;
	private static final long serialVersionUID = 1L;

	public SnakeGame(int boardSize) {
		board = new Block[boardSize][];
		input = new ArrayList<Float>();
		directions = new HashMap<List<Integer>, Direction>();
		latestDirection = Direction.RIGHT;
		for (int i = 0; i < board.length; i++) {
			board[i] = new Block[boardSize];
			for (int j = 0; j < board[i].length; j++)
				if (i == 0 || j == 0 || i == boardSize - 1 || j == boardSize - 1)
					board[i][j] = Block.WALL;
				else
					board[i][j] = Block.NA;
		}
		xHead = 4;
		yHead = 5;
		for (int i = 4; i <= 7; i++) {
			board[i][yHead] = Block.SNAKE;
			List<Integer> key = new ArrayList<Integer>();
			directions.put(key, Direction.RIGHT);
		}
		xTail = 2;
		yTail = yHead;
		generateFood();
		isAlive = true;
		score = 0;
		tailLast = 1;
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
		return (float) score;
	}

	@Override
	public boolean isAlive() {
		return isAlive;
	}

	@Override
	public void tick() {
		int maxIdx = 0;
		for (int i = 0; i < input.size(); i++)
			if (input.get(i) > input.get(maxIdx))
				maxIdx = i;
		Direction value = latestDirection;
		switch (maxIdx) {
		case 1:
			if (latestDirection == Direction.DOWN)
				value = Direction.RIGHT;
			else if (latestDirection == Direction.LEFT)
				value = Direction.DOWN;
			else if (latestDirection == Direction.RIGHT)
				value = Direction.UP;
			else if (latestDirection == Direction.UP)
				value = Direction.LEFT;
			break;
		case 2:
			if (latestDirection == Direction.DOWN)
				value = Direction.LEFT;
			else if (latestDirection == Direction.LEFT)
				value = Direction.UP;
			else if (latestDirection == Direction.RIGHT)
				value = Direction.DOWN;
			else
				value = Direction.RIGHT;
			break;
		default:
			break;
		}
		List<Integer> key = new ArrayList<Integer>();
		key.add(xHead);
		key.add(yHead);
		directions.put(key, value);
		latestDirection = directions.get(key);
		if (latestDirection == Direction.DOWN)
			yHead--;
		else if (latestDirection == Direction.LEFT)
			xHead--;
		else if (latestDirection == Direction.RIGHT)
			xHead++;
		else
			yHead++;
	}

	@Override
	public int getInputAmount() {
		return 8;
	}

	@Override
	public int getOutputAmount() {
		return 3;
	}

	public Object clone() throws CloneNotSupportedException {
		return (SnakeGame) super.clone();
	}

	public void drawBoard() {
		// TODO
	}

}
