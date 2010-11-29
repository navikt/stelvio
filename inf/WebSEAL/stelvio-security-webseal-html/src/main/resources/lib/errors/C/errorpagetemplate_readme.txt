##################    Pensjonsspesifikke feilsider WebSEAL   ###################  

WebSEAL har en rekke feilsider som fremvises når en feil oppstår på WebSEAL. 
For at disse skal ha likt format som alle andre sider på NAV, er det laget 
en mal for hvordan feilsider på WebSEAL skal se ut -
errorpagetemplate.html. Denne malen skal benyttes i alle WebSEALs feilsider.


#################################################################################
#                          Oppbygging av errortemplate.html                     #
#################################################################################

Feilside-malen for WebSEAL er konstruert ved å benytte følgende html og xhtml-
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
tekniske feilside, for å gjøre feilsiden generell for alle applikasjoner i NAV.
Deler av kode fra PSELV filene som ikke skal benyttes for feilsidene på WebSEAL
er kommentert ut med tag'en "<!-- NOT TO BE USED ON WebSEAL -->".

Feilside-malen benytter seg også av en rekke stylesheets (CSS), JavaScript, og 
bilder (som også er hentet fra PSELV). Disse er lokalisert under 
\www-default\docs\statusressurser\ og mappene "css", "javascript" og "pics".


#################################################################################
#                          Endring av feilsider på WebSEAL                      #
#################################################################################

Dersom det er ønskelig å endre feilsidene må dette gjøres i errorpagetemplate.html, 
og man kan deretter benytte scriptet updateerrorpages.sh til å oppdatere alle 
feilsidene med errorpagetemplate.html.

updateerrorpages.sh benytter tekstfilen errorpages.txt som default for å oppdatere 
feilsidene, men har også muligheten til å ta en annen tekstfil med liste over 
feilsider som skal oppdateres som input (men den må ha samme format som 
errorpages.txt - dvs at feilsidene er listet med navn og filtype (feks 38cf0424.html)
og er separert med en ny linje). errorpages.txt inneholder en liste over alle 
feilsidene som skal oppdateres ved kjøring av updateerrorpages.sh. Dersom det er 
flere feilsider som skal oppdateres ved kjøring av scriptet, må disse legges til 
denne listen. 




