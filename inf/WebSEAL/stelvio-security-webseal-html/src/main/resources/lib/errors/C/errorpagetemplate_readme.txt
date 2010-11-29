##################    Pensjonsspesifikke feilsider WebSEAL   ###################  

WebSEAL har en rekke feilsider som fremvises n�r en feil oppst�r p� WebSEAL. 
For at disse skal ha likt format som alle andre sider p� NAV, er det laget 
en mal for hvordan feilsider p� WebSEAL skal se ut -
errorpagetemplate.html. Denne malen skal benyttes i alle WebSEALs feilsider.


#################################################################################
#                          Oppbygging av errortemplate.html                     #
#################################################################################

Feilside-malen for WebSEAL er konstruert ved � benytte f�lgende html og xhtml-
sider fra Pensjonprosjektets applikasjon PSELV:
- ramme-header.html
- ramme.html
- nav-common-header.xhtml
- nav-common-top-menu.xhtml
- ramme-brukerinfo-menu.xhtml
- tekniskfeilside.xhtml
- tekniskfeilside-layout.xhtml
- nav-common-footer.xhtml

Hvilke deler i feilside-malen som er hentet fra PSELV sine filer er tydelig 
markert i errorpagetemplate.html, med "******* START <filename> ********" og
"******* END <filename> ********" tag'er.

Det er gjort noen endringer i errorpagetemplate.html i forhold til PSELV sin
tekniske feilside, for � gj�re feilsiden generell for alle applikasjoner i NAV.
Deler av kode fra PSELV filene som ikke skal benyttes for feilsidene p� WebSEAL
er kommentert ut med tag'en "<!-- NOT TO BE USED ON WebSEAL -->".

Feilside-malen benytter seg ogs� av en rekke stylesheets (CSS), JavaScript, og 
bilder (som ogs� er hentet fra PSELV). Disse er lokalisert under 
\www-default\docs\statusressurser\ og mappene "css", "javascript" og "pics".


#################################################################################
#                          Endring av feilsider p� WebSEAL                      #
#################################################################################

Dersom det er �nskelig � endre feilsidene m� dette gj�res i errorpagetemplate.html, 
og man kan deretter benytte scriptet updateerrorpages.sh til � oppdatere alle 
feilsidene med errorpagetemplate.html.

updateerrorpages.sh benytter tekstfilen errorpages.txt som default for � oppdatere 
feilsidene, men har ogs� muligheten til � ta en annen tekstfil med liste over 
feilsider som skal oppdateres som input (men den m� ha samme format som 
errorpages.txt - dvs at feilsidene er listet med navn og filtype (feks 38cf0424.html)
og er separert med en ny linje). errorpages.txt inneholder en liste over alle 
feilsidene som skal oppdateres ved kj�ring av updateerrorpages.sh. Dersom det er 
flere feilsider som skal oppdateres ved kj�ring av scriptet, m� disse legges til 
denne listen. 




