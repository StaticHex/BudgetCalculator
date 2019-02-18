package budget;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.awt.Color;

enum TransactionType { WITHDRAWAL, DEPOSIT, REPORT };

public class Transaction {
	private final SimpleDateFormat FORMAT = new SimpleDateFormat("MM/dd/yyyy");
	private final SimpleDateFormat FILE_FORMAT = new SimpleDateFormat("MM-dd-yyyy");
	private final int len = 80;
	private Date _date;
	private String _name;
	private TransactionType _type;
	private double _amount;
	private boolean _recurring;
	private Formatting _formatter;
	private Color _transactionColor;
	
	public Transaction() {
		this._date = new Date(0);
		this._name = this._formatter.pad("Empty Transaction", len);
		this._type = TransactionType.REPORT;
		this._amount = 0.0;
		this._recurring = false;
		this._formatter = new Formatting();
		this._transactionColor = this._formatter.lightenColor(SystemColor.GREY,3);
	}
	
	public Transaction(Transaction other) {
		this._date = other._date;
		this._name = other._name;
		this._type = other._type;
		this._amount = other._amount;
		this._recurring = other._recurring;
		this._formatter = other._formatter;
		this._transactionColor = other._transactionColor;
	}
	
	public Transaction(String transaction) {
		Transaction other = this.unwrap(transaction);
		this._date = other._date;
		this._name = other._name;
		this._type = other._type;
		this._amount = other._amount;
		this._recurring = other._recurring;
		this._formatter = other._formatter;
		this._transactionColor = other._transactionColor;
	}
	
	public Transaction(long date, String name, TransactionType type, double amount, boolean recurring, Color transactionColor) {
		this._date = new Date(date);
		this._name = this._formatter.pad(this._formatter.truncate(name, len), len);
		this._type = type;
		this._amount = amount;
		this._recurring = recurring;
		this._formatter = new Formatting();
		this._formatter = new Formatting();
		this._transactionColor = transactionColor;
	}
	
	public Transaction(String date, String name, TransactionType type, double amount, boolean recurring, Color transactionColor) {
		try {
			this._date = FORMAT.parse(date);
		} catch (ParseException e) {
			try {
				this._date = FILE_FORMAT.parse(date);
			} catch (ParseException e2) {
				// TODO Auto-generated catch block
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Something went wrong!");
				alert.setHeaderText("An error occured during transaction file processing");
				alert.setContentText("Could not parse date into date object, check dates in archive files for errors");
				alert.showAndWait().ifPresent(rs -> {
					System.exit(1);
				});
				e2.printStackTrace();
			}
		}
		this._name = this._formatter.pad(this._formatter.truncate(name, len), len);
		this._type = type;
		this._amount = amount;
		this._recurring = recurring;
		this._formatter = new Formatting();
		this._formatter = new Formatting();
		this._transactionColor = transactionColor;
	}
	
	public String export() {
		String exportString = this.getDate() + "|";
			   exportString+= this._name + "|";
			   exportString+= this.getTypeAsString() + "|";
			   exportString+= this.getAmountAsString() + "|";
			   exportString+= this.getRecurringAsString() + "|";
			   exportString+= this.getTransactionColorAsString();
		return exportString;
	}
	
	public Transaction unwrap(String transaction) {
		String[] properties = this._formatter.splitString(transaction, '|');
		return new Transaction(properties[0],
							   properties[1],
							   this._getTypeFromString(properties[2]),
							   Double.parseDouble(properties[3]),
							   this._getRecurringFromString(properties[4]),
							   this._formatter.getColorFromString(properties[5]));
	}
	
	public String toString() {
		String transaction = this._formatter.pad(this.getDate(), 14);
			   transaction+= this._formatter.pad(this._formatter.truncate(this._name, 39), 39);
			   transaction+= this._formatter.pad(this.getTypeAsString(), 3);
			   transaction+= this._formatter.pad(this.getAmountAsString(), 10);
			   transaction+= this._formatter.pad(this.getRecurringAsString(), 3);
			   transaction+= this._formatter.pad(this.getTransactionColorAsString(), 11);
		return transaction;
	}
	
	private TransactionType _getTypeFromString(String type) {
		if (type.equalsIgnoreCase("W")) {
			return TransactionType.WITHDRAWAL;
		} else if (type.equalsIgnoreCase("D")) {
			return TransactionType.DEPOSIT;
		} else {
			return TransactionType.REPORT;
		}
	}
	
	private boolean _getRecurringFromString(String recurring) {
		if (recurring.equalsIgnoreCase("Y")) {
			return true;
		} else {
			return false;
		}
	}
	
	// Getters and setters past this point
	public String 			getDate() 										{ return FORMAT.format(this._date); 			}
	public String 			getName() 										{ return this._name; 							}
	public TransactionType 	getType() 										{ return this._type; 							}
	public double 			getAmount() 									{ return this._amount; 							}
	public Color			getTransactionColor()							{ return this._transactionColor;				}
	public String			getAmountAsString()								{ return String.format("%.2f",this._amount); 	}
	public boolean 			getRecurring() 									{ return this._recurring;						}
	public void 			setDate(long date) 								{ this._date = new Date(date); 					}
	public void 			setName(String name) 							{ this._name = name; 							}
	public void 			setType(TransactionType type) 					{ this._type = type; 							}
	public void 			setAmount(double amount) 						{ this._amount = amount; 						}
	public void 			setRecurring(boolean recurring) 				{ this._recurring = recurring; 					}
	public void				setTransactionColor(Color transactionColor) 	{ this._transactionColor = transactionColor; 	}
	
	// Specialized Getters and Setters
	public String getTypeAsString() { 
		switch(this._type) { 
		case WITHDRAWAL:
			return "W";
		case DEPOSIT:
			return "D";
		case REPORT:
			return "R";
		default:
			return "X";
		} 
	}
	
	public String getRecurringAsString() {
		if (this._recurring) {
			return "Y";
		} else {
			return "N";
		}
	}
	
	public String getTransactionColorAsString() {
		return this._formatter.getColorAsString(this._transactionColor);
	}
	
	public void setType(String type) {
		this._type = this._getTypeFromString(type);
	}
	
	public void setRecurring(String recurring) {
		this._recurring = this._getRecurringFromString(recurring);
	}
	
	public void setTransactionColor(String transactionColor) {
		this._transactionColor = this._formatter.getColorFromString(transactionColor);
	}
}
