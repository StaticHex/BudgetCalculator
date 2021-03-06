## Simple Budget Tracker v1.0

### Description

This project implments a very simple budget tracker with the ability to create and maintain a list of transactions. The features are fairly simple and the program was meant to handle a single account vs. multiple accounts. Users looking for more advanced features or statistics should look at GNUCash or more advanced software.

### Features

* Ability to define recurring transactions for things such as bills, paychecks, etc.
* Ability to save and archive transaction logs.
* Ability to add, modify, and remove transactions.
* Ability to choose custom colors for each transaction.
* Ability to customize the program's color theme.

### Menu Options: 

* (+) -- Adds a new transaction to the list. Brings up the add transaction window (see add transactions section below for more information).
* 🗋 -- Creates a new transaction list. Archives the old list and then allows the user to set a start date for the new list. The end date is selected automatically based on the user's settings (see settings section below for more information).
* 📂 -- Loads a previously saved transaction list.
* 💾 -- Saves the current transaction list
* 🛠 -- Opens the settings menu (see settings section below for more information)

### Add transaction options

* Date - The date the transaction occurred.
* Type - Whether the transaction is a Withdrawal, a Deposit, or a Report. A Report will not impact the total and is used primarily for notes or to list out itemized transactions without them being counted twice.
* Description - A short description of the transaction, entries longer than 64 characters will be truncated (this amount may change in later versions)
* Color - The color of the transaction, useful for keeping track of bills, utilities and other recurring transactions.
* Recurring - (Y/N) Recurring transactions are transactions which are automatically added to any new list you create. Once again, the purpose of these transactions is to allow the user to specify things such as utilities, paystubs, etc.

### Settings options

* Rounding - Currently 4 types of rounding are supported:
  * None - Nothing is rounded, use this if you want to track exact amounts.
  * Simple - Round everything to the closest decimal place
  * Banker's - In the event that the amount to round is equidistant from two values i.e. 0.5 the value is rounded to the nearest even integer. See http://wiki.c2.com/?BankersRounding for more information.
  * Spare Change - Round withdrawals up to the nearest dollar, round deposits down to the nearest dollar, round Total remaining down to the nearest $5
* Show Rounding - Provides the option to round silently, meaning values are rounded when calculating the remaining total but exact amounts are shown on each transaction.
* Archive Mode - Currently 3 modes are supported and the time periods apply globally. Currently there is no way to make some transactions apply under one mode and have others apply on another (though this may change in the future).
  * Weekly - 7 day period based on the start date.
  * Bi-Weekly - 13 day period based on the start date.
  * Monthly - Finds the first and last day of the month that the chosen start date resides in and creates a period based on that.
* Color Options:
  * Button Text Color - Changes the text color on all buttons and title bars.
  * Button Background Color - Changes the background color on all buttons and title bars
  * Stage Background Color - Changes the backtround color of the main window; applies dilute coloring i.e. it averages the color with white before applying so that the color is lighter.
  * Default Transaction Color - This is the color that transactions will default to if no color is assigned to them. Applies dilue coloring i.e. it averages the color with white before applying so that the color is lighter.
 
