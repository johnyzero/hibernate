You can’t have another entity referencing BillinDetails entity — there is no such table.
BillingDetails can not be @MappedSuperclass when it's a target class in associations (when some entities
have field with type BillingDetails).
