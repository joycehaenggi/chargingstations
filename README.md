# OOP2 Programmierprojekt FS19

## Bearbeitet von
 - Joyce Hänggi
 
##Custom Control von
 - Linus Kohler
 - Manuel Riedi
 
 - Sandro Osswald
 - Cedric Bühler

## Abgabe
- Mittwoch, 5.6.2019, 18:00 Uhr
- Die Abgabe erfolgt durch ein "Push" auf den Master-Branch in Ihrem GitHub-Repository.

## Aufgabe: ChargingStationsFX

Implementieren Sie eine Applikation auf Basis JavaFX gemäss der Aufgabenbeschreibung auf dem AD. 

Die dort vorhandene Beschreibung ist massgebend, hier die wichtigsten Punkte:
 - Benutzen Sie zur Umsetzung die vorgegebene Struktur des "Application-Template".
 - Die Verwendung von SceneBuilder und FXML ist nicht erlaubt.
 - Für eine 4.0 müssen folgende Basis-Features implementiert sein
   - Einlesen der Daten
   - Abspeichern der Änderungen
   - Darstellen aller Ladestationen in Tabelle/Liste 
   - Editor-Bereich
     - Editor-Bereich arbeitet stets auf der in der Tabelle selektierten Ladestation
     - Änderungen führen zu *unmittelbarem* Update der Tabelle und des Headers
     - Ein- Ausblenden der "Steckertypen" und "Leistung" abhängig von Anzahl Ladepunkte
     - unmittelbare Berechnung der Gesamt-Anschlussleistung
   - Header-Bereich 
     - Darstellung von: Typ, Strasse, Postleitzahl, Ort, Anzahl Ladepunkte, Anschlussleistung
     - einfaches Styling via CSS
   - Layout mit SplitPane
   - sinnvolles Resizing-Verhalten
   - Anlegen einer neuen Ladestation
   - Löschen bestehender Ladestationen
   - Programmstruktur
     - zwei Layer für Presentation-Model und View 

 
**Nutzen Sie die Coding-Night am Montag, 27.5.2019, 18:00 Uhr.** 

## Bitte beachten Sie:
 - Die Bewertungskriterien sind ebenfalls in dem pdf-File auf dem AD enthalten.
   - die dort definierten Bewertungskriterien sind massgebend
   - nicht compilierbarer Code wird mit einer 1.0 gewertet
   - nicht selbstständig bearbeitete Projekte führen zu einem deutlichen Notenabzug
   
 - Im Projekt enthalten ist ein Daten-File
   - `CHARGING_STATION.csv` enthält die anzuzeigenden Daten der Ladestationen
 
 - Das Programmierprojekt kann auch in einem 2er-Team bearbeitet werden. 
 
 - Falls Sie zu zweit arbeiten:
   - tragen Sie beide Namen unter "Bearbeitet von" ein
   - arbeiten Sie ausschliesslich in einem Repository
   - falls sie beide Zugang zu diesem Repository wollen: tragen Sie die zweite Person als "Collaborator" ein (auf GitHub unter "Settings - > Collaborators & teams")
   - löschen Sie das nicht benötigte Repository (auf GitHub unter "Settings")
   - arbeiten Sie gemeinsam und gleichzeitig an den Aufgaben (Stichwort: Pair-Programming)
   - das Aufteilen und separate Bearbeiten von Aufgaben ist nicht erwünscht
 
 - Ausdrücklich erlaubt und erwünscht ist, dass Sie sich gegebenenfalls Hilfe holen.
   - Das Programmierzentrum ist geöffnet und Nachfragen werden zum Beispiel über den im Repository integrierten 
 Issue Tracker oder per Mail gerne beantwortet. 



 ## Bei Problemen mit dem IntelliJ-Setup
 Es kommt immer wieder mal vor, dass der Setup des IntelliJ-Projekts nicht auf Anhieb funktioniert oder "plötzlich"
 nicht mehr funktioniert.
 
 Sie brauchen in so einem Fall NICHT nochmal den Invitation-Link annehmen oder das Projekt via “Check out from Version Control” oder "git clone ..." nochmal anlegen.
 
 Statt dessen ist es am besten den IntelliJ-Setup neu generieren zu lassen. Dazu verwendet man den File "build.gradle", der eine 
 komplette und IDE-unabhängige Projektbeschreibung enthält.
 
 Die einzelnen Schritte:
 
 - Schliessen Sie alle geöffneten Projekte (File -> Close Project)
 
 - Wählen Sie “OPEN” 
 
 - Es erscheint ein Finder-Fenster mit dem Sie zu ihrem Projekt navigieren.
 
 - Dort wählen Sie den File “build.gradle” aus.
 
 - Beim nächsten Dialog “Open as Project” wählen.
 
 - Beim nächsten Dialog kontrollieren ob Java 10 ausgewählt ist.
 
 - Dann “File already exists” mit YES bestätigen.
 
 - ACHTUNG: Jetzt “Delete existing Project and Import” anklicken.
 
 - Warten, warten, warten.
 
 Wenn alles gut gegangen ist sollte im Project-View der Java-Ordner unter src/main blau sein und der Java-Ordner unter src/test grün.