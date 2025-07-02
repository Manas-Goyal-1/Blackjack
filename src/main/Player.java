package main;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Player implements MouseListener, ActionListener
{
	ArrayList<Card> hand = new ArrayList<>();
	int bank = 5000;
	int bet = 0;
	
	JButton leaveButton;
	
	JButton[] playingButtons = new JButton[2];
	JLabel resultLabel;
	
	Token[] bettingTokens = new Token[5];
	JLabel betLabel;
	JLabel bankLabel;
	
//	JLabel total;
//	int handTotal = 0;
	
	JButton readyButton;
	boolean ready = false;	
	
//	boolean playing = true;
	
	Main.PanelTemplate panel;
	int x, y;
	
	Player(Main.PanelTemplate panel, int x, int y)
	{
		this.panel = panel;
		this.x = x;
		this.y = y;
		
		this.playingButtons[0] = makeJButton("Hit", new Color(44, 219, 29), x, y + 40, 100, 30, this);
		this.playingButtons[1] = makeJButton("Stay", new Color(230, 213, 32), x + 110, y + 40, 100, 30, this);
//		this.buttons[2] = makeJButton("Leave", Color.RED, x +55, y + 40);
		
		this.leaveButton = makeJButton("Leave", Color.RED, x + 55, y + 220, 100, 30, panel);
		this.leaveButton.setVisible(true);

		this.bettingTokens[0] = new Token(panel, this, 1, Color.YELLOW, x - 51, y + 110);
		this.bettingTokens[1] = new Token(panel, this, 10, Color.RED, x + 11, y + 110);
		this.bettingTokens[2] = new Token(panel, this, 40, Color.GREEN, x + 73, y + 110);
		this.bettingTokens[3] = new Token(panel, this, 100, Color.BLUE, x + 135, y + 110);
		this.bettingTokens[4] = new Token(panel, this, 250, Color.BLACK, x + 197, y + 110);
		
		this.readyButton = makeJButton("Ready?", new Color(50, 170, 30), x + 30, y + 180, 150, 35, this);
		this.betLabel = makeJLabel("Bet: $0", 20, new Color(170, 170, 170, 100), Color.WHITE, x + 30, y - 20, 150, 40);
		this.bankLabel = makeJLabel("Bank: $"+this.bank, 20, new Color(170, 170, 170, 100), Color.WHITE, x+30, y + 30, 150, 40);
		
		this.resultLabel = makeJLabel("Didn't play", 30, Color.BLACK, Color.WHITE, x-40, y - 160, 250, 50);
		
//		this.total = makeJLabel("0", 20, Color.BLACK, Color.WHITE, x, y + 80, 250, 50);
		
	}
	
	JLabel makeJLabel(String text, int size, Color bg, Color fg, int x, int y, int width, int height)
	{
		JLabel label = new JLabel(text);
		label.setOpaque(true);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFocusable(false);
		label.setVisible(false);
		label.setBackground(bg);
		label.setForeground(fg);
		label.setFont(new Font("Arial", Font.BOLD, size));
		label.setBounds(x, y, width, height);
		this.panel.add(label);
		return label;
	}
	
	JButton makeJButton(String text, Color bg, int x, int y, int w, int h, ActionListener al)
	{
		JButton button = new JButton(text);
		
		button.setOpaque(true);
		button.setHorizontalAlignment(SwingConstants.CENTER);
		button.setFont(new Font("Arial", Font.PLAIN, 20));
		button.setFocusable(false);
		button.addActionListener(al);
		button.setBorderPainted(false);
		button.setVisible(false);
		button.setBackground(bg);
		button.setBounds(x,  y, w,  h);
		this.panel.add(button);
		return button;
	}
	
	void draw()
	{
		this.showPlayingComponents(false);
		this.showBettingComponents(false);
		this.showResults(false);

		// Betting time
		if (this.panel.state == 0)
		{
			this.showBettingComponents(true);
//			System.out.println(7);
		}
		
		// Playing Time
		else if (this.panel.state == 1 && this.bet != 0)
		{
			this.drawHand();
			if (this == this.panel.currPlayingPlayer)
				this.showPlayingComponents(true);
		}
		else if (this.panel.state == 2)
			this.showResults(true);
		
	}
	
	void endTurn(double win, String result)
	{		
		this.bank += this.bet * win;
		this.bet = 0;
		this.ready = false;
		
		this.bankLabel.setText("Bank: $" + this.bank);
		this.betLabel.setText("Bet: $" + this.bet);
		this.readyButton.setBackground( new Color(50, 170, 30));
		this.readyButton.setText("Ready?");
		
		this.resultLabel.setText(result);
	}
	
	void showResults(boolean showing)
	{
		this.bankLabel.setVisible(showing);
		this.resultLabel.setVisible(showing);
	}
	
	void drawHand()
	{
		for (int i = 0; i < this.hand.size(); i++)
		{
			Card card = this.hand.get(i);
//			this.panel.setComponentZOrder(card,  i);
			card.draw(this.x + i*50, this.y - 100, i);
//			System.out.println(1);
		}
	}
	
	void showBettingComponents(boolean showing)
	{
//		System.out.println(10);
		// Betting chips
		for (Token token: this.bettingTokens)
//			if (token != null)
				token.setVisible(showing);
		
		// Labels and other buttons
		this.betLabel.setVisible(showing);
		this.bankLabel.setVisible(showing);
		this.readyButton.setVisible(showing);
		this.leaveButton.setVisible(showing);
	}

	void showPlayingComponents(boolean showing)
	{
		// Hit/ Stay Buttons
		for (JButton button: this.playingButtons)
			button.setVisible(showing);
//		
//		this.total.setText("" + this.panel.getHandTotal(this.hand));
//		this.total.setVisible(showing);
		
//		// Cards in hand
//		for (Card card: this.hand)
//		{}
		
	}
	
	void removeComponents()
	{
		for (JButton button: this.playingButtons)
			this.panel.remove(button);
		for (Token token: this.bettingTokens)
			this.panel.remove(token);
		this.panel.remove(this.betLabel);
		this.panel.remove(this.leaveButton);
		this.panel.remove(this.bankLabel);
		this.panel.remove(this.readyButton);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.readyButton)
		{
//			System.out.println(5);
			if (!this.ready)
			{
				this.ready = true;
				this.readyButton.setText("Not Ready?");
				this.readyButton.setBackground(Color.YELLOW);
				this.panel.checkToStart();
			}
			else
			{
				this.ready = false;
				this.readyButton.setText("Ready?");
				this.readyButton.setBackground(new Color(50, 170, 30));
			}
		}
		
		else if (e.getSource() == this.playingButtons[0])
		{
			this.panel.dealCard(this.hand);
//			this.hand.add(this.panel.deck.deal());
			if (this.panel.getHandTotal(this.hand) > 21)
				this.panel.nextPlayerTurn();
		}
		else if (e.getSource() == this.playingButtons[1])
			this.panel.nextPlayerTurn();
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		if (!this.ready)
		{
			this.bet = Math.min(Math.max(this.bet + (2 - e.getButton()) * ((Token) e.getComponent()).value, 0), this.bank);
			this.betLabel.setText("Bet: $" + this.bet);			
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
	
	private static class Token extends JLabel
	{
//		int x, y;
		int value;
		Color bg;
		
		Token(Main.PanelTemplate panel, Player player, int value, Color bg, int x, int y)
		{
			this.value = value;
			this.bg = bg;
//			this.x = x;
//			this.y = y;
			
//			this.setContentAreaFilled(false);
			
			super.setOpaque(true);
			super.setHorizontalAlignment(SwingConstants.CENTER);
			super.setFont(new Font("Arial", Font.PLAIN, 20));
			super.setFocusable(false);
//			this.setBorderPainted(false);
			super.setVisible(false);
//			this.setBackground(new Color(220, 220, 220));
			super.setBounds(x,  y, 60, 60);
			super.addMouseListener(player);
			panel.add(this);
//			this.addActionListener(panel);
		}
		
		@Override
		public void paintComponent(Graphics g)
		{
			Graphics2D g2d = (Graphics2D) g;

//			super.paintComponent(g2d);
			
			g2d.setColor(Color.WHITE);
			g2d.fillOval(5,  5,  50,  50);
			
			g2d.setColor(this.bg);
			g2d.setStroke(new BasicStroke(10));
			g2d.drawOval(5, 5, 50, 50);
			
			g2d.setColor(Color.BLACK);
			g2d.setStroke(new BasicStroke(1));
			g2d.drawOval(5, 5, 50, 50);
			
			g2d.setColor(Color.BLACK);
			g2d.setFont(new Font("Arial", Font.BOLD, 20));
			
			String text = "" + this.value;
			g2d.drawString(text, 26 - text.length()*4, 35);
		}
	}
}




