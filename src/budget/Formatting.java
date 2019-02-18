package budget;

import java.awt.Color;

enum Alignment { LEFT, CENTER, RIGHT };
enum PadMode { WIDTH, NUMBER };
enum SystemColor {RED, YELLOW, GREEN, BLUE, 
				   PURPLE, ORANGE, CYAN, GREY, 
				   WHITE, DARK_RED, DARK_YELLOW, DARK_GREEN, 
				   DARK_BLUE, DARK_PURPLE, DARK_ORANGE, DARK_CYAN, 
				   DARK_GREY, BLACK, PINK, INDIGO};

public class Formatting {
	private char _padChar;
	private PadMode _mode;
	private Alignment _align;
	public Formatting() {
		this._padChar = ' ';
		this._mode = PadMode.WIDTH;
		this._align = Alignment.LEFT;
	}
	
	public Formatting(char padChar, PadMode mode) {
		this._padChar = padChar;
		this._mode = mode;
	}
	
	public String pad(String strToPad, int width) {
		switch(this._mode){
		case NUMBER:
			for(int i = 0; i < width; i++) {
				switch(this._align) {
				case LEFT:
					strToPad += this._padChar;
					break;
				case CENTER:
					strToPad = this._padChar+strToPad+this._padChar;
					break;
				case RIGHT:
					strToPad = this._padChar+strToPad;
					break;
				}
			}
			break;
		case WIDTH:
			while(strToPad.length() < width) {
				switch(this._align) {
				case LEFT:
					strToPad += this._padChar;
					break;
				case CENTER:
					if (strToPad.length() % 2 == 0) {
						strToPad += this._padChar;
					} else {
						strToPad = this._padChar+strToPad;
					}
					break;
				case RIGHT:
					strToPad = this._padChar+strToPad;
					break;
				}
			}
			break;
		}
		return strToPad;
	}
	
	public String truncate(String strToTrunc, int width) {
		String truncatedString = "";
		for (int i = 0; i < (width - 3); i++) { 
			truncatedString += strToTrunc.charAt(i);
		}
		return truncatedString + "...";
	}
	
	public Color getSystemColor(SystemColor color) {
		switch(color) {
		case WHITE:
			return new Color(255,255,255);
		case GREY:
			return new Color(192, 192, 192);
		case RED:
			return new Color(255, 0, 0);
		case YELLOW:
			return new Color(255, 255, 0);
		case GREEN:
			return new Color(0, 255, 0);
		case CYAN:
			return new Color(0, 255, 255);
		case BLUE:
			return new Color(0, 0, 255);
		case PURPLE:
			return new Color(255, 0, 255);
		case ORANGE:
			return new Color(255, 128, 64);
		case BLACK:
			return new Color(0, 0, 0);
		case DARK_GREY:
			return new Color(128, 128, 128);
		case DARK_RED:
			return new Color(128, 0, 0);
		case DARK_YELLOW:
			return new Color(128, 128, 0);
		case DARK_GREEN:
			return new Color(0, 128, 0);
		case DARK_CYAN:
			return new Color(0, 128, 128);
		case DARK_BLUE:
			return new Color(0, 0, 128);
		case DARK_PURPLE:
			return new Color(128, 0, 128);
		case DARK_ORANGE:
			return new Color(128, 64, 0);
		case PINK:
			return new Color(255, 0, 128);
		case INDIGO:
			return new Color(128, 0, 255);
		default:
			return new Color(Color.TRANSLUCENT);
		}
	}
	
	public Color getDiluteColor(SystemColor color) {
		Color diffuse = this.getSystemColor(color);
		return new Color((diffuse.getRed() + 255)/2, (diffuse.getGreen() + 255)/2, (diffuse.getBlue() + 255)/2);
	}
	
	public Color getColorFromString(String color) {
		String[] components = color.split(",");
		return new Color(Integer.parseInt(components[0]),
						 Integer.parseInt(components[1]),
						 Integer.parseInt(components[2]));
	}
	
	public String getColorAsString(Color col) {
		return Integer.toString(col.getRed())+","+
				Integer.toString(col.getGreen())+","+
				Integer.toString(col.getBlue());
	}
	
	// Getters and setters past this point
	public void setAlignment(Alignment align)	{ this._align = align; }
	public void setMode(PadMode mode) 			{ this._mode = mode; }
	public void setPadChar(char padChar) 		{ this._padChar = padChar; }
}
