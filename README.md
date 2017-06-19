Project for school.<br />

Should be a working register.<br />
Made for Alfa College Groningen Boumaboulevard, Class B-ITA4-2a.<br />
Project group consisted of [1000monkeys](https://github.com/1000monkeys), [Danique646](https://github.com/Danique646) and [Xciel](https://github.com/Xciel).<br />


## **Changelog**<br />

**V0.0**<br />
Initial commit<br />

**V0.1**<br />
Added uploading temporary prices to database.(DBI)<br />
Added ability to reload data for scene on resetting the scene as the current scene.(DBI)<br />
Added checks for inserting temporary price so you don't have 2 temporary prices at the same time.(DBI)<br />

**V0.2**<br />
Added deleting a temporary prices with the needed checks so that if the price is active you just set the end date to now.(DBI)<br />
Added alert message to inserting a new item.(DBI)<br />

**V0.2.1**<br />
Changed database so that there is a DefaultPrice table instead of having a boolean on the Prices table.(BOTH)<br />
Fixed float rounding errors.(DBI)<br />
Included database in release.(BOTH)<br />


**V0.3**<br />
Added categorie's to the database and retooled the existing code to work with it.(BOTH)<br />

**V0.3.1**<br />
Added DBCP for faster database connections.(BOTH)<br />
Small bug fixes.(DBI)<br />
We now use a mysql database located on a server.(BOTH)<br />

**V0.3.2**<br />
Found a way to make sure a multiline update fails or not so implemented it.(DBI)<br />
We now check before inserting a categorie for if it exists.(DBI)<br />

**V0.3.3**<br />
Retooled some things so some things extend some things now so that code is reused more.(BOTH)<br />
Basically i retooled my scene changing implementation so that i can use it for both the Database Inserter and the register.(BOTH)<br />

**V0.3.4**<br />
Changed the time regex functions to HH:MM:SS.(DBI)<br />
Made the delete item button do something.(DBI)<br />
Made the delete categorie button do something.(DBI)<br />
Implemented "Are you sure?" question on deleting a temporary price and included it in the new delete methods.(DBI)<br />

**V0.4**<br />
Changed update item to include the categorie.(DBI)<br />
Completed implementing editing a categorie/customer card.(DBI)<br />
Added removing customer card.(DBI)<br />

**V0.4.1**<br />
Started progressing on the register part of the register.(REG)<br />
Changed UI of register.(REG)<br />
Added the categorie overview.(REG)<br />
Added part of creating a receipt.(REG)<br />

**V0.5**<br />
Added selecting customer card.(REG)<br />
Added paying and receipts.(REG)<br />
Done?

## **Plans**<br />
Add to the register ability to have customer cards and customer card discounts.<br />
And build the normal things needed for a register, For example 'scanning' an item, that being added to the list of items being bought etc.<br />


## **TODO**<br />
Lots.<br />
Bugfixes.<br />
Bugfix getScene()<br />
Toevoegen zien welke klantenkaart geselecteerd.<br />
Selectie toevoegen aan bonnen lijst.<br />
Correctie uitwerken.

