import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SnakeGame implements Game, Serializable {
	private Block[][] board;
	private Map<List<Integer>, Direction> directions;
	private List<Float> input;
	private List<String> frames;
	private Direction latestDirection;
	private int xHead, yHead, xTail, yTail, xFood, yFood;
	private int score;
	private int tailLast;
	private int hunger;
	private boolean isAlive;
	private static final long serialVersionUID = 1L;

	public SnakeGame() {
		board = new Block[13][];
		for (int i = 0; i < 13; i++)
			board[i] = new Block[13];
		for (int i = 0; i < 13; i++)
			for (int j = 0; j < 13; j++)
				if (i == 0 || j == 0 || i == 12 || j == 12)
					board[i][j] = Block.WALL;
				else
					board[i][j] = Block.NA;

		xHead = 4;
		yHead = 5;
		xTail = xHead - 2;
		yTail = yHead;
		generateFood();
		directions = new HashMap<List<Integer>, Direction>();
		frames = new ArrayList<String>();
		for (int x = 2; x <= 4; x++) {
			board[yHead][x] = Block.SNAKE;
			if (x < 4) {
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(x);
				directions.put(key, Direction.RIGHT);
			}
		}
		latestDirection = Direction.RIGHT;
		score = 3;
		tailLast = 2;
		hunger = 20;
		isAlive = true;
	}

	@Override
	public void setInput(List<Float> input) {
		this.input = new ArrayList<Float>();
		for (float e : input)
			this.input.add(e);
	}

	@Override
	public List<Float> getOutput() {
		List<Float> output = new ArrayList<Float>();

		int yDifference;
		int xDifference;

		if (latestDirection == Direction.UP) {
			yDifference = yHead - yFood;
			xDifference = xHead - xFood;
		} else if (latestDirection == Direction.DOWN) {
			yDifference = yFood - yHead;
			xDifference = xFood - xHead;
		} else if (latestDirection == Direction.LEFT) {
			yDifference = xHead - xFood;
			xDifference = yHead - yFood;
		} else {
			yDifference = xFood - xHead;
			xDifference = yFood - yHead;
		}

		output.add((float) Math.atan2(yDifference, xDifference));
		int upDistance;
		int rightDistance;
		int leftDistance;
		int downDistance;
		if (latestDirection == Direction.DOWN) {
			upDistance = getDownDistance();
			rightDistance = getLeftDistance();
			leftDistance = getRightDistance();
			downDistance = getUpDistance();
		} else if (latestDirection == Direction.LEFT) {
			upDistance = getLeftDistance();
			rightDistance = getUpDistance();
			leftDistance = getDownDistance();
			downDistance = getRightDistance();
		} else if (latestDirection == Direction.RIGHT) {
			upDistance = getRightDistance();
			rightDistance = getDownDistance();
			leftDistance = getUpDistance();
			downDistance = getLeftDistance();
		} else {
			upDistance = getUpDistance();
			rightDistance = getRightDistance();
			leftDistance = getLeftDistance();
			downDistance = getDownDistance();
		}
		output.add(1 / (float) upDistance);
		output.add(1 / (float) rightDistance);
		output.add(1 / (float) leftDistance);
		output.add(1 / (float) downDistance);
		return output;
	}

	public int getUpDistance() {
		int res = 1;
		for (; true; res++) {
			int y = yHead - res;
			if (board[y][xHead] == Block.SNAKE || board[y][xHead] == Block.WALL)
				break;
		}
		return res;
	}

	public int getDownDistance() {
		int res = 1;
		for (; true; res++) {
			int y = yHead + res;
			if (board[y][xHead] == Block.SNAKE || board[y][xHead] == Block.WALL)
				break;
		}
		return res;
	}

	public int getRightDistance() {
		int res = 1;
		for (; true; res++) {
			int x = xHead + res;
			if (board[yHead][x] == Block.SNAKE || board[yHead][x] == Block.WALL)
				break;
		}
		return res;
	}

	public int getLeftDistance() {
		int res = 1;
		for (; true; res++) {
			int x = xHead - res;
			if (board[yHead][x] == Block.SNAKE || board[yHead][x] == Block.WALL)
				break;
		}
		return res;
	}

	@Override
	public float getScore() {
		return score;
	}

	@Override
	public boolean isAlive() {
		return isAlive;
	}

	@Override
	public void tick() {
		if (tailLast <= 0) {
			board[yTail][xTail] = Block.NA;
			List<Integer> key = new ArrayList<Integer>();
			key.add(yTail);
			key.add(xTail);
			Direction tailDirection = directions.get(key);
			if (tailDirection == Direction.DOWN)
				yTail++;
			else if (tailDirection == Direction.LEFT)
				xTail--;
			else if (tailDirection == Direction.RIGHT)
				xTail++;
			else
				yTail--;
			directions.put(key, null);
		} else {
			tailLast--;
			score++;
		}

		int maxIdx = 0;
		for (int i = 1; i < input.size(); i++)
			if (input.get(i) > input.get(maxIdx))
				maxIdx = i;
		switch (maxIdx) {
		case 1:
			if (latestDirection == Direction.DOWN) {
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.RIGHT);
				xHead++;
				latestDirection = Direction.RIGHT;
			} else if (latestDirection == Direction.LEFT) {
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.DOWN);
				yHead++;
				latestDirection = Direction.DOWN;
			} else if (latestDirection == Direction.RIGHT) {
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.UP);
				yHead--;
				latestDirection = Direction.UP;
			} else {
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.LEFT);
				xHead--;
				latestDirection = Direction.LEFT;
			}
			break;
		case 2:
			if (latestDirection == Direction.DOWN) {
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.LEFT);
				xHead--;
				latestDirection = Direction.LEFT;
			} else if (latestDirection == Direction.LEFT) {
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.UP);
				yHead--;
				latestDirection = Direction.UP;
			} else if (latestDirection == Direction.RIGHT) {
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.DOWN);
				yHead++;
				latestDirection = Direction.DOWN;
			} else {
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.RIGHT);
				xHead++;
				latestDirection = Direction.RIGHT;
			}
			break;
		default:
			if (latestDirection == Direction.DOWN) {
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.DOWN);
				yHead++;
			} else if (latestDirection == Direction.LEFT) {
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.LEFT);
				xHead--;
			} else if (latestDirection == Direction.RIGHT) {
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.RIGHT);
				xHead++;
			} else {
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.UP);
				yHead--;
			}
			break;
		}

		if (board[yHead][xHead] == Block.WALL || board[yHead][xHead] == Block.SNAKE || hunger <= 0) {
			isAlive = false;
			addFrame();
			return;
		} else if (board[yHead][xHead] == Block.NA) {
			board[yHead][xHead] = Block.SNAKE;
			hunger--;
		} else {
			tailLast += 3;
			board[yHead][xHead] = Block.SNAKE;
			hunger = 20;
			generateFood();
		}
		addFrame();
	}

	public void generateFood() {
		xFood = (int) (Math.random() * 11) + 1;
		yFood = (int) (Math.random() * 11) + 1;
		if (board[yFood][xFood] == Block.NA)
			board[yFood][xFood] = Block.FOOD;
		else
			generateFood();
	}

	@Override
	public int getInputAmount() {
		return 5;
	}

	@Override
	public int getOutputAmount() {
		return 3;
	}

	public void addFrame() {
		String res = "";
		for (Block[] y : board) {
			for (Block x : y)
				if (x == Block.NA)
					res += " ";
				else if (x == Block.WALL)
					res += "█";
				else if (x == Block.SNAKE)
					res += "*";
				else
					res += "&";
			res += "\n";
		}
		frames.add(res);
	}
	
	@Override
	public void draw() {
		for (String s : frames)
			System.out.println(s);
	}

	public Object clone() throws CloneNotSupportedException {
		// since clone is broken I'm returning a fresh object from the constructor
		return new SnakeGame();
	}

}
