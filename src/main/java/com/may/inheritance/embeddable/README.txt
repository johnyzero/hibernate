Use the @MappedSuperclass annotation on the superclass of the embeddable class you’re
mapping just like you would for an entity. Subclasses will inherit the properties of this
class as persistent properties. See @MappedSuperclass on Measurement class.

Dimensions and Weight subclasses are embeddable components.

Pay attention that Dimensions and Weight classes have @AttributeOverride on them.
Without this override, an Item embedding both Dimension and Weight would map to a
table with conflicting column names.

When you add properties of type Weight and Dimensions to Item class you just include their columns into Item class.

Item class will have the following columns:
id
depth
height
width
dimensions_name
dimensions_symbol
name
weight
weight_name
weight_symbol