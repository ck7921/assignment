# Charlene's Coffee Shop - Working Assignment

2 Solutions have been implemented for this assignment:
* a complete solution that can be extended and configured as the business grows and requirements change 
* a basic dirty solution that doesn't perform a lot of input validation 

The complete solution can be found in:
assignment.AppMain

The architecture of the complete solution is explained in Java Api Doc in the class:
assignment.AppMain
with further links to its services. Use it as a starting point for reading the solution.
A package-by-feature approach was taken for structuring the app.

The complete solution (in the package assignment) uses CSV files to
read product and product configuration data.

Both solutions support free extras, in case a beverage and a snack is purchased and also
the loyalty card feature to count consumed products and offer a free beverage 
in case 4 stamps (or more) have been collected.

(The dirty-quick-hack solution can be found in: alternative.DirtySolution)

This application accepts a list of products and a loyalty card (as a file) as input.
The input has to be provided via command line arguments.

### Examples

Hint: use an IDE e.g. IntelliJ IDEA for easy execution

**Display command line help by starting without arguments:**

Windows:<br/>
java -classpath target\classes assignment.AppMain<br/>
Linux:<br/>
java -classpath target/classes assignment.AppMain<br/>

**Order two small coffee's:**

Windows:<br/>
java -classpath target\classes assignment.AppMain coffee_small=2<br/>
Linux:<br/>
java -classpath target/classes assignment.AppMain coffee_small=2<br/>

**Order one small and one medium coffee:**

Windows:<br/>
java -classpath target\classes assignment.AppMain coffee_small=1,coffee_medium=1<br/>
Linux:<br/>
java -classpath target/classes assignment.AppMain coffee_small=1,coffee_medium=1<br/>

The list of products is a comma separated list of (key=value)-pairs.
The key is the product configuration and value is the quantity of products.  

**Order one small coffee and present a loyalty card:**

Windows:<br/>
java -classpath target\classes assignment.AppMain coffee_small=1 loyaltyCard.properties<br/>
Linux:<br/>
java -classpath target/classes assignment.AppMain coffee_small=1 loyaltyCard.properties<br/>

Hint: to add loyalty points create an empty file and add:
coffee=2
for having already 2 stamps on the card. The card format permits
counting of purchases for each item class/type.

**Order one small coffee with extra milk:**

Windows:<br/>
java -classpath target\classes assignment.AppMain coffee_small/milk_extra=1<br/>
Linux:<br/>
java -classpath target/classes assignment.AppMain coffee_small/milk_extra=1

**Order one small coffee with extra milk and special roast:**

Windows:<br/>
java -classpath target\classes assignment.AppMain coffee_small/milk_extra/special_roast=1<br/>
Linux:<br/>
java -classpath target/classes assignment.AppMain coffee_small/milk_extra/special_roast=1<br/>

#### Alternative Dirty solution

**The dirty solution can be started with:**

Windows:<br/>
java -classpath target\classes alternative.DirtySolution coffee_small=1 4<br/>
Linux:<br/>
java -classpath target/classes alternative.DirtySolution coffee_small=1 4<br/>

it expects at least one argument. The first argument is identical 
with the complete solution. The second argument is the count of
already collected Loyalty Points.

## Assumptions for complete solution

* Maven was used for dependency management
* VAT and taxes have been ignored 
* Ordering a product with extras only permits one extra per item class.
  This means a coffee can be ordered either with Extra milk or Foamed milk but not both.
  This works for this assignment but would not work e.g. for Pizza products
  where different kind of cheese could be added.
* The currency has been hard-coded to be swiss francs (CHF)
* If the CSV file contains multiple product definitions for the same product key
  the behaviour is not defined (normally would be prevented by database key constraint)
* If a customer has "free beverage" it only gets applied to beverages without any discounts.
  Meaning a coffee with free Extra milk can't be free.
* A package-per-feature apporach was choosen thus some classes are package private
* Builders (for easy fluent coding) haven't been implemented for all DTOs.
  (due to time isses)
* Collected loyalty stamps are effective with the next visit. A customer 
  has to come again/perform another purchase to exchange his points for bree beverages.
* If a customer has many loyalty points (multples of 4) several free beverages
  can be obtained with a single purchase. (if possible)
* A customer do get Loyatly points for every beverage and can get a free beverage
  of any other type (small, medium). In case multiple beverages are candidates
  to be free, the behaviour is not defined.
  