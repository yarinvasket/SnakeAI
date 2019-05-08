import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.io.Serializable;

public class Population implements Serializable {
	private Map<Integer, NeuralNetwork> creatures;
	private Map<Integer, Batch> batches;
	private Game game;
	private static final long serialVersionUID = 1L;

	public Population(int popSize, Game game) {
		creatures = new HashMap<Integer, NeuralNetwork>();
		batches = new HashMap<Integer, Batch>();
		this.game = game;

		for (int i = 0; i < popSize; i++) {
			creatures.put(i, new NeuralNetwork(game.getInputAmount(), game.getOutputAmount()));
			batches.put(i, creatures.get(i).addConnectionMutation());
		}

		float[] scores = new float[creatures.size()];
		for (int i = 0; i < creatures.size(); i++)
			scores[i] = testCreature(i);
		sort(scores);
		for (int i = 0; i < creatures.size() / 2; i++) {
			int num = (int) (i * Math.random());
			creatures.put(i + creatures.size() / 2, (NeuralNetwork) creatures.get(num).clone());
			batches.put(i + creatures.size() / 2, (Batch) batches.get(num).clone());
		}

		for (int i = 0; i < creatures.size() / 2; i++)
			batches.get(i + creatures.size() / 2).addBatch(creatures.get(i + creatures.size() / 2).mutateConnections());
	}

	public void killMutate() {
		float[] scores = new float[creatures.size()];
		for (int i = 0; i < creatures.size(); i++)
			scores[i] = testCreature(i);
		sort(scores);

		for (int i = 0; i < creatures.size() / 2; i++)
			try {
				int num = (int) (i * Math.random());
				creatures.put(i + creatures.size() / 2, (NeuralNetwork) creatures.get(num).clone());
				batches.put(i + creatures.size() / 2, (Batch) batches.get(num).clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}

		for (int i = 0; i < creatures.size() / 2; i++) {
			int idx = i + creatures.size() / 2;
			float latestScore = batches.get(idx).getScore(batches.get(idx).getScores().size() - 1);
			float beforeLatestScore = batches.get(idx).getScore(batches.get(idx).getScores().size() - 2);

			for (List<Integer> key : batches.get(idx).getConnections().keySet()) {
				int size = batches.get(idx).getConnections().get(key).size();
				if (size <= 1)
					continue;
				float diff = batches.get(idx).getConnections().get(key).get(size - 1)
						- batches.get(idx).getConnections().get(key).get(size - 2);
				float value = diff * (latestScore - beforeLatestScore) / 2;
				creatures.get(idx).nudgeConnection(key, value);
				batches.get(idx).addConnection(key, creatures.get(idx).getConnections().get(key) + value);
			}

			float absDiff = Math.abs(latestScore) - Math.abs(beforeLatestScore);
			if (absDiff <= latestScore / 50) {
				batches.get(idx).addBatch(creatures.get(idx).addConnectionMutation());
				if (absDiff <= latestScore / 100) {
					batches.get(idx).addBatch(creatures.get(idx).mutateConnections());
					int layerNum = creatures.get(idx).addNeuronMutation();

					batches.get(idx).addBatch(creatures.get(idx)
							.addInConnectionMutation(creatures.get(idx).getNeurons().size() - 1, layerNum));
					batches.get(idx).addBatch(creatures.get(idx)
							.addOutConnectionMutation(creatures.get(idx).getNeurons().size() - 1, layerNum));

					if (absDiff <= latestScore / 200) {
						creatures.get(idx).addLayer();
						int layerNo = creatures.get(idx).getLayers().size() - 2;

						creatures.get(idx).addNeuron(layerNo);

						batches.get(idx).addBatch(creatures.get(idx)
								.addInConnectionMutation(creatures.get(idx).getNeurons().size() - 1, layerNo));
						batches.get(idx).addBatch(creatures.get(idx)
								.addOutConnectionMutation(creatures.get(idx).getNeurons().size() - 1, layerNo));
					}
				}
			}
		}
	}

	public float[] sort(float[] arr) {
		if (arr.length <= 1)
			return arr;

		float pivot = arr[arr.length - 1];
		int pivotIdx = arr.length - 1;

		for (int i = arr.length - 2; i >= 0; i--)
			if (arr[i] < pivot) {
				for (int j = i; j < pivotIdx; j++) {
					float temp = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = temp;

					try {
						NeuralNetwork tmp = (NeuralNetwork) creatures.get(j).clone();
						creatures.put(j, creatures.get(j + 1));
						creatures.put(j + 1, tmp);
						Batch tmpb = (Batch) batches.get(j).clone();
						batches.put(j, batches.get(j + 1));
						batches.put(j + 1, tmpb);
					} catch (CloneNotSupportedException e) {
						e.printStackTrace();
					}
				}
				pivotIdx--;
			}

		float[] leftSubArr = new float[pivotIdx];
		for (int i = 0; i < pivotIdx; i++)
			leftSubArr[i] = arr[i];
		leftSubArr = sort(leftSubArr);

		float[] rightSubArr = new float[arr.length - pivotIdx - 1];
		for (int i = 0; i < arr.length - pivotIdx - 1; i++)
			rightSubArr[i] = arr[i + pivotIdx + 1];
		rightSubArr = sort(rightSubArr);

		float[] res = new float[arr.length];
		for (int i = 0; i < pivotIdx; i++)
			res[i] = leftSubArr[i];
		res[pivotIdx] = arr[pivotIdx];
		for (int i = 0; i < arr.length - pivotIdx - 1; i++)
			res[i + pivotIdx + 1] = rightSubArr[i];

		return res;
	}

	public float testCreature(int creatureNo) {
		try {
			Game gameCopy = (Game) game.clone();
			while (gameCopy.isAlive()) {
				gameCopy.setInput(creatures.get(creatureNo).feedForward(gameCopy.getOutput()));
				gameCopy.tick();
			}
			float score = gameCopy.getScore();
			batches.get(creatureNo).addScore(score);
			return score;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return 0;
		}
	}
}
