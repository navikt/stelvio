def get():
	return """dmgrHostname=b27apvl073.preprod.local
dmgrUsername=srvWPS
dmgrPassword=${wps-wsadmin-password}
dmgrSOAPPort=8879
envClass=Q

linuxUser=wasadm
linuxPassword=${wps-linux-password}

WPS_EndpointNode1=http://b27apvl074.preprod.local:9080
SoapUI_WSSConfig=UsernameToken srvWPS

scopeName=SS_WPSQ3.AppTarget
scope=cluster
wpsClusterName=SS_WPSQ3.AppTarget
meClusterName=SS_WPSQ3.Messaging
supportClusterName=SS_WPSQ3.Support
cellName=SS_WPSQ3
nodeName=
busName=SCA.APPLICATION.SS_WPSQ3.Bus
busNameCommonEvent=CEI.SS_WPSQ3.BUS
serverName=
foreignBus=MRQ1
foreignBusInfot=MRQ1
foreignBusBrv=MRQ1
foreignBusOppdrag=MRQ1
foreignBusOppdragBatch=MRQ1
foreignBusOppdragAvstem=MRQ1
queuePrefix=Q411
queuePrefixSFE=Q412
queuePrefixTPSDistrib=Q412
queueManager=BQL03

policySetBindings=NAV ESB Arena WSImport Binding,NAV ESB AAReg WSImport Binding
policySetBindingsUsers=GOSYS,${aa.usernametoken-username}
policySetBindingsPasswords=${gosys-usernametoken-password},${aa.usernametoken-password}

jndiNameGsak=ejb/no/nav/gsak/ejb/GSakHome
providerUrlGsak=corbaname:iiop:b27apvl064.preprod.local:9813,iiop:b27apvl065.preprod.local:9813,iiop:b27apvl066.preprod.local:9813

jndiNameBProf=ejb/no/nav/bprof/ejb/BProfHome
providerUrlBProf=corbaname:iiop:b27apvl064.preprod.local:9814,iiop:b27apvl065.preprod.local:9814,iiop:b27apvl066.preprod.local:9814

jndiNameNorg=ejb/no/nav/norg/ejb/NorgHome
providerUrlNorg=corbaname:iiop:b27apvl064.preprod.local:9815,iiop:b27apvl065.preprod.local:9815,iiop:b27apvl066.preprod.local:9815

jndiNameJoark=ejb/no/nav/provider/dok/joark/ejb/JoarkProviderHome
providerUrlJoark=corbaname:iiop:b27apfl016.preprod.local:9811,iiop:b27apfl017.preprod.local:9811,iiop:b27apfl018.preprod.local:9811

jndiNameProvOppService=ejb/no/nav/provider/fellesreg/opptjening/ejb/OpptjeningProviderHome
providerurlProvOppService=corbaname:iiop:b27apvl064.preprod.local:9810, iiop:b27apvl065.preprod.local:9810

jndiNameMedlUnntakServ=MedlemskapsunntakService
providerUrlMedlUnntakServ=corbaname:iiop:b27apvl064.preprod.local:9810, iiop:b27apvl065.preprod.local:9810

jndiVedlikeholdService=ejb/no/nav/provider/pensjon/vedlikehold/ejb/VedlikeholdServiceHome
providerUrlVedlikeholdService=corbaname:iiop:b27apvl064.preprod.local:9810, iiop:b27apvl065.preprod.local:9810

jndiNameInstService=ejb/no/nav/provider/fellesreg/inst/ejb/InstitusjonsoppholdServiceHome
providerUrlInstService=corbaname:iiop:b27apvl064.preprod.local:9810, iiop:b27apvl065.preprod.local:9810

jndiNameForskuddSakService=ejb/no/trygdeetaten/facade/bidrag/ForskuddSakFacadeHome
providerurlForskuddSakService=corbaname:iiop:b27apfl016.preprod.local:9816,iiop:b27apfl017.preprod.local:9816,iiop:b27apfl018.preprod.local:9816

jndiNameSkattOgTrekkService=ejb/no/nav/oppdrag/skattogtrekk/ejb/SkattOgTrekkServiceHome
providerurlSkattOgTrekkService=corbaname:iiop:b27apvl096.preprod.local:9815

jndiNameTjenestepensjonService=TjenestepensjonService
providerurlTjenestepensjonService=corbaname:iiop:b27apvl064.preprod.local:9810, iiop:b27apvl065.preprod.local:9810

jndiNameVedtakService=ejb/no/nav/provider/pensjon/vedtak/ejb/VedtakServiceHome
providerurlVedtakService=corbaname:iiop:b27apvl064.preprod.local:9810, iiop:b27apvl065.preprod.local:9810

jndiNamePensjonssakService=ejb/no/nav/provider/pensjon/sak/ejb/SakServiceHome
providerurlPensjonssakService=corbaname:iiop:b27apvl064.preprod.local:9810, iiop:b27apvl065.preprod.local:9810

jndiNameGrunnlagService=ejb/no/nav/provider/pensjon/grunnlag/ejb/GrunnlagServiceHome
providerurlGrunnlagService=corbaname:iiop:b27apvl064.preprod.local:9810, iiop:b27apvl065.preprod.local:9810

jndiNameKravService=ejb/no/nav/provider/pensjon/krav/ejb/KravServiceHome
providerurlKravService=corbaname:iiop:b27apvl064.preprod.local:9810, iiop:b27apvl065.preprod.local:9810

jndiNameBeregningService=ejb/no/nav/provider/pensjon/beregning/ejb/BeregningServiceHome
providerurlBeregningService=corbaname:iiop:b27apvl064.preprod.local:9810, iiop:b27apvl065.preprod.local:9810

jndiNameBeregningService2011=ejb/no/nav/provider/pensjon/beregning2011/ejb/Beregning2011ServiceHome
providerurlBeregningService2011=corbaname:iiop:b27apvl064.preprod.local:9810, iiop:b27apvl065.preprod.local:9810

jndiNameOppdragService=ejb/no/nav/oppdrag/oppdrag/ejb/OppdragServiceHome
providerurlOppdragService=corbaname:iiop:b27apvl096.preprod.local:9815

jndiNameSamProviderHome=ejb/no/nav/provider/stotte/sam/ejb/SamordningProviderHome
providerurlSamProviderHome=corbaname:iiop:b27apvl064.preprod.local:9810, iiop:b27apvl065.preprod.local:9810

jndiNameSamProcessProviderHome=ejb/no/nav/provider/stotte/sam/ejb/SamordningProcessProviderHome
providerurlSamProcessProviderHome=corbaname:iiop:b27apvl064.preprod.local:9810, iiop:b27apvl065.preprod.local:9810

jndiNameMottaksProvider=MottaksProvider
providerurlMottaksProvider=corbaname:iiop:b27apvl064.preprod.local:9810, iiop:b27apvl065.preprod.local:9810

jndiNameLeveattestService=ejb/no/nav/provider/pensjon/leveattest/ejb/LeveattestServiceHome
providerurlLeveattestService=corbaname:iiop:b27apvl064.preprod.local:9810, iiop:b27apvl065.preprod.local:9810

jndiNamePenSimulering=ejb/no/nav/provider/pensjon/simulering/ejb/SimuleringServiceHome
providerUrlPenSimulering=corbaname:iiop:b27apvl064.preprod.local:9810, iiop:b27apvl065.preprod.local:9810

jndiNameMotVedlikeholdProvider=VedlikeholdsProvider
providerurlMotVedlikeholdProvider=corbaname:iiop:b27apvl064.preprod.local:9810, iiop:b27apvl065.preprod.local:9810

jndiNamePenPersonService=ejb/no/nav/provider/pensjon/penperson/ejb/PenPersonServiceHome
providerUrlPenPersonService=corbaname:iiop:b27apvl064.preprod.local:9810, iiop:b27apvl065.preprod.local:9810

jndiNameSkjemaService=ejb/no/nav/provider/pensjon/skjema/ejb/SkjemaServiceHome
providerurlSkjemaService=corbaname:iiop:b27apvl064.preprod.local:9810, iiop:b27apvl065.preprod.local:9810

jndiNameProvOppBulkService=ejb/no/nav/provider/fellesreg/opptjening/ejb/OpptjeningBulkProviderHome
providerurlProvOppBulkService=corbaname:iiop:b27apvl064.preprod.local:9810, iiop:b27apvl065.preprod.local:9810

jndiNamePenVersion=ejb/no/nav/provider/pensjon/application/ejb/VersionServiceHome
providerUrlPenVersion=corbaname:iiop:b27apvl064.preprod.local:9810, iiop:b27apvl065.preprod.local:9810

FGSAKRunAsValue=REALM=s7338002.trygdeetaten2.local:389#ENDUSER=srvPensjon#CREDENTIAL=Test1234

connUrlCicsUr=tcp://155.55.1.82
portCicsUr=2007
serverNameUr=CICSQ293
connectionTimeoutUr=5
applidQualifierUr=Q3-AppQId-Ur
applidUr=Q3-AppId-Ur
tpnNameUr=BUR1
tranNameUr=BUR1
stuckTimerTimeUr=5
stuckTimeUr=10
stuckThresholdUr=2

connUrlCicsAa=tcp://155.55.1.82
portCicsAa=2007
serverNameCicsAa=CICSQ293
connectionTimeoutAa=5
applidQualifierAa=Q3-AppQId-Aa
applidAa=Q3-AppId-Aa
tpnNameAa=BAA1
tranNameAa=BAA1
stuckTimerTimeAa=5
stuckTimeAa=10
stuckThresholdAa=2

connUrlCicsTss=tcp://155.55.1.82
portCicsTss=2007
serverNameCicsTss=CICSQ415
connectionTimeoutTss=5
applidQualifierTss=Q3-AppQId-Tss
applidTss=Q3-AppId-Tss
tpnNameTss=BTSS
tranNameTss=BTSS
stuckTimerTimeTss=5
stuckTimeTss=10
stuckThresholdTss=2

connUrlCicsInnt=tcp://155.55.1.82
portCicsInnt=2007
serverNameCicsInnt=CICSQ460
connectionTimeoutInnt=5
applidQualifierInnt=Q3-AppQId-Innt
applidInnt=Q3-AppId-Innt
tpnNameInnt=BINN
tranNameInnt=BINN
stuckTimerTimeInnt=5
stuckTimeInnt=10
stuckThresholdInnt=2

connUrlCicsInfot=tcp://155.55.1.82
portCicsInfot=2007
serverNameCicsInfot=CICSQ293
connectionTimeoutInfot=5
applidQualifierInfot=Q3-AppQId-Infot
applidInfot=Q3-AppId-Infot
tpnNameInfot=BINF
tranNameInfot=BINF
stuckTimerTimeInfot=5
stuckTimeInfot=10
stuckThresholdInfot=2

connUrlCicsTps=tcp://155.55.1.82
portCicsTps=2007
serverNameCicsTps=CICSQ411
connectionTimeoutTps=5
applidQualifierTps=Q3-AppQId-Tps
applidTps=Q3-AppId-Tps
tpnNameTps=BTPS
tranNameTps=BTPS
stuckTimerTimeTps=5
stuckTimeTps=10
stuckThresholdTps=2

baseQueueNameInfot=ESB_MEDLEMSKAPSUNNTAK_INFOT_RECEIVE

baseQueueNameInfotPinst001=ESB_INSTITUSJONSOPPHOLD_INFOT_RECEIVE
baseQueueNameInfotPinst002=QA.Q293.IT00_PE_INSTTRANS

baseQueueNameInfotOppgave=LAGRE_OG_FERDIGSTILL_OPPGAVE_RECEIVE

queueNameOppdragReplyBatchMQEXP_RECEIVE_DT=ESB_OS_REPLY_QUE_BATCH
queueNameOppdragOnlineReplyMQEXP_RECEIVE_DT=ESB_OS_REPLY_QUE_PPEN015_ONLINE
queueNameOppdragProcessReplyMQEXP_RECEIVE_DT=ESB_OS_REPLY_QUE_PPEN015_BATCH
queueNameOppdragRequestMQIMP_SEND_DT=QA.Q313.OB04_REQUEST_QUE
queueNameOppdragRequestBatchMQIMP_SEND_DT=QA.Q313.OB04_REQUEST_QUE_PE_BATCH
queueNameOppdragAvstemMQIMP_SEND_DT=QA.Q313.OB04_AVSTEMMING_XML
queueNameOppdragTilbakekreving_RECEIVE_DT=ESB_OS_RECEIVE_TBKEVENT

queueNameInfotOpptjeningK278M408_SEND_DT=QA.Q293.IT00_PE_NY_PENSJMLD_M408_REPLY
queueNameInfotPensjonssakK278M404_SEND_DT=QA.Q293.IT00_PE_NY_PENSJMLD_M404_REPLY
queueNameInfotTjenestepensjonK278M402_SEND_DT=QA.Q293.IT00_PE_NY_PENSJMLD_M402_REPLY
queueNameInfotVedtakK278M403_SEND_DT=QA.Q293.IT00_PE_NY_PENSJMLD_M403_REPLY
queueNameInfotVedtakK278M405_SEND_DT=QA.Q293.IT00_PE_NY_PENSJMLD_M405_REPLY

baseQueueNameBrv=QA.Q475.BREVSERVER_ONLINEBREV_PE01
baseQueueNameBrevkvitteringReply=ESB_BREV_PE01_REPLY

baseQueueNameBisys001Reply=QA.Q460.ESB_REPLY_QUE
baseQueueNameBisys001Recieve=ESB_BBM_FORESPORSEL
queueNameBisysPensjonsinntektK460M364JMSEXP_SEND_DT=QA.Q460.ESB_REPLY_QUE_HENTB_PINNTEKT
queueNameBisysPensjonsinntektK460M364JMSEXP_RECEIVE_DT=ESB_BBM_HENTB_PINNTEKT

ELSAM-TPTILB_TPSamordningVarsling_varsleVedtakNAV_Endpoints=V0_7^3010^https://partner-gw-test.pensjonskassa.no:444/nav/TPSamordningVarslingV0_7|V0_7^3100^https://pts-test.pts.no/axis2/services/TPSamordningVarslingWSEXP_TPSamordningVarslingHttpService|V0_7^3200^https://test1.klp.no/navs-ws-gateway/inbound/tpsamordningvarsling|V0_7^3250^https://test1.klp.no/navs-ws-gateway/inbound/tpsamordningvarsling|V0_7^3100^https://pts-test.pts.no/axis2/services/TPSamordningVarslingWSEXP_TPSamordningVarslingHttpService|V0_7^4095^https://xsg-t.storebrand.no/nav/TPSamordningVarsling|V0_7^3820^https://partner-gw.opf.no/Elsam.svc|V0_7^4071^https://nav-nb.adlab.vital.no/Proxy.NET_WS/Vital.NAV/TPSamordningVarsling.asmx|V0_7^4575^https://www.garantikassen.no:4433/axis2/services/TPSamordningVarslingWSEXP_TPSamordningVarslingHttpService

ELSAM-TPTILB_TPSamordningVarsling_varsleEndringPersonalia_Endpoints=V0_7^3010^https://partner-gw-test.pensjonskassa.no:444/nav/TPSamordningVarslingV0_7|V0_7^3100^https://pts-test.pts.no/axis2/services/TPSamordningVarslingWSEXP_TPSamordningVarslingHttpService|V0_7^3200^https://test1.klp.no/navs-ws-gateway/inbound/tpsamordningvarsling|V0_7^3250^https://test1.klp.no/navs-ws-gateway/inbound/tpsamordningvarsling|V0_7^3100^https://pts-test.pts.no/axis2/services/TPSamordningVarslingWSEXP_TPSamordningVarslingHttpService|V0_7^4095^https://xsg-t.storebrand.no/nav/TPSamordningVarsling|V0_7^3820^https://partner-gw.opf.no/Elsam.svc|V0_7^4071^https://nav-nb.adlab.vital.no/Proxy.NET_WS/Vital.NAV/TPSamordningVarsling.asmx|V0_7^4575^https://www.garantikassen.no:4433/axis2/services/TPSamordningVarslingWSEXP_TPSamordningVarslingHttpService

ELSAM-TPTILB_TPSamordningVarsling_varsleManglendeRefusjonskrav_Endpoints=V0_7^3010^https://partner-gw-test.pensjonskassa.no:444/nav/TPSamordningVarslingV0_7|V0_7^3100^https://pts-test.pts.no/axis2/services/TPSamordningVarslingWSEXP_TPSamordningVarslingHttpService|V0_7^3200^https://test1.klp.no/navs-ws-gateway/inbound/tpsamordningvarsling|V0_7^3250^https://test1.klp.no/navs-ws-gateway/inbound/tpsamordningvarsling|V0_7^3100^https://pts-test.pts.no/axis2/services/TPSamordningVarslingWSEXP_TPSamordningVarslingHttpService|V0_7^4095^https://xsg-t.storebrand.no/nav/TPSamordningVarsling|V0_7^3820^https://partner-gw.opf.no/Elsam.svc|V0_7^4071^https://nav-nb.adlab.vital.no/Proxy.NET_WS/Vital.NAV/TPSamordningVarsling.asmx|V0_7^4575^https://www.garantikassen.no:4433/axis2/services/TPSamordningVarslingWSEXP_TPSamordningVarslingHttpService

ELSAM-TPTILB_TPSamordningVarsling_varsleEndringTPYtelse_Endpoints=V0_7^3010^https://partner-gw-test.pensjonskassa.no:444/nav/TPSamordningVarslingV0_7|V0_7^3100^https://pts-test.pts.no/axis2/services/TPSamordningVarslingWSEXP_TPSamordningVarslingHttpService|V0_7^3200^https://test1.klp.no/navs-ws-gateway/inbound/tpsamordningvarsling|V0_7^3250^https://test1.klp.no/navs-ws-gateway/inbound/tpsamordningvarsling|V0_7^3100^https://pts-test.pts.no/axis2/services/TPSamordningVarslingWSEXP_TPSamordningVarslingHttpService|V0_7^4095^https://xsg-t.storebrand.no/nav/TPSamordningVarsling|V0_7^3820^https://partner-gw.opf.no/Elsam.svc|V0_7^4071^https://nav-nb.adlab.vital.no/Proxy.NET_WS/Vital.NAV/TPSamordningVarsling.asmx|V0_7^4575^https://www.garantikassen.no:4433/axis2/services/TPSamordningVarslingWSEXP_TPSamordningVarslingHttpService

ELSAM-TPTILB_TPSamordningVarsling_Endpoints=V0_7^3010^https://partner-gw-test.pensjonskassa.no:444/nav/TPSamordningVarslingV0_7|V0_7^3100^https://pts-test.pts.no/axis2/services/TPSamordningVarslingWSEXP_TPSamordningVarslingHttpService|V0_7^3200^https://test1.klp.no/navs-ws-gateway/inbound/tpsamordningvarsling|V0_7^3250^https://test1.klp.no/navs-ws-gateway/inbound/tpsamordningvarsling|V0_7^3100^https://pts-test.pts.no/axis2/services/TPSamordningVarslingWSEXP_TPSamordningVarslingHttpService|V0_7^4095^https://xsg-t.storebrand.no/nav/TPSamordningVarsling|V0_7^3820^https://partner-gw.opf.no/Elsam.svc|V0_7^4071^https://nav-nb.adlab.vital.no/Proxy.NET_WS/Vital.NAV/TPSamordningVarsling.asmx|V0_7^4575^https://www.garantikassen.no:4433/axis2/services/TPSamordningVarslingWSEXP_TPSamordningVarslingHttpService

ELSAM-TPTILB_SimulereTjenestepensjon_hentStillingsprosentListe_Endpoints=V0_2^3010^https://partner-gw-test.pensjonskassa.no:444/nav/SimulereTjenestepensjon|V0_2^3200^https://test1.klp.no/navs-ws-gateway/inbound/simuleretjenestepensjon|V0_2^3250^https://test1.klp.no/navs-ws-gateway/inbound/simuleretjenestepensjon|V0_2^4071^https://nav-nb.adlab.vital.no/Proxy.NET_WS/Vital.NAV/SimulerTjenestepensjon.asmx|V0_2^4095^https://xsg-t.storebrand.no/nav/SimulerTP

ELSAM-TPTILB_SimulereTjenestepensjon_simulerOffentligTjenestepensjon_Endpoints=V0_2^3010^https://partner-gw-test.pensjonskassa.no:444/nav/SimulereTjenestepensjon|V0_2^3200^https://test1.klp.no/navs-ws-gateway/inbound/simuleretjenestepensjon|V0_2^3250^https://test1.klp.no/navs-ws-gateway/inbound/simuleretjenestepensjon|V0_2^4071^https://nav-nb.adlab.vital.no/Proxy.NET_WS/Vital.NAV/SimulerTjenestepensjon.asmx|V0_2^4095^https://xsg-t.storebrand.no/nav/SimulerTP

Samhandler-hentEksternTilkoblingsListe_SOTP=80000470761^SOTP^ESBEKS_008^SimulerOffentligTjenestepensjon^OK^2009-01-28|80000470767^SOTP^ESBEKS_008^SimulerOffentligTjenestepensjon^OK^2009-01-28
"""