/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ics3u.bingo;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
/**
 *
 * @author saada
 */

public class ICS3UBingo {
	
	private static JButton[][] callBoard = new JButton[5][16];
	private static JButton[][] playerBoard1 = new JButton[5][5];
	private static JButton[][] playerBoard2 = new JButton[5][5];
	private static JButton[][] cpuBoard1 = new JButton[5][5];
	private static JButton[][] cpuBoard2 = new JButton[5][5];
	private static JLabel callDisplay = new JLabel("Get ready to play BINGO!");
	private static int call;
	private static Timer timer = new Timer(4000, null);
	private static boolean validDaub = false;
	private static JPanel panel = new JPanel();
	private static JPanel callBoardPanel = new JPanel();
	private static JPanel playerPanel1 = new JPanel();
	private static JPanel playerPanel2 = new JPanel();
	private static JPanel cpuPanel1 = new JPanel();
	private static JPanel cpuPanel2 = new JPanel();
	private static JPanel fullPlayerPanel = new JPanel();
	private static JPanel fullCpuPanel = new JPanel();
	private static JMenuBar menuBar = new JMenuBar();
	private static JMenu menu = new JMenu("File");
	private static JMenuItem newGame = new JMenuItem("New game", KeyEvent.VK_N);
	private static JMenuItem exit = new JMenuItem("Exit", KeyEvent.VK_E);
	private static JMenuItem callBingo = new JMenuItem("Call BINGO", KeyEvent.VK_B);
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("BingoGUI");
		
		newGame.setMnemonic(KeyEvent.VK_N);
		newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		exit.setMnemonic(KeyEvent.VK_E);
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		callBingo.setMnemonic(KeyEvent.VK_B);
		callBingo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
		
		callBingo.addActionListener(new callBingo());
		exit.addActionListener(new exitApp());
		newGame.addActionListener(new newGame());
		
		createGame();
		
		frame.setJMenuBar(menuBar);
		frame.setVisible(true);
		frame.setSize(500, 500);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(panel);
		frame.pack();
		
