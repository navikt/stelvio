// Decompiled by DJ v3.2.2.67 Copyright 2002 Atanas Neshkov  Date: 14.11.2007 08:29:58
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   InstitusjonsoppholdService.java

package no.nav.provider.fellesreg.inst.ejb;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import no.nav.provider.fellesreg.inst.InstitusjonsoppholdDoesNotExistExceptionDto;
import no.nav.provider.fellesreg.inst.PersonDoesNotExistExceptionDto;
import no.nav.provider.fellesreg.inst.to.*;

public interface InstitusjonsoppholdService
    extends EJBObject
{

    public abstract LagreInstitusjonsoppholdResponseDto lagreInstitusjonsopphold(LagreInstitusjonsoppholdRequestDto lagreinstitusjonsoppholdrequestdto)
        throws RemoteException, InstitusjonsoppholdDoesNotExistExceptionDto, PersonDoesNotExistExceptionDto;

    public abstract HentInstitusjonsoppholdListeResponseDto hentInstitusjonsoppholdListe(HentInstitusjonsoppholdListeRequestDto hentinstitusjonsoppholdlisterequestdto)
        throws RemoteException, PersonDoesNotExistExceptionDto;

    public abstract void slettInstitusjonsopphold(SlettInstitusjonsoppholdRequestDto slettinstitusjonsoppholdrequestdto)
        throws RemoteException, InstitusjonsoppholdDoesNotExistExceptionDto;

    public abstract AldersOgSykehjemsListeResponseDto aldersOgSykehjemsliste(AldersOgSykehjemsListeRequestDto aldersogsykehjemslisterequestdto)
        throws RemoteException;

    public abstract void oppdaterFodselsnummer(OppdaterFodselsnummerRequestDto oppdaterfodselsnummerrequestdto)
        throws RemoteException;

    public abstract void opprettFodselsnummer(OpprettFodselsnummerRequestDto opprettfodselsnummerrequestdto)
        throws RemoteException;
}