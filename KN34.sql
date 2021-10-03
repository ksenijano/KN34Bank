CREATE TABLE Customers (
CustomerID int not null AUTO_INCREMENT,
UserName varchar (6) not null,
Password varchar (8) not null,
LastName varchar(25) not null,
FirstName varchar(25) not null,
EmailAddress varchar(25),
PRIMARY KEY (CustomerID)
);

CREATE TABLE Accounts (
AccountID int not null AUTO_INCREMENT,
CustomerID int not null REFERENCES Customers(CustomerID),
Balance decimal(20,2),
Currency varchar(25) not null,
relatedUserName varchar (6) not null,
PRIMARY KEY (AccountID)
);
