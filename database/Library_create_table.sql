CREATE DATABASE library;
 
USE Library;

CREATE TABLE BOOK(
Book_id VARCHAR(10) PRIMARY KEY,
Title VARCHAR(100)
);

CREATE TABLE BOOK_AUTHORS(
Book_id VARCHAR(10) REFERENCES Book(Book_id) ON DELETE CASCADE ON UPDATE CASCADE,
Author_name VARCHAR(100),
Type int,
PRIMARY KEY(Book_id,Author_name) 
);

CREATE TABLE Library_Branch(
Branch_id int PRIMARY KEY,
Branch_name VARCHAR(200),
Address VARCHAR(200)
);

CREATE TABLE Book_Copies(
Book_id VARCHAR(10) REFERENCES Book(Book_id)
ON DELETE CASCADE ON UPDATE CASCADE,
Branch_id int REFERENCES Library_Branch(Branch_id)
ON DELETE CASCADE ON UPDATE CASCADE,
No_of_copies int,
No_of_available int,
PRIMARY KEY(Book_id,Branch_id)
);


CREATE TABLE Borrower(
Card_no int PRIMARY KEY,
Fname VARCHAR(50),
Lname VARCHAR(50),
Address VARCHAR(200),
Phone varchar(15)
);

CREATE TABLE Book_Loans(
Loan_id int AUTO_INCREMENT PRIMARY KEY,
Book_id VARCHAR(10) REFERENCES Book(Book_id)
ON DELETE CASCADE ON UPDATE CASCADE,
Branch_id int REFERENCES Library_Branch(Branch_id)
ON DELETE CASCADE ON UPDATE CASCADE,
Card_no int REFERENCES Borrower(Card_no)
ON DELETE CASCADE ON UPDATE CASCADE,
Date_out date,
Due_Date date,
Date_in date 
);

CREATE TABLE FINES(
loan_id int PRIMARY KEY REFERENCES Book_Loans(loan_id)
ON DELETE CASCADE ON UPDATE CASCADE,
fine_amt numeric(10, 2),
paid bool
);


