# rosters

Algoritmi (classic):
 - Pelaajat järjestetään paremmuusjärjestykseen. Parilliset ja parittomat pelaajat eri puolille.
 - Tasatilanteessa ratkaisee ilmottautumisjärjestys.
 - Joukkueen pelaajat järjestetään aakkosjärjestykseen.
 - Tepin joukkue saa aina valkoiset paidat.

Algoritmi (fair):
 - Joukkueiden kaksi parasta pelaajaa vaihdetaan keskenään. Muuten sama kuin 'classic'

Satunnaisuus:
 - Jakoihin voi lisätä satunnaisuutta syöteparametrilla "-r" tai "--random", jolloin liki samantasoiset pelaajat arvotaan satunnaiseen järjestykseen.
 - Satunnaisuutta voi säätää antamalla pelaajien voimasuhteiden erotuksen maksimin syöteparametrina (oletusarvo on viisi yksikköä).

Debug -tila:
 - Ohjelman voi suorittaa debug-tilassa syöteparametrilla "-d" tai "--debug", jolloin ohjelma lisää välitulosteita.

Tiedostot:
 - players.txt [INPUT] = vuorolle ilmoittauneet pelaajat.
 - ranking.txt [INPUT] = pelaajien voimasuhteet asteikolla 0-100. Voimasuhde annetaan avain-arvo parina "nimimerkki:kokonaisuluku".
 - rosters.txt [OUTPUT] = joukkueiden kokoonpanot.

Pelaajan nimimerkkiä koskevat rajoitukset:
 - Pelaaja tunnistetaan nimenhuudon nimimerkillä.
 - Molemmissa syötetiedostoissa nimimerkki on kirjoitettava samalla tavalla. Kopioi nimimerkki suoraan nimenhuuto.com palvelusta.
 - Huomioi siis nimimerkeissä isot ja pienet kirjaimet sekä mahdolliset välilyönnit.

Tiedostoja koskevat rajoitukset:
 - players.txt voi sisältää tyhjiä rivejä.
 - ranking.txt voi sisältää tyhjiä rivejä tai kommenttirivejä. Kommenttirivi alkaa '#' merkillä.
