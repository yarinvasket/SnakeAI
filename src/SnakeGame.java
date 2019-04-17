import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SnakeGame implements Game, Serializable {
	private Block[][] board;
	private Map<List<Integer>, Direction> directions;
	private List<Float> input;
	private static final long serialVersionUID = 1L;

	public SnakeGame(int boardSize) {
		board = new Block[boardSize][];
		for (int i = 0; i < boardSize; i++)
			board[i] = new Block[boardSize];
		for (int i = 0; i < boardSize; i++)
			for (int j = 0; j < boardSize; j++)
				if (i == 0 || j == 0 || i == boardSize - 1 || j == boardSize - 1)
					board[i][j] = Block.WALL;
				else
					board[i][j] = Block.NA;

		directions = new HashMap<List<Integer>, Direction>();
	}

	@Override
	public void setInput(List<Float> input) {
		input = new ArrayList<Float>();
		for(float e : input)
			this.input.add(e);
	}

	@Override
	public List<Float> getOutput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isAlive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getInputAmount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getOutputAmount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object clone() throws CloneNotSupportedException {
		return (SnakeGame) super.clone();
	}

}
