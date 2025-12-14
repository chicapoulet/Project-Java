# Guide de Lancement - Application de Gestion de Stock

## 1. Prérequis (À installer sur le PC)
Pour que l'application fonctionne, assurez-vous d'avoir installé :

*   **Java JDK 17** (ou plus récent)
*   **Apache Maven** (Outil de construction)
*   **XAMPP** (ou un serveur MySQL autonome)

## 2. Configuration de la Base de Données
Avant la première utilisation :
1.  Lancez le module **MySQL** dans XAMPP.
2.  Importez le fichier `db_schema.sql` (situé à la racine du projet) dans votre base de données (via phpMyAdmin ou ligne de commande).

## 3. Lancer l'Application
1.  Ouvrez un terminal (CMD ou PowerShell).
2.  Placez-vous dans le dossier du projet :
    ```bash
    cd C:\Users\natha\scratch\java_inventory_app
    ```
3.  Exécutez la commande suivante :
    ```bash
    mvn clean javafx:run
    ```

## 4. Identifiants par défaut
*   **Admin** : `admin` / `admin123`
*   **Vendeur** : `vendeur` / `vendeur123`
