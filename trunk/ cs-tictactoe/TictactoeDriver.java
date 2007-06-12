/*
*	Kate Kinnear
*	3165595
*	CS4725
*	TicTacToe Alpha Beta Pruning Program
*  Driver Program
*
*
*	November 19, 2006
*/

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class TictactoeDriver extends JFrame
{	
	//constants
	public static final char EMPTY = 'E';
    public static final char X = 'X';
    public static final char O = 'O';
	 public static final int NOWIN = 0;
	 public static final int XWIN = 1;
	 public static final int OWIN = 2;
    public static final int TIE = 3;
	 private static final int BUTTONSIZE = 50;
	 
	 private static JButton screenBoard[][];
	 private static JLabel reportLabel;
	 private static JTextField reportText;
	 private static JButton restart;
	 private static JButton exit;
	 
	 public GameBoard tictactoe;
	 
	private static Font myfont = new Font("Garamond", Font.BOLD, 50);
	
	//Method to catch bad input when asking for an integer board size 
	//and call itself recursively until proper input is supplied
	public int tryInputN(String s)
	{
		int N = 0;
		try {
			N = Integer.parseInt(s);
			return N;
		}
		
		catch (Exception e) {
			String nextInt = JOptionPane.showInputDialog(null, "Please enter a valid board size:");
			//Exit if user presses cancel
			if(nextInt == null)
			{
				System.exit(0);
			}
			N = tryInputN(nextInt);
		}
		
		return N;
	}
	
	//Method to catch bad input when asking for an integer board size 
	//and call itself recursively until proper input is supplied
	public int tryInputM(String s)
	{
		int M = 0;
		try {
			M = Integer.parseInt(s);
			return M;
		}
		
		catch (Exception e) {
			String nextInt = JOptionPane.showInputDialog(null, "Please enter a valid win length:");
			//Exit if user presses cancel
			if(nextInt == null)
			{
				System.exit(0);
			}
			M = tryInputM(nextInt);
		}
		
		return M;
	}

	//Driver for the tictactoe game, to be created in main
	public TictactoeDriver() throws IOException
	{
		int N;	//board size
		int M;   //Number needed for a row
		int HEIGHT, WIDTH; //size of the screen
		int numSquares;  //number of squares on the board
		int i, j; //array indexes
		int x, y; //board coordinates
		boolean gameFinished = false;
		char winner;	//Game winner
		String input;
		
		input = JOptionPane.showInputDialog(null, "Please enter the board size:");
		//Exit if user presses cancel
		if(input == null)
		{
			System.exit(0);
		}
		N = tryInputN(input);
	
		while(N < 3 || N > 10)
		{
			input = JOptionPane.showInputDialog(null, "Please enter a valid board size:");
			//Exit if user presses cancel
			if(input == null)
			{
				System.exit(0);
			}
			N = tryInputN(input);
		}
		
		if(N == 3)
		{
			M = 3;
		}
		else if(N == 4)
		{
			M = 4;
		}
		else
		{
			input = JOptionPane.showInputDialog(null, "Please enter the win length:");
			//Exit if user presses cancel
			if(input == null)
			{
				System.exit(0);
			}
			M = tryInputM(input);
	
			while(M > N || (N!=5 && M<=4) || M < 4)
			{
				input = JOptionPane.showInputDialog(null, "Please enter a valid win length:");
				//Exit if user presses cancel
				if(input == null)
				{
					System.exit(0);
				}
				M = tryInputM(input);
			}
		}
			

		numSquares = N*N;
	
		//Create new board
		tictactoe = new GameBoard(N, M);
		
		//Set up Display
		HEIGHT = (N+2) * BUTTONSIZE + 25;
		WIDTH = HEIGHT - 15;
		HEIGHT = HEIGHT + 100;
		Container pane = getContentPane();
		pane.setLayout(null);
		
		//Create game board (create and add all buttons)
		screenBoard = new JButton[N][N];
		for(i = 0; i < N; i++)
		{
			for(j = 0; j < N; j++)
				{
				screenBoard[i][j] = new JButton("");
				screenBoard[i][j].setBackground(Color.white);
				screenBoard[i][j].setSize(BUTTONSIZE,BUTTONSIZE);
				screenBoard[i][j].setLocation(i*BUTTONSIZE + BUTTONSIZE, j*BUTTONSIZE + BUTTONSIZE);
				screenBoard[i][j].addMouseListener(new BoardButtonHandler());
				pane.add(screenBoard[i][j]);
				}

		}
		
		//Add restart and Exit buttons
		JButton restart = new JButton("Restart");
		restart.setBackground(Color.white);
		restart.setSize(BUTTONSIZE*2,BUTTONSIZE);
		restart.setLocation(BUTTONSIZE/2, N*BUTTONSIZE + BUTTONSIZE*2);
		pane.add(restart);
		
		//Restart button handler
		RestartButtonHandler rbh = new RestartButtonHandler();
		restart.addActionListener(rbh);
		
		JButton exit = new JButton("Quit");
		exit.setBackground(Color.white);
		exit.setSize(BUTTONSIZE*2,BUTTONSIZE);
		exit.setLocation(WIDTH-BUTTONSIZE*2-BUTTONSIZE/2, N*BUTTONSIZE + BUTTONSIZE*2);
		pane.add(exit);
		
		//Exit button handler
		ExitButtonHandler ebh = new ExitButtonHandler();
		exit.addActionListener(ebh);
		
		
		//Set title of window
		setTitle("Intelligent TicTacToe");
		//Set color of window
		pane.setBackground(Color.red);
		//Set size of window
		setSize(WIDTH,HEIGHT);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}//end TicTacToe


	//Class to perform appropriate action when a game button is pressed
	private class BoardButtonHandler implements MouseListener
	{
		public void mouseClicked(MouseEvent e)
		{
			int row, column;
			row = -1;
			column = -1;
	
			JButton find = new JButton();
			
			//Find which of the JButtons was clicked
			find = (JButton)e.getSource();

			//Only take an action if the game is not over
			//Can't affect the board if someone has already won
			if(tictactoe.getWinner() == NOWIN)
			{
			//Compare this JButton to each in the array until a match is found.
			//Store this button's position in the array
			for(int d = 0; d < tictactoe.getSize(); d++)
			{
				for(int h = 0; h < tictactoe.getSize(); h++)
				{
					if(screenBoard[d][h].equals(e.getSource()))
					{
						column = d;
						row = h;
						//System.out.println( "ROW:" + row + " COLUMN:" + column);
					}
				}
			}
				//Check to see if it's occupied, do nothing if it is
				if(tictactoe.getTile(row,column) == EMPTY)
				{
					//Change text on button to X
					//Set this button to occupied and insert the x in the array of chars
					screenBoard[column][row].setText("X");
					//Pause for a few seconds to show the move
					screenBoard[column][row].doClick(100);

					//Update game board
					tictactoe.setTile(row,column,X);
					tictactoe.fillSquare();
					
					//System.out.println("CHECKBOARD");
					//tictactoe.printBoard();
			
					//Check for win based on this new move
					tictactoe.checkWinner(row,column,X);
					
					
					//AI's turn
					//AI only moves if there is an empty square left and if there isn't a winner yet
					if(tictactoe.getWinner() == NOWIN && tictactoe.getEmptySquares() > 0)
					{
						//Pick square, Update Board, check winner all done in this method
						tictactoe.moveOpponent(O);
						//Update button
						screenBoard[tictactoe.getMoveY()][tictactoe.getMoveX()].setText("O");
						screenBoard[tictactoe.getMoveY()][tictactoe.getMoveX()].doClick(100);
					}
					
				}
			}//end if
		}//end mouseClicked method

		public void mousePressed(MouseEvent e)
		{
		}
	
		public void mouseExited(MouseEvent e)
		{
		}

		public void mouseReleased(MouseEvent e)
		{
		}
	
		public void mouseEntered(MouseEvent e)
		{
		}

	}//end buttonhandler class
	
	
	//Class to perform appropriate action when Restart button is pressed
	//All arrays are reset, gameover is changed to false, and message is displayed
	private class RestartButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e3)
		{
			//Clear the screen and game board
			int i,j;
			for(i = 0; i < tictactoe.getSize(); i++)
			{
				for(j = 0; j < tictactoe.getSize(); j++)
				{
					screenBoard[i][j].setText("");
					tictactoe.setTile(i,j,EMPTY);
				}
			}
			
			//Reset winner and empty all squares
			tictactoe.setEmptySquares(tictactoe.getSize()*tictactoe.getSize());
			tictactoe.setWinner(NOWIN);
			tictactoe.resetMove();
				
				

		}
	}
	
	//Class to perform appropriate action when Exit button is pressed
	//All arrays are reset, gameover is changed to false, and message is displayed
	private class ExitButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e3)
		{
			System.exit(0);
		}
	}

	//Main class, only needs to create a tictactoeDriver object
	public static void main(String[] args) throws IOException
	{
		TictactoeDriver game = new TictactoeDriver();
	}//end main
	
}// end tictactoeDriver