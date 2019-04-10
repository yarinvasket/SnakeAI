import java.util.List;

public class SnakeGame implements Game {

	public SnakeGame() {
		
	}
	
	@Override
	public void setInput(List<Float> input) {
		
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
		return false;
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
		return 0;
	}
	
	public Object clone() throws CloneNotSupportedException {
		return (SnakeGame) super.clone();
	}

}
