package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Main{
	public static void main(String[] args)
	{
		int w = 1000;
		int h = 600;
		
		JFrame frame = new JFrame("Blackjack");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
//		JTextField numPlayerInput = new JTextField("Enter the numbers of players");
		
		frame.add(new PanelTemplate(w, h));
		
		frame.pack();
		frame.setVisible(true);
	}

	static class PanelTemplate extends JPanel implements ActionListener
	{
		final int w, h;
		private Deck deck;
		private int numPlayers = 3;
		
		Player[] players = new Player[numPlayers];
		JButton[] joinButtonArray = new JButton[numPlayers];
//		boolean playing = false;
		int state = 0;
		
		Player currPlayingPlayer = null;
		
		ArrayList<Player> playingPlayers = new ArrayList<>();
		ArrayList<Card> dealerHand = new ArrayList<>();
		
//		Timer timer = new Timer(5000, this);
		JButton replayButton;
		
//		ArrayList<ArrayList<JButton>> buttons = new ArrayList<ArrayList<JButton>>();
//		ArrayList<JButton>[] buttons = new ArrayList[3];
//		JButton[][] buttons = new JButton[3][3];
		
		PanelTemplate(int w, int h)
		{
			this.w = w;
			this.h = h;
			
			super.setPreferredSize(new Dimension(w, h));
			super.setBackground(new Color(220, 220, 220));
			super.setLayout(null);
			
			this.deck = new Deck(this);
			
			for (int i = 0; i < numPlayers; i++)
			{
				int x = 50 + i*345 + 55; 
				int y = 360 - Math.abs(1 - i) * 40;
				
				JButton joinButton = new JButton("Join");
				joinButton.setBackground(new Color(150, 150, 150));
				joinButton.setOpaque(true);
				joinButton.setHorizontalAlignment(SwingConstants.CENTER);
				joinButton.setFont(new Font("Arial", Font.PLAIN, 20));
				joinButton.setBounds(x, y, 100, 30);
				joinButton.setFocusable(false);
				joinButton.addActionListener(this);
				joinButton.setBorderPainted(false);
				joinButton.setVisible(true);
				this.add(joinButton);
				this.joinButtonArray[i] = joinButton;
			}
			
			 this.replayButton = new JButton("Play Again!");
			 this.replayButton.setBackground(Color.GREEN);
			 this.replayButton.setHorizontalAlignment(SwingConstants.CENTER);
			 this.replayButton.setFont(new Font("Arial", Font.PLAIN, 20));
			 this.replayButton.setVisible(false);
			 this.replayButton.setBounds(400, 450, 200, 50);
			 this.replayButton.addActionListener(this);
			 this.add(this.replayButton);
		
		}

		Void checkToStart()
		{
			for (Player player: this.players)
			{
//				System.out.println(player);
				if (player != null)
					if (!player.ready)
						return null;
			}
//			this.startGame();
			
			// Storing the playing players in an ArrayList.
//			ArrayList<Player> playingPlayers = new ArrayList<>();
//			ArrayList<Card> dealerHand = new ArrayList<>();
//			
			for (Player player: this.players)
				if (player != null && player.bet != 0)
				{
					this.playingPlayers.add(player);
					player.bank -= player.bet;
				}
			
			// Checking if somebody is playing?
			if (this.playingPlayers.size() == 0)
				return null;
			
//			for (Player player: this.players)

			// Game Starts
//			this.playing = true;
			this.state = 1;
			
			// Hiding the join buttons
			this.showJoinButtons(false);

			for (int i = 0; i < 2; i++)
			{
				for (Player player: playingPlayers)
				{
//					System.out.println(player);
					this.dealCard(player.hand);
//					player.hand.add(this.deck.deal());
//					System.out.println(player.hand);
				}
				this.dealCard(dealerHand);
//				dealerHand.add(this.deck.deal());
			}

			this.currPlayingPlayer = this.playingPlayers.get(0);
			if (this.checkForBlackjack(this.currPlayingPlayer.hand))
				this.nextPlayerTurn();
//			this.playingPlayers.get(0).checkForBlackjack();
			
			super.repaint();
			return null;
		}
		
		boolean checkForBlackjack(ArrayList<Card> hand)
		{
			return this.getHandTotal(hand) == 21 && hand.size() == 2;
		}
		
		void dealCard(ArrayList<Card> hand)
		{
			hand.add(this.deck.deal());
		}
		
		void nextPlayerTurn()
		{
			int index = this.playingPlayers.indexOf(this.currPlayingPlayer);
			super.repaint();
			if (index == this.playingPlayers.size() - 1)
			{
				this.currPlayingPlayer = null;
				this.evaluate();
			}
			else
			{
				this.currPlayingPlayer = this.playingPlayers.get(index + 1);
				if (this.checkForBlackjack(this.currPlayingPlayer.hand))
					this.nextPlayerTurn();
			}
//			super.repaint();
		}
		
		int getHandTotal(ArrayList<Card> hand)
		{
			int total = 0;
			
			for (Card card: hand)
				total += card.value;
			
			if (total > 21)
				for (Card card: hand)
					if (card.value == 11)
					{
						card.value = 1;
						total = this.getHandTotal(hand);
						break;
					}
			
			return total;
		}
		
		void evaluate()
		{
			this.completeDealerHand();
			
			int dealerTotal = this.getHandTotal(this.dealerHand);
			boolean dealerBlackjack = this.checkForBlackjack(this.dealerHand);
			
			// Can I just make it work without storing the totals?
//			int[] totals = new int[this.playingPlayers.size()];
			
			for (Player player: this.playingPlayers)
			{
//				int total = this.getHandTotal(this.playingPlayers.get(i).hand);
//				totals[i] = total;
				
//				Player player = this.playingPlayers.get(i);
				
				int total = this.getHandTotal(player.hand);
				boolean blackjack = this.checkForBlackjack(player.hand);
				
				if (blackjack)
				{
					if (dealerBlackjack)
						player.endTurn(1, "You Tied");
					else
						player.endTurn(2.5, "Blackjack!!");
				}
				else if (total > 21)
					player.endTurn(0,  "You Busted!");
				
				else if (dealerTotal > 21)
					player.endTurn(2, "You Win!");
				
				else if (total > dealerTotal)
					player.endTurn(2, "You Win!");
				
				else if (total < dealerTotal)
					player.endTurn(0, "You Lose!");
				
				else
					player.endTurn(1, "You Tied");
			}
			
			this.state = 2;
			
			this.replayButton.setVisible(true);
//			this.timer.start();
//			this.timer.setRepeats(false);
			
//			this.playing = false;
		}
		
		private void showJoinButtons(boolean showing)
		{
			for (int i = 0; i < 3; i++)
			{
				JButton joinBtn = this.joinButtonArray[i];
				joinBtn.setVisible(false);
				
				if (this.players[i] == null && showing)
					joinBtn.setVisible(true);
			}
 			
//			for (JButton button: this.joinButtonArray)
//				if (button != null)
//					button.setVisible(showing);
		}
		
		private void removeCards(ArrayList<Card> hand)
		{
			for (Card card: hand)
			{
				this.remove(card);
//				System.out.println(card);
			}
			
			super.repaint();
		}
		
		private void completeDealerHand()
		{
			while (this.getHandTotal(this.dealerHand) <= 16)
				this.dealCard(this.dealerHand);
			this.drawDealerHand();
		}
		
		private void drawDealerHand()
		{
			if (this.currPlayingPlayer != null)
			{
				this.dealerHand.get(0).draw(400,  60,  0);
				this.dealerHand.get(1).draw(485,  60,  0, false);
			}
			else
			{
				this.removeCards(this.dealerHand);
				for (int i = 0; i < this.dealerHand.size(); i++)
				{
					Card card = dealerHand.get(i);
					card.draw(300 + 85*i, 60, i);
				}
			}
		}

		@Override
		public void paintComponent(Graphics g)
		{
//			super.removeAll();
			
			Graphics2D g2d = (Graphics2D) g;
			super.paintComponent(g2d);
			
//			Table
			g2d.setColor(new Color(0, 100, 25));
			g2d.fillRect(0, 0, this.w, 325);
			g2d.fillArc(0, 225, this.w, 200, 180, 180);
			
			g2d.setColor(Color.BLACK);
			g2d.setStroke(new BasicStroke(4));
			g2d.drawArc(0, 225, this.w, 200, 180, 180);
			
//			Dealer Name
			g2d.setColor(Color.WHITE);
			g2d.setFont(new Font("Arial", Font.BOLD, 40));
			g2d.drawString("Dealer", 437, 50);
			
//			System.out.println(1);

			// Players
			for (Player player: this.players)
				if (player != null)
					player.draw();
			
//			this.deck.deal().draw(g2d,  200,  100);
			
//			System.out.println(1);
			
			if (this.state == 1)
				this.drawDealerHand();
			
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
//			System.out.println(-1);
			Object source = e.getSource();
			for (int i = 0; i < numPlayers; i++)
			{
				JButton joinBtn = this.joinButtonArray[i];
				Player player = this.players[i];

				// Checking if someone joins.
				if (source == joinBtn)
				{
//					Why can't I use player? Do these not share the same memory address?
					this.players[i] = new Player(this, joinBtn.getX() - 55, joinBtn.getY() - 30);
					this.showJoinButtons(true);
//					joinBtn.setVisible(false);
				}
				
				// Checking for player inputs (buttons).
				else if (player != null)
				{
					// Checking if 'leave' button is pressed.
					if (source == player.leaveButton)
					{
						player.removeComponents();
						this.players[i] = null;
						joinBtn.setVisible(true);
					}
				}
//				else
//				{
////					for ()
//				}
			}
			if (source == this.replayButton)
			{
				this.state = 0;
				this.showJoinButtons(true);
				for (Player p: this.playingPlayers)
				{
					this.removeCards(p.hand);
					p.hand.clear();
				}
				this.removeCards(this.dealerHand);
				this.dealerHand.clear();
				this.playingPlayers.clear();
				this.replayButton.setVisible(false);
			}
			super.repaint();

//			System.out.println(100);
//			super.repaint();
		}
	}
}


