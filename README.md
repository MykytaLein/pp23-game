### English

# Programming Course Project

## Game Flow

The project focuses on implementing a game. On a game board, the player's game component is located in the bottom left 
corner, and the computer's game component is in the top right corner. Moves involve changing the color of the game 
component. The goal of the game, from the player's perspective, is to have a larger game component than the computer's 
by the end of the game. One can add an adjacent field (a field with one or more sides bordering the game component) to 
its game component by choosing the color of that field as the next color for one's game component. However, one cannot 
choose the color of its own game component or the color of the computer's game component as the next color for its 
own game component. The game proceeds as follows:

- Upon starting the game, a game window with settings options is displayed.
- Clicking the "Help" button displays the help instructions for the game flow, including how to choose a new color.
- Initially, the player selects the configuration of the game board, including the number of colors to play with and the 
number of rows and columns that make up the game board.
- Once the selection is made, pressing the "Start" button generates and displays the new game board:
  - The game board is generated so that: 1. all adjacent fields have different colors, 2. the player's and computer's 
  game components have different colors, and 3. all colors currently in play appear at least once on the game board.
- Next, one can choose who takes the first move and the depth of minimax searching algorithm.
- The game is considered over when all fields on the game board belong to either the player's or the computer's game component, or if in the last four moves (two per player), the game components did not increase in size.
  
## Project Structure

The project structure follows programming best practices, with the classes organized as follows:

- Classes defining the user interface are contained in the **'gui'** package.
- Classes implementing the game logic are in the **'logic'** package.
- The **'testing'** package contains the class for testing and analyzing the game.
- The **'graph'** package contains classes that represent and analyze the game board as an undirected graph with 
connected fields of the same color as nodes.

## Highlights

- The game board is represented as a ***two-dimensional array of fields***.
- The computer needs some time (up to 5 seconds) to make a move. To avoid delaying the timer, 
the computer's game logic is implemented as a separate ***Thread***.
- The computer selects the best move based on the result of the ***minimax*** algorithm. Additionally, to minimize the runtime of the program, a few optimizations are implemented:
  - Alpha-Beta Pruning.
  - The colors in the algorithm are sorted in descending order based on the number of adjacent fields of that color in the algorithm, making the pruning process even more efficient.
- Wherever it makes sense, data structures such as HashSet and HashMap are used for storing unique values, since both 
adding and searching for elements in such data structures takes **O(1)** time.
- The testing class includes methods for game analysis. The most interesting ones are:
  - minMoves(int row, int col): This method returns the number of moves needed for the player to add the field with 
  coordinates row and col to its game component. It assumes the player is playing alone and chooses colors in 
  ascending and cyclical order (i.e., if color 1 is chosen as first color, playeer must choose 2, then 3, and so on up to 6, then start 
  again from 1, and so on). The game board is represented as an undirected graph, and the minimum number of moves to add 
  the component containing the field to the player's game component is calculated using the ***Dijkstra's algorithm***. 
  Distances between components are initialized based on the color selection strategy (using the modulo operator), and 
  the first color is chosen using a brute-force approach.
  - toBoard(Field[][] anotherBoard, int moves): This method returns true if it is possible to transform the game board 
  stored in the testing class instance into **anotherBoard** using a maximum of **moves** number of moves. Both players, 
  the player and the computer, make moves and can choose colors freely. The game board is represented as a graph with 
  distances between adjacent nodes equal to 1. The minimum distance from each node to the player's and computer's game 
  components is calculated using Dijkstra's algorithm, and the conditions are checked to determine if it is possible 
  to transform the initial game board into **anotherBoard** within **moves** number of moves.
- The code is sufficiently commented, enabling easier navigation within and maintenance of the project.

### Deutsch

# Programmierpraktikumsprojekt

## Ablauf des Spiels
Das Projekt befasst sich mit der Umsetzung eines Spiels. Auf einem Spielbrett befindet sich in der linken unteren Ecke 
die Spielkomponente vom Spieler und in der rechten oberen Ecke die Spielkomponente des Computers. Die Züge stellen ein 
Wechsel der Farbe der Spielkomponente dar. Ziel des Spiels 
aus Sicht der Spieler ist es am Ende des Spiels eine größere Spielkomponente als die vom Rechner zu haben. 
Man kann ein benachbartes Feld (ein Feld mit einer oder mehreren Seiten, die an der Spielkomponente grenzen) zu seiner 
Spielkomponente hinzufügen dadurch, dass man als nächste Farbe die Farbe dieses Felds wählt. Man darf nicht die Farbe 
von der eigenen Spielkomponente und die Farbe von der Spielkomponente vom Computer nicht als nächste Farbe seiner 
Spielkomponente wählen.
Das Spiel läuft folgendermaßen ab:
- Beim Starten des Spiels wird ein Spielfenster mit den Einstellungsmöglichkeiten angezeigt.
- Durch das Klicken auf "Help" Schaltfläche kann man die Hilfe zum Spielablauf (insbesondere wie man eine neue Farbe wählen kann) anzeigen lassen.
- Zunächst wählt der Spieler die Konfiguration des Spielbretts (mit wie vielen Farben das Spiel gespielt wird, aus wie viele Zeilen und Spalten ist das Spielbrett zusammengesetzt).
- Wenn die Wahl eingetroffen ist, kann man durch das Drücken auf die Schaltfläche "Start" das neue Spielbrett generieren und anzeigen lassen:
  - Das Spielbrett ist so generiert, dass 1. alle benachbarten Felder unterschiedliche Farben haben, 2. Spielkomponente 
