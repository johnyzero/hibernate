This mapping strategy is a winner in terms of both performance and simplicity.

There is one major problem: data integrity!!!
You must declare columns for properties declared by subclasses to be nullable.
If your subclasses each define several non-nullable properties,
the loss of NOT NULL constraints may be a serious problem from the point of view of data correctness.

Another important issue is normalization. You have declared functional dependencies between
non-key columns, violating the third normal form.

The root class BillingDetails of the inheritance hierarchy is mapped
to the table BILLINGDETAILS automatically.
Only properties of the superclass can be NOT NULL in the schema!!! Other properties should ne nullable in the schema.

Hibernate requires that you declare nullability with @Column because Hibernate
ignores Bean Validation’s @NotNull when it generates the database schema.

If you don’t specify a discriminator column in the superclass, its name defaults to DTYPE and the value are strings.

Without an explicit discriminator value, Hibernate defaults to the fully qualified class name
if you use Hibernate XML files and the simple entity name if you use annotations or JPA XML files.

Note that JPA doesn’t specify a default for non-string discriminator types.
Each persistence provider can have different defaults. Therefore, you should always specify
discriminator values for your concrete classes.

Remember that NOT NULL constraints aren’t allowed in the schema, because a BankAccount instance
won’t have an expMonth property, and the EXPMONTH column must be NULL for that row. Hibernate
ignores the @NotNull for schema DDL generation, but it observes it at runtime, before inserting a row.

Hibernate generates the following SQL for "select bd from BillingDetails bd":
select
    ID, OWNER, EXPMONTH, EXPYEAR, CARDNUMBER,
fromACCOUNT, BANKNAME, SWIFT, BD_TYPE
    BILLINGDETAILS

To query the CreditCard subclass, Hibernate adds a restriction on the discriminator column:
select
    ID, OWNER, EXPMONTH, EXPYEAR, CARDNUMBER
from
    BILLINGDETAILS
where
    BD_TYPE='CC'

You can apply an expression to calculate a discriminator value for each row.
Formulas for discrimination aren’t part of the JPA specification, but Hibernate
has an extension annotation, @DiscriminatorFormula.
