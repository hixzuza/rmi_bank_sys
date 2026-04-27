DROP DATABASE IF EXISTS BANK;
CREATE DATABASE BANK CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE BANK;



CREATE TABLE UTILISATEUR (
    id_user   INT          PRIMARY KEY AUTO_INCREMENT,
    username  VARCHAR(50)  UNIQUE NOT NULL,
    mot_pass  VARCHAR(200) NOT NULL,
    nom       VARCHAR(50)  NOT NULL,
    prenom    VARCHAR(50)  NOT NULL,
    role      VARCHAR(14)  NOT NULL CHECK (role IN ('CLIENT', 'ADMIN'))
);

CREATE TABLE COMPTE (
    numero_compte VARCHAR(50)    PRIMARY KEY,
    solde         DECIMAL(15,2)  NOT NULL DEFAULT 0.00,
    date_creation DATE           NOT NULL DEFAULT (CURRENT_DATE),
    actif         BOOLEAN        NOT NULL DEFAULT TRUE,
    id_user       INT            NOT NULL,
    FOREIGN KEY (id_user) REFERENCES UTILISATEUR(id_user)
);

CREATE TABLE TRANSACTION_B (
    id_transaction INT           PRIMARY KEY AUTO_INCREMENT,
    numero_compte  VARCHAR(50)   NOT NULL,
    type_op        VARCHAR(14)   NOT NULL CHECK (type_op IN ('DEPOT', 'RETRAIT', 'VIREMENT')),
    montant        DECIMAL(15,2) NOT NULL CHECK (montant > 0),
    date_op        DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    compte_dest    VARCHAR(50)   NULL,
    FOREIGN KEY (numero_compte) REFERENCES COMPTE(numero_compte),
    FOREIGN KEY (compte_dest)   REFERENCES COMPTE(numero_compte)
);


CREATE INDEX idnx_auth        ON UTILISATEUR(username, mot_pass);
CREATE INDEX idnx_transaction  ON TRANSACTION_B(numero_compte);
CREATE INDEX idnx_user_compte  ON COMPTE(id_user);
CREATE INDEX idnx_compte_actif ON COMPTE(actif);



-- SET FOREIGN_KEY_CHECKS = 0;
-- DELETE FROM TRANSACTION_B;
-- DELETE FROM COMPTE;
-- DELETE FROM UTILISATEUR;
-- SET FOREIGN_KEY_CHECKS = 1;
-- ALTER TABLE UTILISATEUR    AUTO_INCREMENT = 1;
-- ALTER TABLE TRANSACTION_B  AUTO_INCREMENT = 1;



-- SHOW TABLES;
-- SELECT * FROM UTILISATEUR;
-- SELECT * FROM COMPTE;
-- SELECT * FROM TRANSACTION_B;