von Spieler und Computer unterschiedliche Farben haben und 3. alle Farben, die aktuell im Spiel sind, mindestens ein Mal auf dem Spielbrett vorkommen.
- Als Nächstes kann man wählen, wer den ersten Zug macht und wie tief der minimax Algo nach dem besten Zug sucht:
- Wenn alle Felder auf dem Spielbrett entweder zur Spielkomponente vom Spieler oder zur Spielkomponente vom Computer 
gehören oder wenn in den letzten vier Zügen (zwei pro Spielteilnehmer) die Spielkomponente sich nicht vergrößert haben, 
gilt das Spiel als beendet. 


## Projektstruktur

In der Projektstruktur sind nach Programmierung best practice die Klassen wie folgt getrennt:
- Klassen, die die Benutzeroberfläche definieren, sind in **'gui'** Package enthalten.
- Klassen, die Logik des Spiels realisieren, sind in **'logic'** Package enthalten.
- **'testing'** Package beinhaltet die Klasse zur Testing und Analyse des Spiels.
- in der **'graph'** Package sind die Klassen enthalten, die das Spielbrett als ein ungerichtetes Graph mit zusammenhängenden 
Feldern von einer Farbe als Nodes darstellen und analysieren. 

## Highlights

- Das Spielbrett ist als ein ***zwei-dimensionales Array von Feldern*** dargestellt.
- Der Rechner braucht einige Zeit um den Zug zu machen. Um den Timer nicht zu verzögern ist die
Computer Spiellogik Klasse als ein separater ***Thread*** realisiert.
- Der Rechner wählt den besten Zug basierend auf dem Ergebnis vom ***minimax-Algorithmus***. Außerdem um die
Laufzeit des Programms zu minimieren sind ein paar optimizationen implimentiert:
  - **Alpha-Beta Pruning**
  - Die Farben sind im Algorithmus absteigend nach Anzahl der benachbarten Felder dieser Farbe sortiert,
was den Pruning-Prozess noch effizienter macht.
- Überall, wo es sinnvoll ist, ist für die Speicherung von eindeutigen Werten Datenstrukturen wie HashSet und HashMap, da die sowohl 
Hinzufügen von eindeutigen als auch Suche nach Elementen in solchen Datenstrukturen nur **O(1)** Zeit benötigt.
- In der Testing Klasse befinden sich Methoden zur Analyse des Spiels. Die interessantesten davon sind:
  - **minMoves(int row, int col):** die Methode gibt zurück, wie viele Züge bräuchte der Spieler, um das Feld mit den 
  Koordinaten row, col zu seiner Komponente hinzuzufügen. Hier wird es davon ausgegangen, dass der Spieler alleine spielt 
  und dass er die Farben aufsteigend und zyklisch wählt (d.h. wenn er als erste Farbe 1 wählt muss er danach 2, dann 3 usw. 
  bis 6 wählen und nach 6 wieder die 1 usw.). Dafür wird das Spielbrett als ein ungerichtetes Graph dargestellt
  und mithilfe vom ***Dijkstra Algorithmus*** die minimale Anzahl an Zügen berechnet, um die Komponente, die das Feld 
  enthält, zur Spielkomponente vom Spieler hinzuzufügen. Dabei werden die Distanzen zwischen den Komponenten mit Hinsicht 
  auf Spielwahlstrategie initialisiert (mithilfe vom Modulo-Operanden) und erste Farbe mit Brute-Force Vorgehen gewählt.
  - **toBoard(Field[][]anotherBoard, int moves):** die Methode gibt true zurück, wenn es möglich ist das in der Testing 
  Klasse Instanz gespeicherte Spielbrett in höchstens **moves** Zügen so umzuwandeln, dass es identisch zu **anotherBoard**
  ist. Dabei machen beide Spieler und Computer die Züge und können dabei die Farben beliebig wählen. Dafür wird das 
  Spielbrett als Graph dargestellt mit Distanzen zwischen den benachbarten Nodes gleich 1, minimale Distanz von jedem Node 
  zu den Spielkomponenten vom Spieler und vom Computer mittels Dijkstra Algorithmus berechnet und die Bedingungen geprüft, 
  die darauf hinweisen, dass es nicht möglich ist, das initiale Spielbrett in höchstens **moves** viele Züge so zu 
  verändern, dass es gleich **anotherBoard** ist. 
- Der Code ist hinreichend kommentiert, was leichtere Navigation durch und Aufbewahrung vom Projekt ermöglicht.