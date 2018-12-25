Note that the JPA standard specifies that TABLE_PER_CLASS is optional, so not all JPA implementations may support it.

There is no relationship between the database tables, except for the fact that they have some (many) similar columns.

If parent class BillingDetails were abstract you wouldn't need an additional table to hold instances
If BillingDetails were concrete, you’d need an additional table to hold instances.

The advantages of this mapping strategy are clearer if we examine polymorphic queries.
For example, the following polymorphic query:
select bd from BillingDetails bd
generates the following SQL statement:

select
    ID, OWNER, EXPMONTH, EXPYEAR, CARDNUMBER, ACCOUNT, BANKNAME, SWIFT, CLAZZ_
from
    ( select
        ID, OWNER, EXPMONTH, EXPYEAR, CARDNUMBER,
        null as ACCOUNT,
        null as BANKNAME,
        null as SWIFT,
        1 as CLAZZ_
      from
        CREDITCARD
      union all
      select
        id, OWNER,
        null as EXPMONTH,
        null as EXPYEAR,
        null as CARDNUMBER,
        ACCOUNT, BANKNAME, SWIFT,
        2 as CLAZZ_
      from
          BANKACCOUNT
    ) as BILLINGDETAILS

A union requires that the queries that are combined project over the same columns.
Hence, you have to pad and fill nonexistent columns with NULL.

Another much more important advantage is the ability to handle polymorphic associations.
For example, an association mapping from User to BillingDetails would now be possible.


Negatives:

You have to define @Id only once inside parent class BillingDetails.
You cannot use @Id annotation inside child classes.
The database identifier and its mapping have to be present in the superclass,
to share it in all subclasses and their tables.

You cannot override inherited column names with @AttributeOverride.
The @AttributeOverride annotation is only specified to work for overriding
the attributes of mapped superclasses and fields of embedded classes.


You can't use GenerationType.IDENTITY on the BillingDetails.id field since in this
case polymorphic queries would return two entities for the same id.