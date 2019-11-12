# Nettoyage du fichier des noms de famille à l'aide de SublimeText.
#Suppression des caractères [0-9_] (expression régulière) via l'option de remplacement.

# Nettoyage du fichier des noms de famille à l'aide de MySQL.
UPDATE `last_names` SET nom = REPLACE(nom, '\t', '')

# Nettoyage du fichier des prénoms à l'aide de MySQL.

CREATE TABLE `nat2018` (
	sexe INT(1),
    first_name VARCHAR(50) PRIMARY KEY,
    year VARCHAR(4),
    count INT(10)
)

# Suppression des colonnes inutiles
ALTER TABLE `nat2018`
DROP `sexe`, DROP `year`, DROP `count`

# Suppression des prénoms de longueur inférieure à 3
DELETE FROM `nat2018` WHERE LENGTH(`first_name`) < 3;

DELETE * FROM `nat2018_csv` WHERE first_name LIKE '%PRENOM_RARES';

# Suppression des noms en double et des colonnes non-nécessaires.
INSERT INTO `first_names` (first_name, count)
	SELECT preusuel, COUNT(nombre) 
	FROM `nat2018_csv`
	GROUP BY preusuel
	ORDER BY COUNT(nombre) DESC