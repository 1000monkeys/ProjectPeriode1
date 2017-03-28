Project for school.<br />

Should be a working register.<br />
Made for Alfa College Groningen Boumaboulevard, Class B-ITA4-2a.<br />
Project group consisted of [1000monkeys](https://github.com/1000monkeys), [Danique646](https://github.com/Danique646) and [Xciel](https://github.com/Xciel).<br />


## **Changelog**<br />

**V0.0**<br />
Initial commit<br />

**V0.1**<br />
Added uploading temporary prices to database in the database inserter.<br />
Added ability to reload data for scene on resetting the scene as the current scene for database inserter.<br />
Added checks for inserting temporary price so you don't have 2 temporary prices at the same time.<br />

**V0.2**<br />
Added deleting a temporary prices with the needed checks so that if the price is active you just set the end date to now.<br />
Added alert message to inserting a new item.<br />

**V0.2.1**<br />
Changed database so that there is a DefaultPrice table instead of having a boolean on the Prices table.<br />
Fixed float rounding errors.<br />
Included database in release.<br />


**V0.3**<br />
Added categorie's to the database and retooled the database inserter to work with them.


## **Plans**<br />
Add to the register ability to have customer cards and customer card discounts.<br />
And build the normal things needed for a register, For example 'scanning' an item, that being added to the list of items being bought etc.<br />


## **TODO**<br />
Loads

