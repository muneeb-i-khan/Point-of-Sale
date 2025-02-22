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

# Backend
[*]Organize Models folder
[*]lombok -> for getter and setter HIGH
[*]Application props file in spring
[]Check if we can avoid lists and use sql for referencing tables for optimizations
[]barcode -> prod id in pojo and similiarly remove all redundant fields
[]Handle references by joins rather than cascading and Spring, and remove all redundant data HIGH
[]Replace Delete method with isDeleted field
[]Abstract Pojo
[] Invoice
[]Checks and validations  HIGH
[*]Replace @ReqMapping with @GetMapping for eg. everywhere  LOW
[] Rest Controller Advice -> Diff exceptions  HIGH
[] Use Transactional at Class level
[] Study about id's
# Frontend
[] Pagination for get all HIGH
[] Reports  HIGH
[] Fix  UI
[] Session mgmt
[] Combine view and create forms
