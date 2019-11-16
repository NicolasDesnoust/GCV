GCV

1) Changer les informations de connexion à la base de données (MySQL 5 actuellement) dans 
les fichiers conf/openejb.xml et src/META-INF/persistence.xml.

2) Changer la propriété AsynchronousPool.Size dans conf/system.properties et l'attribut TASKS dans src/gcv/tests/TestFillDB
pour coincider avec le nombre de processeurs physiques (6 sur ma machine par exemple).
