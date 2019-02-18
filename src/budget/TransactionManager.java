package budget;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

enum ArchiveMode { WEEKLY, BIWEEKLY, MONTHLY };

public class TransactionManager {
	// Private finals, right now just something for displaying our date formats
	private final SimpleDateFormat FORMAT = new SimpleDateFormat("MM-dd-yyyy");
	private final SimpleDateFormat SFORMAT = new SimpleDateFormat("MM/dd/yyyy");
	
	// It would honestly be more trouble than it's worth to create getters and setters for our transaction lists and so
	// instead we just expose these so we can access them directly from outside the class using the ArrayList classes
	// methods
	public ArrayList<Transaction> transactions;
	public ArrayList<Transaction> recurring;
	
	private boolean _round;
	private ArchiveMode _archiveMode;
	private String _currentArchive;
	
	public TransactionManager() {
		this.transactions = new ArrayList<Transaction>();
		this.recurring = new ArrayList<Transaction>();
		this._round = true;
		this._currentArchive = "01-01-1970";
		this._archiveMode = ArchiveMode.WEEKLY;
	}
	
	public void save() {
		File archiveDir = new File("./archives");
		if (!archiveDir.exists()) {
			archiveDir.mkdir();
		}
		try {
			Writer archiveOut = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("./archives/"+this._currentArchive), "UTF-8"));
			Writer settingsOut = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("./settings.txt"), "UTF-8"));
			Writer recurringOut = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("./recurring.txt"), "UTF-8"));
			
			// Generate settings file string, write to file, and then close settings file
			String settingString = this.getRoundAsString()+"|";
				   settingString+= this._currentArchive+"|";
				   settingString+= this.getArchiveModeAsString();
			settingsOut.write(settingString+"\n");
			settingsOut.close();
			
			// Write transactions to archive file
			Iterator<Transaction> it = this.transactions.iterator();
			while(it.hasNext()) {
				archiveOut.write(((Transaction) it.next()).export()+"\n");
			}
			archiveOut.close();
			
			// Write recurring transactions to archive file
			it = this.recurring.iterator();
			while(it.hasNext()) {
				recurringOut.write(((Transaction) it.next()).export()+"\n");
			}
			recurringOut.close();
			
		} catch (FileNotFoundException e) {
			// Print alert if we can't create the file
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Something went wrong!");
			alert.setHeaderText("File was not found and could not create it, check permissions.");
			alert.setContentText(e.getStackTrace().toString());
			alert.showAndWait().ifPresent(rs -> {
				System.exit(1);
			});
		} catch (UnsupportedEncodingException e) {
			// Print alert if we run into trouble parsing UTF-8
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Something went wrong!");
			alert.setHeaderText("Check to ensure unicode is supported on your system.");
			alert.setContentText(e.getStackTrace().toString());
			alert.showAndWait().ifPresent(rs -> {
				System.exit(1);
			});
		} catch (IOException e) {
			// Print alert if we run into trouble reading file
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Something went wrong!");
			alert.setHeaderText("An error occured while writing to file, check permissions and disk space and try again.");
			alert.setContentText(e.getStackTrace().toString());
			alert.showAndWait().ifPresent(rs -> {
				System.exit(1);
			});
		}
	
	}
	
	public void load() throws IOException {
		File archiveFile = new File("./archives/"+this._currentArchive);
		File recurringFile = new File("./recurring.txt");

		String line;
		// Read in and parse archive file
		if(archiveFile.exists()) {
			BufferedReader archiveIn = new BufferedReader(
					new InputStreamReader(new FileInputStream(archiveFile), "UTF-8")); 
			while((line = archiveIn.readLine()) != null) {
				if(line.contains("|")) {
					this.transactions.add(new Transaction(line));
				}
			}
			archiveIn.close();
		}
			
		// Read in and parse recurring file
		if(recurringFile.exists()) {
			BufferedReader recurringIn = new BufferedReader(
					new InputStreamReader(new FileInputStream(recurringFile), "UTF-8")); 
			while((line = recurringIn.readLine()) != null) {
				if(line.contains("|")) {
					this.recurring.add(new Transaction(line));
				}
			}
			recurringIn.close();
		}		
	}
	
	public void loadCurrent() throws IOException {
		File settingsFile = new File("./settings.txt");
		// Read in and parse settings file
		String line;
		if(settingsFile.exists()) {
			BufferedReader settingsIn = new BufferedReader(
					new InputStreamReader(new FileInputStream(settingsFile), "UTF-8"));
			while ((line = settingsIn.readLine()) != null) {
				if(line.contains("|")) {
					this._unwrap(line);
				}
			}
			settingsIn.close();
		}
		this.load();
	}
	
	public String getArchivePeriod() throws ParseException {
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
		this.setCurrentArchive(settings[1]);
		this.setArchiveMode(settings[2]);
	}
	
	// Getters and setters past this point
	public boolean 		getRound() 									{ return this._round; 						}
	public String 		getCurrentArchive() 						{ return this._currentArchive; 				}
	public ArchiveMode 	getArchiveMode() 							{ return this._archiveMode; 				}
	public void 		setRound(boolean round)			 			{ this._round = round; 						}
	public void 		setCurrentArchive(String currentArchive) 	{ this._currentArchive = currentArchive; 	}
	public void 		setArchiveMode(ArchiveMode archiveMode) 	{ this._archiveMode = archiveMode; 			}
	
	// Specialized getters and setters here
	public String getRoundAsString() {
		if (this._round) {
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
		if (round.equalsIgnoreCase("Y")) {
			this._round = true;
		} else {
			this._round = false;
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
}
