package br.lopes.biometrySom.logic;

public class Map {

	private int winnerNeuronIndex;
	private String letter;

	public Map(int winnerNeuron, String letter) {
		this.winnerNeuronIndex = winnerNeuron;
		this.letter = letter;
	}

	public int getWinnerNeuronIndex() {
		return winnerNeuronIndex;
	}

	public String getLetter() {
		return letter;
	}

}
