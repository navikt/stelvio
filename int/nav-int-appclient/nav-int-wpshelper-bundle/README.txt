***Leveranse av st�tteverkt�y for WPS versjon ${project.version}***

* Zip-filen skal pakkes ut p� WPS deployment manager, i katalogen som angitt i dokumentasjon; /was_app/tools/esb/wpshelpers.
  Helperverkt�yene havner da i underkatalogen ${project.build.finalName}.
* Kontroller at shell-skript er eksekverbare


Opprettet katalog /was_app/tools/esb/wpshelpers/${project.build.finalName} har igjen f�lgende innhold:
* Dokumentasjon
** Fellesdokumentasjon av WPS Helpers
** Dokumentasjon av FEM Helper
** Dokumentasjon av BPC Helper
** Dokumentasjon av SIBus Helper
** Dokumentasjon av SIBus-k�dybdescript
* FEM Helper
* BPC Helper
* SIBus Helper
* SibusQueuesDepth.py
* localhost_helper.properties
* Shell-script som pakker inn normale kj�rem�nstre. Disse fungerer ogs� som eksempler for nye script som opprettes av drift ved behov.

12.12.2008