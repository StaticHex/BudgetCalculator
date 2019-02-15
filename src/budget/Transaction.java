package budget;
import java.text.SimpleDateFormat;
import java.util.Date;

enum TransactionType { WITHDRAWAL, DEPOSIT, REPORT };

public class Transaction {
	private final SimpleDateFormat FORMAT = new SimpleDateFormat("MM/dd/yyyy");
	private Date _date;
	private String _name;
	private TransactionType _type;
	private double _amount;
	private boolean _recurring;
	private Formatting _settings;
	
	public Transaction() {
		this._date = new Date(0);
		this._name = "Empty Transaction";
		this._type = TransactionType.REPORT;
		this._amount = 0.0;
		this._recurring = false;
		this._settings = new Formatting();
	}
	
	public Transaction(long date, String name, TransactionType type, double amount, boolean recurring) {
		this._date = new Date(date);
		this._name = name;
		this._type = type;
		this._amount = amount;
		this._recurring = recurring;
		this._settings = new Formatting();
		this._settings = new Formatting();
	}
	
	public String toString() {
		String transaction = this._settings.pad(this.getDate(), 14);
			   transaction+= this._settings.pad(this._name, 50);
			   transaction+= this._settings.pad(this.getTypeAsString(), 3);
			   transaction+= this._settings.pad(this.getAmountAsString(), 10);
			   transaction+= this._settings.pad(this.getRecurringAsString(), 3);
		return transaction;
	}
	
	// Getters and setters past this point
	public String 			getDate() 						{ return FORMAT.format(this._date); }
	public String 			getName() 						{ return this._name; }
	public TransactionType 	getType() 						{ return this._type; }
	public double 			getAmount() 					{ return this._amount; }
	public String			getAmountAsString()				{ return String.format("%.2f",this._amount); }
	public boolean 			getRecurring() 					{ return this._recurring; }
	public String  			getTypeAsString()               { 
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
	public String           getRecurringAsString()          {
																if (this._recurring) {
																	return "Y";
																} else {
																	return "N";
																}
															}
	public void 			setDate(long date) 				{ this._date = new Date(date); }
	public void 			setName(String name) 			{ this._name = name; }
	public void 			setType(TransactionType type) 	{ this._type = type; }
	public void 			setAmount(double amount) 		{ this._amount = amount; }
	public void 			setRecurring(boolean recurring) { this._recurring = recurring; }
	
}
