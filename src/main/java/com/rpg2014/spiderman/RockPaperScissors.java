package com.rpg2014.spiderman;

import java.util.Random;

import com.rpg2014.spiderman.GroupMe.GroupMeResponse;

public class RockPaperScissors {

	private static final int HUMAN = 0;
	private static final int COMPUTER = 1;
	private static final int TIE = 2;

	private static final int ROCK = 0;
	private static final int PAPER = 1;
	private static final int SCISSORS = 2;
	private static String[] rps = { "Rock", "Paper", "Scissors" };
	private static final Random rand = new Random();

	public static GroupMeResponse play(String playerGuess) {
		int computerChoice = getComputerChoice();
		try {
			int playerChoice = getPlayerChoice(playerGuess);

			int winner = compare(playerChoice, computerChoice);
			return getMessage(winner, rps[computerChoice]);

		} catch (IllegalArgumentException e) {
			return new GroupMeResponse(e.getMessage());
		}
	}

	private static GroupMeResponse getMessage(int winner, String computerGuess) {
		GroupMeResponse response;
		if (winner == HUMAN) {
			response = new GroupMeResponse("I chose " + computerGuess + ". You won.");
		} else if (winner == COMPUTER) {
			response = new GroupMeResponse("I chose " + computerGuess + ". I won");
		} else {
			response = new GroupMeResponse("I chose " + computerGuess + ". We tied.");
		}
		return response;

	}

	private static int getPlayerChoice(String playerWord) {
		int choice;
		switch (playerWord.trim().toLowerCase()) {
		case "rock":
			choice = ROCK;
			break;
		case "paper":
			choice = PAPER;
			break;
		case "scissors":
			choice = SCISSORS;
			break;
		default:
			throw new IllegalArgumentException("Did you spell the word right?");

		}
		return choice;
	}

	private static int compare(int playerChoice, int computerChoice) {
		int winner = HUMAN;
		if ((playerChoice == ROCK && computerChoice == PAPER) || (playerChoice == PAPER && computerChoice == SCISSORS)
				|| (playerChoice == SCISSORS && computerChoice == ROCK)) {
			winner = COMPUTER;
		} else if (playerChoice == computerChoice) {
			winner = TIE;
		}
		return winner;
	}

	private static int getComputerChoice() {
		return rand.nextInt(SCISSORS + 1); // cause exclusive on number
	}
}
