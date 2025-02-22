# PoS Application

## To do
### Priority - HIGH
- Role based Authorization
- Session storage reset after 5 min
- Write invoice generation logic
### Priority - MED
- Tests for orders and others
### Priority - LOW
- UI elements like snack and toasts
- Look out for scope of having generic functions and overall refactoring.

[*]Organize Models folder
[]lombok -> for getter and setter HIGH
[]Check if we can avoid lists and use sql for referencing tables for optimizations
[]barcode -> prod id in pojo and similiarly remove all redundant fields
[]Abstract Pojo
[]Replace Delete method with isDeleted field
[]Handle references by joins rather than cascading and Spring, and remove all redundant data HIGH
[*]Application props file in spring
[]Checks and validations  HIGH
[]Replace @ReqMapping with @GetMapping for eg. everywhere  LOW
[] Rest Controller Advice -> Diff exceptions  HIGH
[] Pagination for get all HIGH
[] Reports  HIGH
[] Fix  UI
[] Combine view and create forms
[] Invoice
