
# Projet Morpion
### Fonctionnalitée :
- permet deux faire des parties de morpions à un contre un
- peut gérer des centaines d'utilisateurs en même temps (enfin probablement)
- gères les erreurs d'entrées utilisateurs
- gères les cas de déconnexions de l'adversaire

### Architecture
<pre> voir le diagramme de classe ci-dessous pour une vue d'ensemble.</pre>
<pre>
La classe client.Game représente le programme coté client
    Il faut l'installer sur l'ordinateur des joueurs afin qu'ils puissent le lancer.
    Elle se connecte au serveur et interprète les résultats envoyés.
</pre>
<pre>
Le lobby est une salle d'attente qui lance des joueurs dans une partie (une serveur.GameSession)
    par groupe de deux dans un Thread A part.
</pre>
<pre>
Une serveur.GameSession se termine lorsqu'un joueur à gagné ou que la grille (serveur.Plateau) est pleine.
    Chaque serveur.GameSession hérite de la classe Thread et gère :
        - le shéma de communiquation avec les applications clientes
        - la transmission d'information (victoire, grilles) aux joueurs
</pre>
<pre>
La classe serveur.clients.Joueur se construit autours d'une socket 
    pour faciliter les communications au travers des pipes.
</pre>
<pre>
La classe serveur.Plateau contient l'aspect métier : 
    - conditions de victoire
    - gestion de la grille de morpion
    - affichage de la grille de morpion
</pre>
<img src="./diagramme de classe.jpg" alt="diagramme de classe">
