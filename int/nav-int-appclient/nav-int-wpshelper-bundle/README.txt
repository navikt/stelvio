***Leveranse av st�tteverkt�y for WPS versjon ${project.version}***

Filen inneholder f�lgende:
* Dokumentasjon av FEM Helper
* Dokumentasjon av BPC Helper
* Dokumentasjon av SiBus Helper
* wpshelpers-<VERSION>.zip
  - FEM Helper
  - BPC Helper
  - localhost_helper.properties
  - SiBus Helper (SibusQueuesDepth.py)
  
  * zip-filen skal pakkes ut p� WPS deployment manager, i katalogen /was_app/tools/esb/ - helperverkt�yene havner da i underkatalogen ${project.build.finalName}.
  * Kontroller at shell-skript er eksekverbare
  
14.11.2008