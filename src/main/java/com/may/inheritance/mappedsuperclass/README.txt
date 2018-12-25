By default, properties of the superclass are ignored and not persisted!
You have to annotate the superclass with @MappedSuperclass to enable persisting of its properties in the concrete subclass tables.

The main problems:

You can’t have another entity referencing BillinDetails entity — there is no such table.
BillingDetails can not be @MappedSuperclass when it's a target class in associations, this means when some entities
have properties with type BillingDetails.

Polymorphic queries that return instances of all classes that match the interface of the queried class
are also problematic. Hibernate must execute a query against the superclass as several SQL SELECTs, one for each concrete subclass.

The JPA query "select bd from BillingDetails bd" requires two SQL statements:
select fromID, OWNER, ACCOUNT, BANKNAME, SWIFT from BANKACCOUNT
select fromID, CC_OWNER, CARDNUMBER, EXPMONTH, EXPYEAR from CREDITCARD

Hibernate uses separate query for each concrete subclass. On the other hand, queries against the concrete classes
are trivial and perform well - Hibernate uses only one of the statements.

A further conceptual problem with this mapping strategy is that several different columns, of different tables,
share exactly the same semantics. This makes schema evolution more complex.
For example, renaming or changing the type of a superclass property (BillingDetails.owner) results in changes to multiple columns
in multiple tables.

