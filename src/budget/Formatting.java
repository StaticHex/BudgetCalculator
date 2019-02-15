package budget;

enum Alignment { LEFT, CENTER, RIGHT };
enum PadMode { WIDTH, NUMBER };

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
	
	// Getters and setters past this point
	public void setAlignment(Alignment align)	{ this._align = align; }
	public void setMode(PadMode mode) 			{ this._mode = mode; }
	public void setPadChar(char padChar) 		{ this._padChar = padChar; }
}
