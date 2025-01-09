# Rapport labo 07 : Chess Game

*Bénédicte Vernet & Benoît Jaouen*

## Introduction

Ce projet propose une implémentation complète d'un jeu d'échecs en Java. Il intègre les mécanismes fondamentaux du jeu ainsi que des règles avancées telles que le petit et le grand roque, la promotion des pions, la prise en passant et la vérification de l'état d'échec. 

## Choix de conception et architecture

### **Structure**
Le projet suit une architecture divisée en plusieurs composants :
- **`engine`** : Contient la logique du jeu, y compris la gestion des pièces, des mouvements et des règles.
- **`chess`** : Définit les interfaces pour le contrôleur et les vues (console et graphique).
- **`views`** : Implémente les interfaces utilisateur, en offrant une version console (`ConsoleView`) et une version graphique (`GUIView`).

### **Architecture**
- **Contrôleur (`ChessGame`)** : Centralise la gestion du jeu et agit comme intermédiaire entre la vue et la logique du jeu.
- **Pièces (`Piece` et ses sous-classes)** : Chaque pièce (roi, dame, fou, etc.) possède une classe dédiée pour gérer ses mouvements spécifiques.
- **Mouvements (`Move`)** : Modélise un mouvement, incluant les cas complexes comme les roques et les prises multiples.
- **Vue (`ChessView`)** : Affiche l’état du jeu et gère les interactions avec l’utilisateur.

### **Conformité aux règles des échecs**
Le code gère les spécificités du jeu, telles que les déplacements autorisés, les règles de promotion, et les conditions d’échec et mat. Les méthodes comme `isThreatened()` et `currentPlayerHasValidMove()` garantissent la validation de l’état du jeu.

## Analyse des fonctionnalités spécifiques

### **Petit et grand roque**
L’implémentation du roque est intégrée dans la méthode `availableMoves()` de la classe `King`. 
- Le roque est autorisé si :
  - Le roi et la tour concernés n'ont pas encore bougé (`hasMoved()`).
  - Les cases entre le roi et la tour sont libres.
  - Aucune des cases traversées par le roi n’est sous menace (`isThreatened()`).
- Lorsqu'un roque est valide, deux mouvements imbriqués sont créés dans l’objet `Move`, grâce à l'attribut `secondMove`, un pour le roi et un pour la tour.

### **Promotion**
La promotion des pions est gérée dans la méthode `promote()` de la classe `ChessGame`.
- Un pion est promu lorsqu’il atteint la dernière rangée (pour les blancs) ou la première rangée (pour les noirs), comme vérifié par `canBePromotedAt()` dans la classe `Pawn`.
- L’utilisateur choisit la pièce de promotion via l'interface.

### **Prise en passant**
La prise en passant est implémentée dans la méthode `addEatMoves()` de la classe `Pawn`.
- La méthode vérifie si le dernier mouvement joué concerne un pion adverse ayant avancé de deux cases depuis sa position initiale.
- Si les conditions sont remplies, un mouvement spécial est ajouté à la liste des mouvements disponibles, avec une référence au pion capturé dans `Move`.

### **Vérification de l’état d’échec**
L’état d’échec est vérifié via la méthode `isThreatened()` de `ChessGame` :
- Elle parcourt toutes les pièces adverses et vérifie si l'une d'elles peut capturer le roi.
- L’état d’échec est utilisé pour bloquer des mouvements illégaux dans `tryMove()` et pour détecter les situations de mat ou de pat.

## Tests effectués

### Tests implémentés

Le projet inclut une série de tests unitaires pour vérifier la robustesse et la conformité de la logique métier du jeu d'échecs. Ces tests couvrent à la fois les fonctionnalités de base et certains scénarios spécifiques. Voici les principaux cas testés :

1. **Initialisation du plateau (`testNewGame`)**  
   - Vérifie que le plateau est correctement initialisé dans sa configuration standard pour une nouvelle partie.
   - Teste également la capacité à configurer un plateau avec une disposition personnalisée des pièces.

2. **Prise en passant (`testPriseEnPassant`)**  
   - Vérifie que la prise en passant fonctionne correctement lorsque les conditions sont réunies (un pion adverse ayant avancé de deux cases est capturé par un pion adjacent).
   - Simule plusieurs tours et compare l'état du plateau attendu après chaque coup avec celui réellement généré.

3. **Vérification générale des états**  
   - La méthode utilitaire `testTurns` est utilisée pour valider les transitions entre les différents états du plateau. Elle compare le plateau attendu et le plateau actuel après chaque mouvement.

#### Résultats des tests unitaires
- Tous les tests unitaires implémentés passent avec succès, ce qui indique que les fonctionnalités couvertes (comme l'initialisation du plateau et la prise en passant) sont correctement implémentées.

### Autres tests
Les tests suivants ont été realisé "à la main" à l'aide de l'interface graphique, en simulant de vraies parties d'echec :

1. **Mouvements de base** :
   - Valider les déplacements de chaque type de pièce (pion, roi, dame, etc.).
   - Vérifier les restrictions, comme les mouvements hors de l’échiquier.
   - Vérifier qu'on ne puisse jouer que si l'autre joueur a joué.

2. **Cas complexes** :
   - Vérifier le bon fonctionnement du petit et du grand roque.
   - Tester la promotion avec chaque type de pièce.
   - Tester la prise en passant dans différents scénarios.

3. **Vérifications d’état** :
   - Valider la détection correcte de l’échec et du mat.
   - Tester les situations de pat.

4. **Tests de bord** :
   - Scénarios où toutes les pièces d’un joueur sont bloquées.

5. **Interface utilisateur** :
   - Tester l’interaction avec l’utilisateur dans les deux interfaces (console et graphique).
