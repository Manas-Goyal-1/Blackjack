package main;

import java.util.Random;
import java.util.Collections;
import java.util.ArrayList;

public class Deck {
	Main.PanelTemplate panel;
	int cutNumber;
	ArrayList<Card> deck = new ArrayList<Card>();
	boolean shuffleNextTurn = false;
	int numDecks = 4;
	
	Deck(Main.PanelTemplate panel)
	{
		this.panel = panel;		
		makeDeck(numDecks);
	}
	
	private void makeDeck(int numDecks)
	{
		this.deck.clear();
		
		this.cutNumber = new Random().nextInt((numDecks - 1) * 52 / 2) + 52 * numDecks/2;
		
		String[] suits = {"Spade", "Heart", "Diamond", "Club"};
		
		for (int i = 0; i < numDecks; i++)
			for (int j = 0; j < 4; j++)
				for (int k = 1; k <= 13; k++)
					this.deck.add(new Card(this.panel, k, suits[j]));
		
		Collections.shuffle(this.deck);
	}
	
	Card deal()
	{
		if (4*52 - cutNumber >= 0)
			this.shuffleNextTurn = true;
		
		Card card = this.deck.get(0);
		this.deck.remove(0);
		
		if (this.deck.size() == 0)
			this.makeDeck(numDecks);
		
		return card;
	}
}







