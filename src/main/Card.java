package main;

import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

class Card extends JLabel
{
	Main.PanelTemplate panel;
	int value, faceNum;
	String suit;
	BufferedImage image;
	
	BufferedImage subImage = null;
	Color numColor = null;
	String text;
	boolean showing = true;
	
//	int x, y;
	
	Card(Main.PanelTemplate panel, int faceNum, String suit)
	{
		this.panel = panel;
		this.faceNum = faceNum;
		this.suit = suit;
		
		this.value = Math.min(this.faceNum, 10);
		
		if (this.value == 1)
			this.value = 11;
		
		try
			{image = ImageIO.read(new File("src/Suits.png"));}
		catch (IOException e) {}
		
		switch (this.suit)
		{
			case "Heart":
				subImage = image.getSubimage(0, 0, 363, image.getHeight());
				numColor = Color.RED;
				break;
				
			case "Club":
				subImage = image.getSubimage(363,  0,  363,  image.getHeight());
				numColor = Color.BLACK;
				break;

			case "Diamond":
				subImage = image.getSubimage(363*2,  0, 363,  image.getHeight());
				numColor = Color.RED;
				break;
				
			case "Spade":
				subImage = image.getSubimage(363*3,  0,  363,  image.getHeight());
				numColor = Color.BLACK;
				break;
		}
		
		
		text = "" + faceNum;
		
		switch (this.faceNum)
		{
			case 1:
				text = "A";
				break;
			case 11:
				text = "J";
				break;
			case 12:
				text = "Q";
				break;
			case 13:
				text = "K";
				break;
		}
		
		super.setVisible(true);
		this.setOpaque(true);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setFocusable(false);
		this.setVisible(true);
		this.setBackground(Color.WHITE);
//		this.setForeground(fg);
		this.setFont(new Font("Arial", Font.BOLD, 20));
//		this.setBounds(x, y, width, height);
		
	}
	
//	void draw(Graphics g, int x, int y)
//	{
//		drawSuit(g, this.suit, x, y);
//	}
	
	public void draw(int x, int y, int index)
	{
		this.draw(x,  y,  index, true);
	}
	
	public void draw(int x, int y, int index, boolean showing)
	{
//		this.x = x;
//		this.y = y;
		this.showing = showing;
		
		super.setBounds(x, y, 80, 100);
		
		this.panel.add(this, index);
		
//		this.paintComponent(g2d);
		super.repaint();
	}
	
//	@Override
	public void paintComponent(Graphics g1)
	{
//		System.out.println(3);
//		g.setColor(Color.WHITE);
//		g.fillRect(x,  y, 75,  125);
//		
//		g.setColor(numColor);
//		
//		g.setFont(new Font(g.getFont().getName(), Font.PLAIN, 30));
//		g.drawString(text, x + 5, y + 25);
//		g.drawImage(subImage.getScaledInstance(30,  30,  0), x, y+30, null);
//		
//		g.drawString(text, x + 50, y + 125);
//		g.drawImage(subImage.getScaledInstance(30,  30,  0), x + 45, y + 70, null);	
		
//		super.paintComponent(g);
		
//		System.out.println(3);
		
		Graphics2D g = (Graphics2D) g1;
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 80, 100);
		
		if (this.showing)
		{
			g.setColor(numColor);
			
			g.setFont(new Font("Arial", Font.PLAIN, 25));
			g.drawString(text, 5, 25);
			g.drawImage(subImage.getScaledInstance(20,  20,  0), 0, 25, null);
			
			g.drawString(text, 60 - this.text.length()*5, 100);
			g.drawImage(subImage.getScaledInstance(20,  20,  0), 50, 60, null);
			
			g.drawRect(0,  0, 79, 99);	
		}
		else
		{
			g.setStroke(new BasicStroke(7));

			g.setColor(Color.BLACK);
			g.drawOval(15, 10, 50, 50);
			g.drawLine(40, 60, 40, 80);
			g.fillOval(35, 85, 10, 10);
			
			g.setColor(Color.WHITE);
			g.fillRect(10, 8, 15, 50);
			g.fillRect(10, 50, 27, 20);
			
		}
		
	}
}






