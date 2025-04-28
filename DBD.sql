CREATE TABLE `Customer` (
                            `CustomerID` int  NOT NULL ,
                            `Login` String  NOT NULL ,
                            `Password` varchar(200)  NOT NULL ,
                            `FirstName` string  NOT NULL ,
                            `LastName` string  NOT NULL ,
                            `Mail` string  NOT NULL ,
                            PRIMARY KEY (
                                         `CustomerID`
                                )
                            CONSTRAINT U_Mail UNIQUE (Mail)
);

CREATE TABLE `Admin` (
                         `AdminID` int  NOT NULL ,
                         `Login` String  NOT NULL ,
                         `Password` varchar(200)  NOT NULL ,
                         PRIMARY KEY (
                                      `AdminID`
                             )
);

CREATE TABLE `Order` (
                         `OrderID` int  NOT NULL ,
                         `CustomerID` int  NOT NULL ,
                         `TotalAmount` money  NOT NULL ,
                         `OrderStatusID` int  NOT NULL ,
                         PRIMARY KEY (
                                      `OrderID`
                             )
);

CREATE TABLE `OrderLine` (
                             `OrderLineID` int  NOT NULL ,
                             `OrderID` int  NOT NULL ,
                             `ProductID` int  NOT NULL ,
                             `Quantity` int  NOT NULL ,
                             PRIMARY KEY (
                                          `OrderLineID`
                                 )
);


CREATE TABLE `Product` (
                           `ProductID` int  NOT NULL ,

                           `Name` varchar(200)  NOT NULL ,
                           `Price` money  NOT NULL ,
                           `SalePrice` money  NOT NULL ,
                           `Brand` String  NOT NULL ,
                           `Category` String  NOT NULL ,
                           `Image` varchar(max)  NOT NULL ,
    PRIMARY KEY (
        `ProductID`
    ),
    CONSTRAINT `uc_Product_Name` UNIQUE (
        `Name`
    )
);

CREATE TABLE `ProductSale` (
                               `ProductID` int  NOT NULL ,
                               `SaleID` int  NOT NULL ,
                               `QtyForSale` int  NOT NULL ,
                               PRIMARY KEY (
                                            `ProductID`,`SaleID`
                                   )
);

CREATE TABLE `OrderStatus` (
                               `OrderStatusID` int  NOT NULL ,
                               `Name` string  NOT NULL ,
                               PRIMARY KEY (
                                            `OrderStatusID`
                                   ),
                               CONSTRAINT `uc_OrderStatus_Name` UNIQUE (
                                                                        `Name`
                                   )
);

ALTER TABLE `Order` ADD CONSTRAINT `fk_Order_CustomerID` FOREIGN KEY(`CustomerID`)
    REFERENCES `Customer` (`CustomerID`);

ALTER TABLE `Order` ADD CONSTRAINT `fk_Order_OrderStatusID` FOREIGN KEY(`OrderStatusID`)
    REFERENCES `OrderStatus` (`OrderStatusID`);

ALTER TABLE `OrderLine` ADD CONSTRAINT `fk_OrderLine_OrderID` FOREIGN KEY(`OrderID`)
    REFERENCES `Order` (`OrderID`);

ALTER TABLE `OrderLine` ADD CONSTRAINT `fk_OrderLine_ProductID` FOREIGN KEY(`ProductID`)
    REFERENCES `Product` (`ProductID`);

ALTER TABLE `ProductSale` ADD CONSTRAINT `fk_ProductSale_ProductID` FOREIGN KEY(`ProductID`)
    REFERENCES `Product` (`ProductID`);

