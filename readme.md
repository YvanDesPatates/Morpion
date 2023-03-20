
# Projet Morpion
### Présentation générale :
<pre>
Ce readme est dédié à la partie sécurité !
Pour un aperçu des fonctionnalités et de l'architecture employé, <a href="./readme_structure_application.md">voir ce readme-ci</a>
</pre>

### Méthode de sécurisation des messages :
<pre>
La sécurisation des données se fait grâce à des échanges de clefs DES encryptés sous RSA :
- Le serveur, à son instanciation, créer un paire de clef RSA
- A chaque nouvelle connexion, le serveur envoies sa clef RSA publique au client
- Le client génère sa clef privé DES, et l'envoie au serveur après l'avoir encrypté avec le clef RSA publique
- Le serveur décrypte la clef DES grâce à sa clef RSA rivé
- les deux entités partagent maintenant la même clef DES privé et peuvent communiquer en toute sécurité
</pre>

### Avantages de cette méthode :
<pre>
- L'utilisation de la clef RSA permet de s'assurer qu'as aucun moment on ne peut intercepter des données,
    et ainsi garantir une sécurité très forte
- L'utilisation du DES permet de gagner en rapidité de lecture et écriture face à une utilisation unique du RSA
</pre>

--------------------------------------------------------------------------------

### Difficultés rencontrées :
##### Conversion des types :
<pre>
L'ancienne version envoyais directement des String d'une socket à l'autre.
Cependant l'encryptage et le décryptage des données se font par des tableaux de bytes.
Des problèmes de conversion m'ont vite fait comprendre qu'il valait mieux envoyer directement des bytes,
    et les convertir uniquement pour l'affichage et le traitement.
</pre>

##### Synchronisation des lectures / écritures :
<pre>
Cette nouvelle méthode de communication présente un défaut :
    Lorsque le serveur envoies deux messages d'affilés, le client à tendance à lire les deux messages en un seul read().

Cela pose des problèmes de synchronisation client / serveur.

Pour résoudre cela j'ai dût normer mon système de communication avec un signal pour signifier la fin d'un message.
De plus j'ai créer mon propre "buffer tampon" qui capture les messages lorsqu'un seul read() en lit plusieurs,
    et les redistribues au prochain read().
</pre>