
# Projet Morpion
### Fonctionnalitée :
- permet deux faire des parties de morpions à un contre un
- peut gérer des centaines d'utilisateurs en même temps (enfin probablement)
- gères les erreurs d'entrées utilisateurs
- gères les cas de déconnexions de l'adversaire
- **Nouveau ! vous pouvez maintenant choisir la taille de la grille sur laquelle vous voulez jouer**
- **Nouveau ! vous pouvez maintenant assister à une partie en tant que spectateur**

### Architecture
<pre> voir le diagramme de classe ci-dessous pour une vue d'ensemble.</pre>
###### Game (client)
<pre>
La classe client.Game représente le programme coté client
    Il faut l'installer, ainsi que la classe MainClient, sur l'ordinateur des joueurs afin qu'ils puissent le lancer.
    Elle se connecte au serveur et interprète les résultats envoyés.
</pre>
###### MainServer
<pre>
La classe MainServer, porte d'entrée de l'application serveur, lance la classe Serveur.
</pre>
###### Serveur
<pre>
La class Serveur est une boucle perpétuelle qui attends que des clients se connecte.
    Pour chaque connexion, le Serveur lance un Thread sous la forme d'une instance de la classe de LobbyOptions
</pre>
###### LobbyOptions
<pre>
Chaque instance de la classe LobbyOptions se lance dans un thread à part dès lors qu'on exécute la méthode start().
    Chaque instance de la classe LobbyOptions permet à un nouveau client de s'orienter, 
            soit vers une nouvelle partie soit vers une partie à laquelle assister.
    Lorsque le choix est fait, LobbyOption donne l'information au Lobby
</pre>
###### Lobby
<pre>
Le lobby est une salle d'attente qui lance des joueurs dans une partie (une serveur.GameSession) 
            par groupe de deux dans un Thread A part.
    C'est via la gestion de ces collections de GameSession en cours (attribut games) 
            que le Lobby peut inscrire un nouveau client en temps que viewer d'une partie.
</pre>
###### GameSession
<pre>
Une serveur.GameSession se termine lorsqu'un joueur à gagné ou que la grille (serveur.Plateau) est pleine.
    Chaque serveur.GameSession hérite de la classe Thread et gère :
        - le schéma de communication avec les applications clientes
        - la transmission d'information (victoire, grilles) aux joueurs
</pre>
###### Client
<pre>
La classe serveur.clients.Client se construit autours d'une socket 
    pour faciliter les communications au travers des pipes.
</pre>
###### Joueur
<pre>
La classe serveur.clients.Joueur hérite de la classe Client, 
            elle ajoute des attributs de joueurs tel qu'un pseudo et un symbole ('X' ou 'O')
</pre>
###### Plateau
<pre>
La classe serveur.Plateau contient l'aspect métier : 
    - conditions de victoire quelle que soit la taille de la grille
    - gestion de la grille de morpion
    - affichage de la grille de morpion
</pre>

### Légère précision
<pre>Bien que l'architecture soit capable de gérer toutes les tailles de grilles >= 3x3,
    nous avons restreint ce choix à un maximum de 10x10 pour garder une certaine cohérence.</pre>

### Diagramme de classe en mode esquisse de la partie serveur de l'application
<img src="./diagramme de classe Serveur.png" alt="diagramme de classe">
