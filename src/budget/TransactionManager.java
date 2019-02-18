package budget;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.awt.Color;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

enum ArchiveMode { WEEKLY, BIWEEKLY, MONTHLY };
enum RoundingMode { NONE, SIMPLE, BANKERS, SPARE_CHANGE };

public class TransactionManager {
	// Private finals, right now just something for displaying our date formats
	private final SimpleDateFormat 	FORMAT 		= new SimpleDateFormat("MM-dd-yyyy");
	private final SimpleDateFormat 	SFORMAT 	= new SimpleDateFormat("MM/dd/yyyy");
	private final Formatting 		_formatter 	= new Formatting();
	
	// It would honestly be more trouble than it's worth to create getters and setters for our transaction lists and so
	// instead we just expose these so we can access them directly from outside the class using the ArrayList classes
	// methods
	public 	ArrayList<Transaction> 	transactions;
	public 	ArrayList<Transaction> 	recurring;
	
	// Settings which are written out to file
	private RoundingMode			_round;						// Used to hold rounding settings
	private boolean					_showRounding;				// Round silently or show the effects of the rounding in the table
	private ArchiveMode 			_archiveMode;				// Used to hold the period to show for the archive mode
	private String 					_currentArchive;			// Used to hold the start date of the current archive
	private Color					_buttonTextColor;			// Used to hold the color used for button and header text
	private Color					_buttonBackgroundColor;		// Used to hold the color used for button and header backgrounds
	private Color					_stageBackgroundColor;  	// Used to hold the color for the stage (diluted color)
	private Color					_defaultTransactionColor;	// Used to hold the default color placed on transactions when created
	public TransactionManager() {
		this.transactions = new ArrayList<Transaction>();
		this.recurring = new ArrayList<Transaction>();
		this._round = RoundingMode.SIMPLE;
		this._showRounding = false;
		this._archiveMode = ArchiveMode.WEEKLY;
		this._currentArchive = "01-01-1970";
		this._buttonTextColor = _formatter.getSystemColor(SystemColor.WHITE);
		this._buttonBackgroundColor = this._formatter.getSystemColor(SystemColor.DARK_BLUE);
		this._stageBackgroundColor = this._formatter.getDiluteColor(SystemColor.GREY);
		this._defaultTransactionColor = this._formatter.getDiluteColor(SystemColor.GREY);
	}
	
