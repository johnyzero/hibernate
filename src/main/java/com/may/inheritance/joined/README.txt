If you make an instance of the CreditCard subclass persistent, Hibernate inserts two rows.
The values of properties declared by the BillingDetails superclass are stored in a new row of
the BILLINGDETAILS table. Only the values of properties declared by the subclass are stored in a
new row of the CREDITCARD table. The primary key shared by the two rows links them together.
Later, the subclass instance may be retrieved from the database by joining the subclass table
with the superclass table.

The primary advantage of this strategy is that it normalizes the SQL schema.
Schema evolution and integrity-constraint definition are straightforward.
A foreign key referencing the table of a particular subclass may represent a
polymorphic association to that particular subclass.

In subclasses, you don’t need to specify the join column if the primary key column of
the subclass table has (or is supposed to have) the same name as the primary key column of
the superclass table.

Pay attention to the @PrimaryKeyJoinColumn(name = "CREDITCARD_ID") annotation on CreditCard class
that defines PK name.

Hibernate relies on an SQL outer join for "select bd from BillingDetails bd":
select
    BD.ID, BD.OWNER,
    CC.EXPMONTH, CC.EXPYEAR, CC.CARDNUMBER,
    BA.ACCOUNT, BA.BANKNAME, BA.SWIFT,
    case
        when CC.CREDITCARD_ID is not null then 1
        when BA.ID is not null then 2
        when BD.ID is not null then 0
    end
from
    BILLINGDETAILS BD
    left outer join CREDITCARD CC on BD.ID=CC.CREDITCARD_ID
    left outer join BANKACCOUNT BA on BD.ID=BA.ID

The SQL CASE ... WHEN clause detects the existence (or absence) of rows in the subclass
tables CREDITCARD and BANKACCOUNT, so Hibernate can determine the concrete subclass for a
particular row of the BILLINGDETAILS table.

For a narrow subclass query like "select cc from CreditCard cc", Hibernate uses an inner join:
select
    CREDITCARD_ID, OWNER, EXPMONTH, EXPYEAR, CARDNUMBER
from
    CREDITCARD
    inner join BILLINGDETAILS on CREDITCARD_ID=ID

As you can see, this mapping strategy is more difficult to implement by hand - even ad hoc reporting
is more complex. This is an important consideration if you plan to mix Hibernate code with handwritten
SQL.

Furthermore, even though this mapping strategy is deceptively simple, our experience is that performance
can be unacceptable for complex class hierarchies.
Queries always require a join across many tables, or many sequential reads.