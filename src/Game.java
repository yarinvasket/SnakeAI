import java.util.List;

public interface Game extends Cloneable {
	public void setInput(List<Float> input);

	public List<Float> getOutput();

	public float getScore();

	public boolean isAlive();

	public void tick();
	
	public void draw();

	public int getInputAmount();

	public int getOutputAmount();

	public Object clone() throws CloneNotSupportedException;
}