	public void save() throws IOException {
		// Data write used to write out settings object to a file
		Writer currentOut = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream("./current.txt"), "UTF-8"));
			
		// Generate settings file string, write to file, and then close settings file
		String settingString = this.getRoundAsString()+"|";
			   settingString+= this.getShowRoundingAsString()+"|";
			   settingString+= this.getArchiveModeAsString()+"|";
			   settingString+= this._currentArchive+"|";
			   settingString+= this._formatter.getColorAsString(this._buttonTextColor)+"|";
			   settingString+= this._formatter.getColorAsString(this._buttonBackgroundColor)+"|";
			   settingString+= this._formatter.getColorAsString(this._stageBackgroundColor)+"|";
			   settingString+= this._formatter.getColorAsString(this._defaultTransactionColor);
			   
		currentOut.write(settingString+"\n");

		// Create an iterator to loop through our transaction lists and write them to file
		Iterator<Transaction> it = this.transactions.iterator();
			
		// Write recurring transactions to archive file
		it = this.recurring.iterator();
		while(it.hasNext()) {
			currentOut.write(((Transaction) it.next()).export()+"\n");
		}
			
		// Write transactions to archive file
		while(it.hasNext()) {
			currentOut.write(((Transaction) it.next()).export()+"\n");
		}
		currentOut.close();
	}
	
	public void load() throws IOException {
		File 	currentFile = new File("./current.txt"); // A file used to hold the current transaction list to display
		String 	line		= ""; // A temporary string used to read in currentFile line by line
		
		// Clear out old transaction files before we load in
		this.transactions = new ArrayList<Transaction>();
		this.recurring = new ArrayList<Transaction>();
		
		// Read in and parse current transaction file
		if(currentFile.exists()) {
			BufferedReader currentIn = new BufferedReader(
					new InputStreamReader(new FileInputStream(currentFile), "UTF-8"));
			
			// Read in settings from file
			line = currentIn.readLine();
			this._unwrap(line);
			
			// Read in transactions (both recurring and non-recurring)
			while((line = currentIn.readLine()) != null) {
				if(line.contains("|")) {
					Transaction next = new Transaction(line);
					if (next.getRecurring()) {
						this.recurring.add(next);
					} else {
						this.transactions.add(next);
					}
				}
			}
			
			// Close the file after we're done
			currentIn.close();
		}	
	}
	
	public void archive() throws IOException {
		// Save before we archive
		this.save();
		
		// Check to make sure we have an archive directory before the move
		File archiveDir = new File("./archives");
		if (!archiveDir.exists()) {
			archiveDir.mkdir();
		}
		
		// Move our current file into an archive file
		Path source = Paths.get("./current.txt");
		Path target = Paths.get("./archives/"+this._currentArchive);
		Files.move(source, target, REPLACE_EXISTING);
	}
	
	public void unarchive(String archiveName) throws IOException {
		// Archive current list before pulling old list
		this.archive();
		
		// Move our current file into an archive file
		Path source = Paths.get("./archives/"+archiveName);
		Path target = Paths.get("./current.txt");
		Files.move(source, target, REPLACE_EXISTING);
		
		// Load in new file after swapping
		this.load();
	}
	
	public String getPeriod() throws ParseException {
		Date startDate = FORMAT.parse(this._currentArchive);
		Calendar dateAdder = Calendar.getInstance();
		dateAdder.setTime(startDate);
		String periodString = SFORMAT.format(startDate)+"-";
		switch(this._archiveMode) {
		case MONTHLY:
			int day = 0;
			String[] dateComponents;
			while(day != 1) {
				dateAdder.add(Calendar.DATE, 1);
				dateComponents = SFORMAT.format(dateAdder.getTime()).split("/");
				day = Integer.parseInt(dateComponents[1]);
			}
			return periodString+SFORMAT.format(dateAdder.getTime());
		case BIWEEKLY:
			dateAdder.add(Calendar.DATE, 13);
			return periodString+SFORMAT.format(dateAdder.getTime());
		case WEEKLY:
		default:
			dateAdder.add(Calendar.DATE, 6);
			return periodString+SFORMAT.format(dateAdder.getTime());
		}
	}
	
	private void _unwrap(String settingString) {
		String[] settings = settingString.split("|");
		this.setRound(settings[0]);
		this.setShowRounding(settings[1]);
		this.setArchiveMode(settings[2]);
		this.setCurrentArchive(settings[3]);
		this.setButtonTextColor(settings[4]);
		this.setButtonBackgroundColor(settings[5]);
		this.setStageBackgroundColor(settings[6]);
		this.setDefaultTransactionColor(settings[6]);
	}
	
	// Getters and setters past this point
	public RoundingMode	getRound() 													{ return this._round; 										}
	public boolean		getShowRounding()											{ return this._showRounding;								}
	public ArchiveMode 	getArchiveMode() 											{ return this._archiveMode; 								}
	public String 		getCurrentArchive() 										{ return this._currentArchive; 								}
	public Color		getButtonTextColor()										{ return this._buttonTextColor;								}
	public Color		getButtonBackgroundColor()									{ return this._buttonBackgroundColor;						}
	public Color		getStageBackgroundColor()									{ return this._stageBackgroundColor; 						}
	public Color		getDefaultTransactionColor()								{ return this._defaultTransactionColor;						}
	public void 		setRound(RoundingMode round)			 					{ this._round = round; 										}
	public void			setShowRounding(boolean showRounding)						{ this._showRounding = showRounding;						}
	public void 		setArchiveMode(ArchiveMode archiveMode) 					{ this._archiveMode = archiveMode; 							}
	public void 		setCurrentArchive(String currentArchive)			 		{ this._currentArchive = currentArchive; 					}
	public void			setButtonTextColor(Color buttonTextColor)					{ this._buttonTextColor = buttonTextColor;  				}
	public void			setButtonBackgroundColor(Color buttonBackgroundColor)		{ this._buttonBackgroundColor = buttonBackgroundColor; 		}
	public void 		setStageBackgroundColor(Color stageBackgroundColor)			{ this._stageBackgroundColor = stageBackgroundColor; 		}
	public void			setDefaultTransactionColor(Color defaultTransactionColor)	{ this._defaultTransactionColor = defaultTransactionColor;	}
	
	// Specialized getters and setters here
	public String getRoundAsString() {
		switch(this._round) {
		case SIMPLE:
			return "S";
		case BANKERS:
			return "B";
		case SPARE_CHANGE:
			return "$";
		case NONE:
		default:
			return "X";
		}
	}
	
	public String getShowRoundingAsString() {
		if (this._showRounding) {
			return "Y";
		} else {
			return "N";
		}
	}
	
	public String getArchiveModeAsString() {
		switch(this._archiveMode) {
		case WEEKLY:
			return "W";
		case BIWEEKLY:
			return "B";
		case MONTHLY:
			return "M";
		default:
			return "?";
		}
	}
	
	public void setRound(String round) {
		if(round.equalsIgnoreCase("B")) {
			this._round = RoundingMode.BANKERS;
		} else if(round.equalsIgnoreCase("S")) {
			this._round = RoundingMode.SIMPLE;
		} else if(round.equalsIgnoreCase("$")) {
			this._round = RoundingMode.SPARE_CHANGE;
		} else {
			this._round = RoundingMode.NONE;
		}
	}
	
	public void setShowRounding(String showRounding) {
		if (showRounding.equalsIgnoreCase("Y")) {
			this._showRounding = true;
		} else {
			this._showRounding = false;
		}
	}
	
	public void setArchiveMode(String archiveMode) {
		if(archiveMode.equalsIgnoreCase("B")) {
			this._archiveMode = ArchiveMode.BIWEEKLY;
		} else if(archiveMode.equalsIgnoreCase("M")) {
			this._archiveMode = ArchiveMode.MONTHLY;
		} else {
			this._archiveMode = ArchiveMode.WEEKLY;
		}
	}
	
	public void	setButtonTextColor(String buttonTextColor) {
		this._buttonTextColor = this._formatter.getColorFromString(buttonTextColor);
	}
	
	public void setButtonBackgroundColor(String buttonBackgroundColor) {
		this._buttonBackgroundColor = this._formatter.getColorFromString(buttonBackgroundColor);
	}
	
	public void setStageBackgroundColor(String stageBackgroundColor) {
		this._stageBackgroundColor = this._formatter.getColorFromString(stageBackgroundColor);
	}
	
	public void setDefaultTransactionColor(String defaultTransactionColor) {
		this._defaultTransactionColor = this._formatter.getColorFromString(defaultTransactionColor);
	}
}
