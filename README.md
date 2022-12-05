# Question 6

## Avant la communication
Utiliser le RSA, avant toute communication. 
Le client et le server génèrent 2 clés (une publique et une privées)
Ils s'échangent leurs clé publiques.

![img](https://upload.wikimedia.org/wikipedia/commons/thumb/0/01/Asymmetric_cryptography_-_step_1.svg/800px-Asymmetric_cryptography_-_step_1.svg.png)

## Pendant la communication
Lors qu'un utilisateur veut envoyer un message, il crypte son
message avec la clé pulique de son interlocuteur et lui envoie.
La personne qui reçois le message, le décrypte le message avec
sa propre clé privée.

![img](https://upload.wikimedia.org/wikipedia/commons/thumb/1/11/Asymmetric_cryptography_-_step_2.svg/800px-Asymmetric_cryptography_-_step_2.svg.png)