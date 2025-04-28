CREATE TABLE `Utilisateur` (
                               `UtilisateurID` int  NOT NULL ,
                               `Login` varchar(50)  NOT NULL ,
                               `Password` varchar(200)  NOT NULL ,
                               `Name` varchar(50)  NOT NULL ,
                               `Mail` varchar(100)  NOT NULL ,
                               `isAdmin` bool  NOT NULL ,
                               PRIMARY KEY (
                                            `UtilisateurID`
                                   )
);

CREATE TABLE `Commande` (
                            `CommandeID` int  NOT NULL ,
                            `UtilisateurID` int  NOT NULL ,
                            `Total` double  ,
                            `StatutCommande` varchar(20)  NOT NULL ,
                            PRIMARY KEY (
                                         `CommandeID`
                                )
);

CREATE TABLE `CommandeLigne` (
                                 `CommandeLigneID` int  NOT NULL ,
                                 `CommandeID` int  NOT NULL ,
                                 `ProduitID` int  NOT NULL ,
                                 `Qte` int  NOT NULL ,
                                 PRIMARY KEY (
                                              `CommandeLigneID`
                                     )
);

CREATE TABLE `Produit` (
                           `ProduitID` int  NOT NULL ,
                           `Nom` varchar(200)  NOT NULL ,
                           `Prix` double  NOT NULL ,
                           `RabaisPrix` double  ,
                           `Image` varchar(1000)  NOT NULL ,
                           PRIMARY KEY (
                                        `ProduitID`
                               ),
                           CONSTRAINT `uc_Produit_Nom` UNIQUE (
                                                               `Nom`
                               )
);

ALTER TABLE `Commande` ADD CONSTRAINT `fk_Commande_UtilisateurID` FOREIGN KEY(`UtilisateurID`)
    REFERENCES `Utilisateur` (`UtilisateurID`);

ALTER TABLE `CommandeLigne` ADD CONSTRAINT `fk_CommandeLigne_CommandeID` FOREIGN KEY(`CommandeID`)
    REFERENCES `Commande` (`CommandeID`);

ALTER TABLE `CommandeLigne` ADD CONSTRAINT `fk_CommandeLigne_ProduitID` FOREIGN KEY(`ProduitID`)
    REFERENCES `Produit` (`ProduitID`);
ALTER TABLE `Utilisateur` ADD CONSTRAINT `uc_Utilisateur_Login` UNIQUE (`Login`);