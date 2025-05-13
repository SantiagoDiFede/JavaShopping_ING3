CREATE TABLE `Utilisateur` (
                               `UtilisateurID` int  NOT NULL ,
                               `Login` varchar(50)  NOT NULL ,
                               `Password` varchar(200)  NOT NULL ,
                               `Name` varchar(50)  NOT NULL ,
                               `Mail` varchar(100)  NOT NULL ,
                               `isAdmin` bool  NOT NULL ,
                               PRIMARY KEY (
                                            `UtilisateurID`
                                   ),
                                 CONSTRAINT `uc_Utilisateur_Mail` UNIQUE (
                                                                          `Mail`
                                      )
);

CREATE TABLE `Commande` (
                            `CommandeID` int  NOT NULL ,
                            `UtilisateurID` int  NOT NULL ,
                            `PrixTotal` double  ,
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
                           `PrixReduction` double  ,
                           `QteReduction` int  ,
                           `Image` LONGBLOB  NOT NULL ,
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

ALTER TABLE `utilisateur` CHANGE `UtilisateurID` `UtilisateurID` INT(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `commande` CHANGE `CommandeID` `CommandeID` INT(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `commandeligne` CHANGE `CommandeLigneID` `CommandeLigneID` INT(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `produit` CHANGE `ProduitID` `ProduitID` INT(11) NOT NULL AUTO_INCREMENT;

INSERT INTO `utilisateur` (`UtilisateurID`, `Login`, `Password`, `Name`, `Mail`, `isAdmin`) VALUES (NULL, 'Santiago', 'admin', 'Admin', 'santiago@mail.fr', '1');
INSERT INTO `utilisateur` (`UtilisateurID`, `Login`, `Password`, `Name`, `Mail`, `isAdmin`) VALUES (NULL, 'Aaron2', '1234', 'Aaron', 'aaron@yahoo.us', '0');
INSERT INTO `utilisateur` (`UtilisateurID`, `Login`, `Password`, `Name`, `Mail`, `isAdmin`) VALUES (NULL, 'John12', '4321', 'John', 'john@gmail.com', '0');
INSERT INTO `utilisateur` (`UtilisateurID`, `Login`, `Password`, `Name`, `Mail`, `isAdmin`) VALUES (NULL, 'Lucy', 'ycul', 'Lucy', 'Lulu@gmail.com', '0');
INSERT INTO `utilisateur` (`UtilisateurID`, `Login`, `Password`, `Name`, `Mail`, `isAdmin`) VALUES (NULL, 'Jacob', 'DaMan', 'Jake', 'Jacob@gmail.com', '0');
INSERT INTO `utilisateur` (`UtilisateurID`, `Login`, `Password`, `Name`, `Mail`, `isAdmin`) VALUES (NULL, 'Snowfall', 'woof', 'Snow', 'snow@gmail.com', '0');


INSERT INTO `produit` (`ProduitID`, `Nom`, `Prix`, `PrixReduction`, `QteReduction`, `Image`) VALUES (NULL, 'Nike air force', 99.99, 79.99, 10, 'nike.jpeg');
INSERT INTO `produit` (`ProduitID`, `Nom`, `Prix`, `PrixReduction`, `QteReduction`, `Image`) VALUES (NULL, 'Statue Lego', 39.99, 29.99, 5, 'lego.jpeg');
INSERT INTO `produit` (`ProduitID`, `Nom`, `Prix`, `PrixReduction`, `QteReduction`, `Image`) VALUES (NULL, 'Statue astroboy', 49.99, 44.99, 4, 'astro.jpeg');
INSERT INTO `produit` (`ProduitID`, `Nom`, `Prix`, `PrixReduction`, `QteReduction`, `Image`) VALUES (NULL, 'jouet voiture', 299.99, 249.99, 2, 'toycar.jpeg');
INSERT INTO `produit` (`ProduitID`, `Nom`, `Prix`, `PrixReduction`, `QteReduction`, `Image`) VALUES (NULL, 'allumettes', 0.99, 0.49, 200, 'allumettes.jpg');
INSERT INTO `produit` (`ProduitID`, `Nom`, `Prix`, `PrixReduction`, `QteReduction`, `Image`) VALUES (NULL, 'Pelle', 60.00, 30.00, 6, 'pelle.jpg');


INSERT INTO `commande` (`CommandeID`, `UtilisateurID`, `PrixTotal`, `StatutCommande`) VALUES (NULL, 3, 0, 'En cours');
INSERT INTO `commandeligne` (`CommandeLigneID`, `CommandeID`, `ProduitID`, `Qte`) VALUES (NULL, 1, 1, 2);
INSERT INTO `commandeligne` (`CommandeLigneID`, `CommandeID`, `ProduitID`, `Qte`) VALUES (NULL, 1, 2, 1);
