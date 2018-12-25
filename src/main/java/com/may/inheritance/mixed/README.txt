The @SecondaryTable and @Column annotations group some properties and tell Hibernate to
get them from a secondary table. You map all properties that you moved into the secondary table
with the name of that secondary table. This is done with the table parameter of @Column.

In this example, it separates the CreditCard properties from the single table strategy into the CREDITCARD table.

Remember that InheritanceType.SINGLE_TABLE enforces all columns of subclasses to be nullable.
One of the benefits of this mapping is that you can now declare columns of the CREDITCARD table as NOT NULL,
guaranteeing data integrity.

At runtime, Hibernate executes an outer join to fetch BillingDetails and all subclass instances polymorphically:
select
    ID, OWNER, ACCOUNT, BANKNAME, SWIFT,
    EXPMONTH, EXPYEAR, CARDNUMBER,
    BD_TYPE
from
    BILLINGDETAILS
    left outer join CREDITCARD on ID=CREDITCARD_ID

You can also use this trick for other subclasses in your class hierarchy.
If you have an exceptionally wide class hierarchy, the outer join can become a problem.

Some database systems (Oracle, for example) limit the number of tables in an outer join operation.
For a wide hierarchy, you may want to switch to a different fetching strategy that executes an
immediate second SQL select instead of an outer join.