		ActionListener taskPerformer = new ActionListener() {
			
		    public void actionPerformed(ActionEvent evt) {
		    	Random rand = new Random();
		    	call = rand.nextInt(75)+ 1;
		    	loop:
		    	while (true) { // Prevents repeat calls
					for (int row = 0; row < callBoard.length; row++) {
						for (int col = 0; col < callBoard[row].length; col++) {
							if (callBoard[row][col].getText().equals(Integer.toString(call)) && callBoard[row][col].isContentAreaFilled()) {
								call = rand.nextInt(75)+ 1;
							} else if (callBoard[row][col].getText().equals(Integer.toString(call)) && !(callBoard[row][col].isContentAreaFilled())) {
								break loop;
							}
						}
					}
		    	}
				if (call <= 15) {
					callDisplay.setText("Call is: B" + Integer.toString(call)); // Displays call
				} else if (call <= 30) {
					callDisplay.setText("Call is: I" + Integer.toString(call));
				} else if (call <= 45) {
					callDisplay.setText("Call is: N" + Integer.toString(call));
				} else if (call <= 60) {
					callDisplay.setText("Call is: G" + Integer.toString(call));
				} else if (call <= 75) {
					callDisplay.setText("Call is: O" + Integer.toString(call));
				}
				for (int row = 0; row < callBoard.length; row++) { // Daubs call board
					for (int col = 0; col < callBoard[row].length; col++) {
						if (callBoard[row][col].getText().equals(Integer.toString(call))) {
							callBoard[row][col].setContentAreaFilled(true);
							callBoard[row][col].setBackground(Color.BLUE);
							callBoard[row][col].setForeground(Color.WHITE);
						}
					}
				}
				for (int row = 0; row < cpuBoard1.length; row++) { // Daubs CPU board
					for (int col = 0; col < cpuBoard1[row].length; col++) {
						if (cpuBoard1[row][col].getText().equals(Integer.toString(call))) {
							cpuBoard1[row][col].setContentAreaFilled(true);
							cpuBoard1[row][col].setBackground(Color.BLUE);
							cpuBoard1[row][col].setForeground(Color.WHITE);
						}
						if (cpuBoard2[row][col].getText().equals(Integer.toString(call))) {
							cpuBoard2[row][col].setContentAreaFilled(true);
							cpuBoard2[row][col].setBackground(Color.BLUE);
							cpuBoard2[row][col].setForeground(Color.WHITE);
						}
					}
				}
				if (checkWinner(cpuBoard1) || checkWinner(cpuBoard2)) {
				timer.stop();
				JFrame cpuWin = new JFrame("CPU Win");
				callDisplay.setText("Select New Game from menu to play again");
				JOptionPane.showMessageDialog(cpuWin, "CPU Win!");
				disableBoard();
				}
		    }

		};
		timer.addActionListener(taskPerformer);
		timer.setRepeats(true);
		timer.start();
	}
	
	private static class exitApp implements ActionListener {
		
		public void actionPerformed(ActionEvent event) {
			System.exit(0);
		}
	}
	
	private static class newGame implements ActionListener {
		
		public void actionPerformed(ActionEvent event) {
			timer.stop();
			panel.removeAll();
			callBoardPanel.removeAll();
			cpuPanel1.removeAll();
			cpuPanel2.removeAll();
			playerPanel1.removeAll();
			playerPanel2.removeAll();
			panel.revalidate();
			panel.repaint();
			createGame();
			callDisplay.setText("Get ready to play BINGO!");
			panel.revalidate();
			panel.repaint();
			timer.start();
		}
	}
	
	private static class callBingo implements ActionListener {
		
		public void actionPerformed(ActionEvent event) {
			if (checkWinner(playerBoard1) || checkWinner(playerBoard2)) {
				timer.stop();
				JFrame playerWin = new JFrame("You win!");
				JOptionPane.showMessageDialog(playerWin, "Congratulations! You win!");
				callDisplay.setText("Select New Game from menu to play again");	
				disableBoard();		
			} else if (!(checkWinner(playerBoard1) || checkWinner(playerBoard2))) {
				timer.stop();
				JFrame playerWin = new JFrame("You lose!");
				JOptionPane.showMessageDialog(playerWin, "False call! You lose!");
				callDisplay.setText("Select New Game from menu to play again");
				disableBoard();
			}
		}
	}
	
	public static void createGame() {
		
		int y = 1;
		
		for (int row = 0; row < callBoard.length; row++) { // Create call board
			for (int col = 0; col < callBoard[row].length; col++) {
				if (col == 0) {
					switch (row) {
						case 0:
							callBoard[0][0] = new JButton("B");
							break;
						case 1:
							callBoard[1][0] = new JButton("I");
							break;
						case 2:
							callBoard[2][0] = new JButton("N");
							break;
						case 3:
							callBoard[3][0] = new JButton("G");
							break;
						case 4:
							callBoard[4][0] = new JButton("O");
							break;
					}
					callBoard[row][col].setForeground(Color.MAGENTA);
					callBoard[row][col].setFont(new Font("Arial", Font.PLAIN, 20));	
				} else {
					callBoard[row][col] = new JButton(Integer.toString(y));;
					callBoard[row][col].setForeground(Color.BLUE);;
					callBoard[row][col].setFont(new Font("Arial", Font.PLAIN, 15));
					y++;
				}
				callBoard[row][col].setRolloverEnabled(false);
				callBoard[row][col].setContentAreaFilled(false);
				callBoard[row][col].setPreferredSize(new Dimension(50, 50));
				callBoardPanel.add(callBoard[row][col]);
			}
		}
		
		callDisplay.setFont(new Font("Arial", Font.PLAIN, 20));	
		callDisplay.setForeground(Color.MAGENTA);
		
		UIManager.put("TitledBorder.font", new Font("Arial", Font.PLAIN, 20));
		
		createBoard(playerBoard1, playerPanel1, false);
		createBoard(playerBoard2, playerPanel2, false);
		createBoard(cpuBoard1, cpuPanel1, true);
		createBoard(cpuBoard2, cpuPanel2, true);

		callBoardPanel.setLayout(new GridLayout(5, 16, 0, 0));
		callBoardPanel.setBorder(BorderFactory.createTitledBorder("Call Board"));
		fullPlayerPanel.setBorder(BorderFactory.createTitledBorder("Player Boards"));
		fullCpuPanel.setBorder(BorderFactory.createTitledBorder("CPU Boards"));
		
		menuBar.add(menu);
		menu.add(newGame);
		menu.add(callBingo);
		menu.add(exit);
		
		fullPlayerPanel.add(playerPanel1);
		fullPlayerPanel.add(playerPanel2);
		fullCpuPanel.add(cpuPanel1);
		fullCpuPanel.add(cpuPanel2);
		panel.setLayout(new BorderLayout());
		panel.add(callBoardPanel, BorderLayout.NORTH);
		panel.add(fullCpuPanel, BorderLayout.WEST);
		panel.add(fullPlayerPanel, BorderLayout.EAST);
		panel.add(callDisplay, BorderLayout.SOUTH);
	}
	
	public static void createBoard(JButton[][] board, JPanel panel, boolean cpu) { // Generates CPU and player boards
		JButton[] bingo = new JButton[5];
		bingo[0] = new JButton("B");
		bingo[1] = new JButton("I");
		bingo[2] = new JButton("N");
		bingo[3] = new JButton("G");
		bingo[4] = new JButton("O");
		
		for (int i = 0; i < bingo.length; i++) {
			bingo[i].setContentAreaFilled(false);
			bingo[i].setForeground(Color.MAGENTA);
			bingo[i].setPreferredSize(new Dimension(50, 50));
			bingo[i].setRolloverEnabled(false);
			bingo[i].setFont(new Font("Arial", Font.PLAIN, 20));
			panel.add(bingo[i]);	
		}
		Random rand = new Random();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				board[row][col] = new JButton(); 
			}
		}
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				int x = rand.nextInt(15) + col * 15 + 1;
				while (Integer.toString(x).equals(board[0][col].getText()) || Integer.toString(x).equals(board[1][col].getText()) || Integer.toString(x).equals(board[2][col].getText()) || Integer.toString(x).equals(board[3][col].getText()) || Integer.toString(x).equals(board[4][col].getText())) {
					x = rand.nextInt(15) + col * 15 + 1;
				}
				board[row][col] = new JButton(Integer.toString(x)); 
				board[row][col].setPreferredSize(new Dimension(50, 50));
				board[row][col].setFont(new Font("Arial", Font.PLAIN, 15));
				board[row][col].setContentAreaFilled(false);
				panel.add(board[row][col]);
				if (cpu) {
					board[row][col].setForeground(Color.BLUE);
					board[row][col].setRolloverEnabled(false);
					board[2][2].setBackground(Color.BLUE);
				} else {
					board[row][col].setForeground(Color.RED);
					board[row][col].addActionListener(new DaubBoard());
					board[2][2].setBackground(Color.RED);
				}
			}
		}
		panel.setLayout(new GridLayout(6, 5, 0, 0));
		board[2][2].setContentAreaFilled(true);
		board[2][2].setForeground(Color.WHITE);
	}
	
	private static class DaubBoard implements ActionListener { // Allows player to daub board
		
		public void actionPerformed(ActionEvent event) {
			for (int row = 0; row < playerBoard1.length; row++) {
				for (int col = 0; col < playerBoard1[row].length; col++) {
					if (playerBoard1[row][col] == event.getSource() && playerBoard1[row][col].getText().equals(Integer.toString(call))) {
						validDaub = true;
					} else if (playerBoard2[row][col] == event.getSource() && playerBoard2[row][col].getText().equals(Integer.toString(call))) {
						validDaub = true;
					}
				}
			}
			if (validDaub) {
				for (int row = 0; row < playerBoard1.length; row++) {
					for (int col = 0; col < playerBoard1[row].length; col++) {
						if (playerBoard1[row][col] == event.getSource() && playerBoard1[row][col].getText().equals(Integer.toString(call))) {
							playerBoard1[row][col].setContentAreaFilled(true);
							playerBoard1[row][col].setBackground(Color.RED);
							playerBoard1[row][col].setForeground(Color.WHITE);
						} else if (playerBoard2[row][col] == event.getSource() && playerBoard2[row][col].getText().equals(Integer.toString(call))) {
							playerBoard2[row][col].setContentAreaFilled(true);
							playerBoard2[row][col].setBackground(Color.RED);
							playerBoard2[row][col].setForeground(Color.WHITE);
						}
					}
				}
			} else {
				timer.stop();
				JFrame falseDaub = new JFrame("False Daub");
				callDisplay.setText("Select New Game from menu to play again");
				JOptionPane.showMessageDialog(falseDaub, "False daub! You lose!");
				disableBoard();
			}
			validDaub = false;
		}
	}
	
	public static boolean checkWinner(JButton[][] board) {
		if ((board[0][0].isContentAreaFilled() && board[0][1].isContentAreaFilled() && board[0][2].isContentAreaFilled() && board[0][3].isContentAreaFilled() && board[0][4].isContentAreaFilled())
			|| (board[1][0].isContentAreaFilled() && board[1][1].isContentAreaFilled() && board[1][2].isContentAreaFilled() && board[1][3].isContentAreaFilled() && board[1][4].isContentAreaFilled())
				|| (board[2][0].isContentAreaFilled() && board[2][1].isContentAreaFilled() && board[2][3].isContentAreaFilled() && board[2][4].isContentAreaFilled())
					|| (board[3][0].isContentAreaFilled() && board[3][1].isContentAreaFilled() && board[3][2].isContentAreaFilled() && board[3][3].isContentAreaFilled() && board[3][4].isContentAreaFilled())
						|| (board[0][0].isContentAreaFilled() && board[1][0].isContentAreaFilled() && board[2][0].isContentAreaFilled() && board[3][0].isContentAreaFilled() && board[4][0].isContentAreaFilled())
							|| (board[0][1].isContentAreaFilled() && board[1][1].isContentAreaFilled() && board[2][1].isContentAreaFilled() && board[3][1].isContentAreaFilled() && board[4][1].isContentAreaFilled())
								|| (board[0][2].isContentAreaFilled() && board[1][2].isContentAreaFilled() && board[3][2].isContentAreaFilled() && board[4][2].isContentAreaFilled())
									|| (board[0][3].isContentAreaFilled() && board[1][3].isContentAreaFilled() && board[2][3].isContentAreaFilled() && board[3][3].isContentAreaFilled() && board[4][3].isContentAreaFilled())
										|| (board[0][4].isContentAreaFilled() && board[1][4].isContentAreaFilled() && board[2][4].isContentAreaFilled() && board[3][4].isContentAreaFilled() && board[4][4].isContentAreaFilled())
											|| (board[0][0].isContentAreaFilled() && board[1][1].isContentAreaFilled() && board[3][3].isContentAreaFilled() && board[4][4].isContentAreaFilled())
												|| (board[4][0].isContentAreaFilled() && board[4][1].isContentAreaFilled() && board[4][2].isContentAreaFilled() && board[4][3].isContentAreaFilled() && board[4][4].isContentAreaFilled())
													|| (board[0][4].isContentAreaFilled() && board[1][3].isContentAreaFilled() && board[3][1].isContentAreaFilled() && board[4][0].isContentAreaFilled())) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void disableBoard() {
		for (int row = 0; row < playerBoard1.length; row++) {
			for (int col = 0; col < playerBoard1[row].length; col++) {
				playerBoard1[row][col].setEnabled(false);
				playerBoard2[row][col].setEnabled(false);
			}
		}
	}
}