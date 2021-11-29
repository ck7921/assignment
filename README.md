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

**Order two small coffee's:**

java ./target/classes/assignment.AppMain coffee_small=2

**Order one small and one medium coffee:**

java ./target/classes/assignment.AppMain coffee_small=1,coffee_medium=1

The list of products is a comma separated list of (key=value)-pairs.
The key is the product configuration and value is the quantity of products.  

**Order one small coffee and present a loyalty card:**
java ./target/classes/assignment.AppMain coffee_small=1 loyaltyCard.properties

Hint: to add loyalty points create an empty file and add:
coffee=2
for having already 2 stamps on the card. The card format permits
counting of purchases for each item class/type.

**Order one small coffee with extra milk:**

java ./target/classes/assignment.AppMain coffee_small/milk_extra=1

**Order one small coffee with extra milk and special roast:**

java ./target/classes/assignment.AppMain coffee_small/milk_extra/special_roast=1

#### Alternative Dirty solution

**The dirty solution can be started with:**
java ./target/classes/alternative/DirtySolution coffee_small=1 4

it expects at least one argument. The first argument is identical 
with the complete solution. The second argument is the count of
already collected Loyalty Points.
