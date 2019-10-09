import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import static javax.swing.BoxLayout.Y_AXIS;

public class DisplayMap {

	private JPanel mapPanel;
	private JFrame mainFrame;
	private JPanel otherPanel;
	private JScrollPane scrollPanel;
	private JPanel statPanel;

	private JTextPane txtServerCMD = new JTextPane();
	private JLabel lblCurrentPos = new JLabel("");
	private JLabel lblOrientation = new JLabel("");
	private JLabel lblType = new JLabel("");
	private JLabel lblCurrentCol = new JLabel("");
	private JLabel lblRGB = new JLabel("");
	private JLabel lblVictims = new JLabel("");
	private JLabel lblObstacle = new JLabel("");
	private JLabel lblTarget = new JLabel("");

	private String[][] grid = new String[6][6];

	public DisplayMap() {
		JFrame frame = new JFrame("Display");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(1240, 650));
		frame.setLayout(new FlowLayout(FlowLayout.LEFT));
		frame.setResizable(false);
		mainFrame = frame;

		JPanel mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(600, 600));
		mainPanel.setLayout(new GridLayout(6, 6));
		mapPanel = mainPanel;

		JPanel otherPnl = new JPanel();
		otherPnl.setLayout(new GridLayout(2, 1));
		otherPanel = otherPnl;

		JPanel stPanel = new JPanel();
		stPanel.setLayout(new BoxLayout(stPanel, Y_AXIS));
		stPanel.setPreferredSize(new Dimension(600, 300));
		stPanel.add(lblCurrentPos);
		stPanel.add(lblOrientation);
		stPanel.add(lblType);
		stPanel.add(lblCurrentCol);
		stPanel.add(lblRGB);
		stPanel.add(lblObstacle);
		stPanel.add(lblVictims);
		stPanel.add(lblTarget);

		Font f = new Font("Arial", Font.BOLD, 15);
		Font sf = new Font("Arial", Font.PLAIN, 14);

		txtServerCMD.setFont(sf);

		lblCurrentPos.setFont(f);
		lblOrientation.setFont(f);
		lblType.setFont(f);
		lblCurrentCol.setFont(f);
		lblRGB.setFont(f);
		lblObstacle.setFont(f);
		lblVictims.setFont(f);
		lblTarget.setFont(f);

		statPanel = stPanel;

		txtServerCMD.setContentType("text/html");

		txtServerCMD.setEditable(false);
		JScrollPane scPanel = new JScrollPane(txtServerCMD, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scPanel.setPreferredSize(new Dimension(600, 300));
		scrollPanel = scPanel;

		otherPanel.add(statPanel);
		otherPanel.add(scrollPanel);

		mainFrame.add(mapPanel);
		mainFrame.add(otherPanel);
	}

	public void initDisplay() {
		for (int y = 5; y >= 0; y--) { // Inverted x and y order
			for (int x = 0; x <= 5; x++) {
				grid[x][y] = "empty";
				CustomPanel pnl = new CustomPanel(x, y, grid[x][y], "");
				mapPanel.add(pnl);

			}
		}
		mainFrame.pack();
		mainFrame.setVisible(true);
	}

	public void mapGUI() {
		updateGrid();
	}

	public void showCompletionMessage(int nov, int nom) {
		JOptionPane.showMessageDialog(mainFrame,
				nov + " victims have been taken to hospital. Mission complete in " + nom + " trips!");
	}

	public void updateGrid() {
		for (int y = 5; y >= 0; y--) { // Inverted x and y order
			for (int x = 0; x <= 5; x++) {
				CustomPanel pnl = getCell(x, y);
				pnl.setCellType(CustomPanel.locationTypes.valueOf(grid[x][y]));
				pnl.setRobotAtCell(false);
				pnl.setObjective(false);
				pnl.drawUpdate();
			}
		}
	}

	public CustomPanel getCell(int x, int y) {
		for (Component com : mapPanel.getComponents()) {
			if (com instanceof CustomPanel) {
				// It's a CustomPanel
				CustomPanel pnl = (CustomPanel) com;
				if (pnl.getXY()[0] == x && pnl.getXY()[1] == y)
					return pnl;
			}
		}
		return null;
	}

	public void appendStats(String act, String str, boolean isserver) {
		String result = str.replaceAll("\"", "");
		String action = act.replaceAll("\"", "");

		StyledDocument doc = txtServerCMD.getStyledDocument();

		SimpleAttributeSet txt = new SimpleAttributeSet();
		StyleConstants.setForeground(txt, Color.BLACK);
		StyleConstants.setBackground(txt, Color.WHITE);
		StyleConstants.setBold(txt, false);
		StyleConstants.setAlignment(txt, StyleConstants.ALIGN_LEFT);

		SimpleAttributeSet ac = new SimpleAttributeSet();
		StyleConstants.setForeground(ac, isserver ? Color.RED : Color.BLUE);
		StyleConstants.setBackground(ac, Color.WHITE);
		StyleConstants.setBold(ac, true);
		StyleConstants.setAlignment(ac, StyleConstants.ALIGN_LEFT);
		
		try {
			doc.insertString(doc.getLength(), action + ": ", ac);
			doc.insertString(doc.getLength(), result + "\n", txt);
			this.txtServerCMD.setCaretPosition(this.txtServerCMD.getDocument().getLength());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setLocationType(int x, int y, String locationType) {
		grid[x][y] = locationType;
	}

	public String getLocationType(int x, int y) {
		return grid[x][y];
	}

	// Sets the type of cell at grid[x][y]
	public void setType(int x, int y, String type) {
		grid[x][y] = type;
	}

	public void setLblCurrentPos(String str) {
		lblCurrentPos.setText("Current Tile Position: " + str);
	}

	public void setLblOrientation(String str) {
		lblOrientation.setText("Robot's Orientation: " + str);
	}

	public void setLblType(String str) {
		lblType.setText("Current Tile Type: " + str);
	}

	public void setLblCurrentCol(String str) {
		lblCurrentCol.setText("Current Tile Colour: " + str);
	}

	public void setLblVictim(String str) {
		lblRGB.setText("Victim at position: " + str);
	}

	public void setLblObstacle(String str) {
		lblObstacle.setText("Obstacles: " + str);
	}

	public void setLblVictims(String str) {
		lblVictims.setText("Victims Saved: " + str);
	}

	public void setLblTarget(int x, int y) {
		String str = "(" + x + ", " + y + ")";
		lblTarget.setText("Target Cell Position: " + str);
	}

}
