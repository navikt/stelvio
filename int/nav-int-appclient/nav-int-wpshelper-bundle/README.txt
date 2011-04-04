***Leveranse av støtteverktøy for WPS versjon ${project.version}***

* Zip-filen skal pakkes ut på WPS node1, i katalogen som angitt i dokumentasjon; /was_app/tools/esb/wpshelpers.
  Helperverktøyene havner da i underkatalogen ${project.build.finalName}.
* Kontroller at shell-skript er eksekverbare


Opprettet katalog /was_app/tools/esb/wpshelpers/${project.build.finalName} har igjen følgende innhold:
* Dokumentasjon
** Fellesdokumentasjon av WPS Helpers
** Dokumentasjon av FEM Helper
** Dokumentasjon av BPC Helper
** Dokumentasjon av SIBus Helper
** Dokumentasjon av SIBus-kødybdescript
* FEM Helper
* BPC Helper
* SIBus Helper
* SibusQueuesDepth.py
* localhost_helper.properties
* Shell-script som pakker inn normale kjøremønstre. Disse fungerer også som eksempler for nye script som opprettes av drift ved behov.

04.04.2011