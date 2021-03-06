BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "Products" (
	"productID"	INTEGER NOT NULL,
	"productName"	TEXT NOT NULL,
	"unitPrice"	REAL NOT NULL,
	"stockQuantity"	INTEGER NOT NULL,
	PRIMARY KEY("productID")
);
CREATE TABLE IF NOT EXISTS "Purchases" (
	"purchaseID"	INTEGER NOT NULL,
	"customerID"	INTEGER NOT NULL,
	"productID"	INTEGER NOT NULL,
	"price"	REAL NOT NULL,
	"quantity"	REAL NOT NULL,
	"cost"	REAL NOT NULL,
	"tax"	REAL NOT NULL,
	"totalCost"	REAL NOT NULL,
	"date"	INTEGER,
	FOREIGN KEY("customerID") REFERENCES "Customers"("customerID"),
	FOREIGN KEY("productID") REFERENCES "Products"("productID"),
	PRIMARY KEY("purchaseID")
);
CREATE TABLE IF NOT EXISTS "Customers" (
	"customerID"	INTEGER NOT NULL,
	"name"	TEXT DEFAULT 'Guest',
	"phone"	TEXT DEFAULT '(000)000-0000',
	"address"	TEXT DEFAULT 'Auburn, AL',
	PRIMARY KEY("customerID")
);
INSERT INTO "Products" VALUES (1,'Clay Bar',19.99,33);
INSERT INTO "Products" VALUES (2,'Heat Guns',29.99,6);
INSERT INTO "Products" VALUES (3,'65 inch TV',749.99,2);
INSERT INTO "Products" VALUES (4,'PS4',399.99,31);
INSERT INTO "Products" VALUES (5,'XBOX ONE S',299.99,26);
INSERT INTO "Products" VALUES (6,'Amazon Kindle',199.99,11);
INSERT INTO "Products" VALUES (7,'Baby Stroller',299.99,25);
INSERT INTO "Products" VALUES (8,'Baby Diaper',2.99,120);
INSERT INTO "Purchases" VALUES (1,1,1,19.99,12.0,239.88,21.5892,261.4692,'Tue Oct 08 23:36:45 CDT 2019');
INSERT INTO "Purchases" VALUES (2,4,1,19.99,3.0,59.97,5.3973,65.3673,'Tue Oct 08 23:36:45 CDT 2019');
INSERT INTO "Purchases" VALUES (3,6,2,29.99,4.0,119.96,10.7964,130.7564,'Tue Oct 08 23:36:45 CDT 2019');
INSERT INTO "Purchases" VALUES (4,2,2,29.99,3.0,89.97,8.0973,98.0673,'Tue Oct 08 23:36:45 CDT 2019');
INSERT INTO "Purchases" VALUES (5,6,3,749.99,1.0,749.99,67.4991,817.4891,'Tue Oct 08 23:36:45 CDT 2019');
INSERT INTO "Purchases" VALUES (6,3,5,299.99,2.0,599.98,53.9982,653.9782,'Tue Oct 08 23:36:45 CDT 2019');
INSERT INTO "Purchases" VALUES (7,4,7,299.99,1.0,299.99,26.9991,326.9891,'Tue Oct 08 23:36:45 CDT 2019');
INSERT INTO "Purchases" VALUES (8,5,8,2.99,10.0,29.9,2.691,32.591,'Tue Oct 08 23:36:45 CDT 2019');
INSERT INTO "Purchases" VALUES (9,7,3,749.99,1.0,749.99,67.4991,817.4891,'Tue Oct 08 23:40:13 CDT 2019');
INSERT INTO "Purchases" VALUES (10,9,6,199.99,2.0,399.98,35.9982,435.9782,'Tue Oct 08 23:40:13 CDT 2019');
INSERT INTO "Purchases" VALUES (11,10,2,29.99,3.0,89.97,8.0973,98.0673,'Tue Oct 08 23:47:03 CDT 2019');
INSERT INTO "Customers" VALUES (1,'William','(185)864-1832','Auburn, AL');
INSERT INTO "Customers" VALUES (2,'Cox','(284)968-3821','Auburn, AL');
INSERT INTO "Customers" VALUES (3,'Lee','(382)289-1928','Auburn, AL');
INSERT INTO "Customers" VALUES (4,'Daniel','(042)213-4126','Auburn, AL');
INSERT INTO "Customers" VALUES (5,'Miller','(158)239-1295','Auburn, AL');
INSERT INTO "Customers" VALUES (6,'Tayolr','(334)512-3123','Auburn, AL');
INSERT INTO "Customers" VALUES (7,'Diaz','(772)334-6041','Auburn, AL');
INSERT INTO "Customers" VALUES (8,'Brown','(341)412-5816','Auburn, AL');
INSERT INTO "Customers" VALUES (9,'Oliver','(751)285-1682','Auburn, AL');
INSERT INTO "Customers" VALUES (10,'Jacob','(681)186-2851','Auburn, AL');
COMMIT;
