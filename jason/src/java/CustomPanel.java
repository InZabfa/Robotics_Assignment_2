import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class CustomPanel extends JPanel {
	private int x;
	private int y;
	public boolean robotAtLocation = false;
	private boolean isObjective = false;
	private locationTypes locationType;
	private String labelText = "";
	private Color cellColour;
	private Color borderColour = Color.BLACK;
	private JLabel lblCell;
	private String direction;

	private File up_img;
	private File down_img;
	private File left_img;
	private File right_img;

	public enum locationTypes {
		hospital, obstacle, victim, critical, nonCritical, savednoncritical, savedcritical, empty, objective
	}

	public CustomPanel(int x, int y, String locType) {
		super();
		this.x = x;
		this.y = y;
		this.locationType = locationTypes.valueOf(locType);
		this.lblCell = new JLabel("(" + x + ", " + y + ")");
		this.lblCell.setFont(new Font("Font", Font.PLAIN, 25));
		this.add(lblCell);
		getImages();
	}

	public CustomPanel(int x, int y, String locType, String labelText) {
		this(x, y, locType);
		this.labelText = labelText;
	}

	public void setDirection(String dir) {
		this.direction = dir;
	}

	public void getImages() {
		ClassLoader classLoader = getClass().getClassLoader();
		up_img = new File(classLoader.getResource("src/img/up.png").getFile());
		down_img = new File(classLoader.getResource("src/img/down.png").getFile());
		left_img = new File(classLoader.getResource("src/img/left.png").getFile());
		right_img = new File(classLoader.getResource("src/img/right.png").getFile());

	}

	/**
	 * int[0] = x int[1] = y
	 * 
	 * @return
	 */
	public int[] getXY() {
		return new int[] { this.x, this.y };
	}

	public void setRobotAtCell(boolean val) {
		this.robotAtLocation = val;
	}

	public void setObjective(boolean val) {
		this.isObjective = val;
	}

	public locationTypes getLocationType() {
		return this.locationType;
	}

	public void setLabelText(String txt) {
		this.labelText = txt;
	}

	public void setCellType(locationTypes locationType) {
		this.borderColour = Color.BLACK;
		this.lblCell.setVisible(true);
		switch (locationType) {
		case hospital:
			cellColour = new Color(255, 255, 0);
			this.lblCell.setForeground(Color.BLACK);
			this.lblCell.setText("H");
			this.borderColour = Color.BLACK;
			break;
		case obstacle:
			cellColour = Color.BLACK;
			this.lblCell.setVisible(false);
			this.lblCell.setForeground(Color.WHITE);
			break;
		case victim:
			cellColour = Color.LIGHT_GRAY;
			this.lblCell.setText("?");
			break;
		case critical:
			this.cellColour = new Color(128, 0, 32); // Burgundy
			this.lblCell.setForeground(Color.WHITE);
			break; // Burgundy
		case nonCritical:
			cellColour = new Color(0, 139, 139);
			this.lblCell.setForeground(Color.WHITE);
			break;
		case savednoncritical:
			this.borderColour = Color.GREEN;
			cellColour = new Color(0, 139, 139);
			this.lblCell.setText("NC");
			break;
		case savedcritical:
			this.cellColour = new Color(128, 0, 32); // Burgundy
			this.borderColour = Color.GREEN;
			this.lblCell.setForeground(Color.WHITE);
			this.lblCell.setText("C");
			break;
		default:
			cellColour = Color.WHITE;
			this.lblCell.setText("(" + x + "," + y + ")");
			break;
		}
		if (robotAtLocation & !isObjective) {
			borderColour = Color.BLUE;
		}

		if (isObjective & !robotAtLocation) {
			this.borderColour = Color.RED;
			switch (locationType) {
			case critical:
				this.lblCell.setForeground(Color.WHITE);
			case savedcritical:
				this.lblCell.setForeground(Color.WHITE);
			case obstacle:
				this.lblCell.setForeground(Color.WHITE);
			case savednoncritical:
				this.lblCell.setForeground(Color.WHITE);
			case nonCritical:
				this.lblCell.setForeground(Color.WHITE);
			default:
				this.lblCell.setForeground(Color.BLACK);
			}
			this.lblCell.setText("(" + x + "," + y + ")");
		}

		if (robotAtLocation & isObjective) {
			this.borderColour = new Color(128, 0, 128);
		}

		this.setBackground(cellColour);
		this.lblCell.setVisible(!robotAtLocation);
	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		if (robotAtLocation) {

			BufferedImage img;

			try {
				switch (this.direction) {
				case "n":
					img = ImageIO.read(up_img);
					break;
				case "e":
					img = ImageIO.read(right_img);
					break;
				case "s":
					img = ImageIO.read(down_img);
					break;
				case "w":
					img = ImageIO.read(left_img);
					break;
				default:
					img = ImageIO.read(up_img);
					break;
				}

				int x = (getWidth() - img.getHeight()) / 2;
				int y = (getHeight() - img.getWidth()) / 2;
				g.drawImage(img, x, y, null);
			} catch (Exception ex) {

			}
		}
	}

	public void drawUpdate() {
		this.setBackground(cellColour);
		if (robotAtLocation) {
			this.setBorder(BorderFactory.createLineBorder(borderColour, 6));
		} else
			this.setBorder(BorderFactory.createLineBorder(borderColour, 3));
	}
}
