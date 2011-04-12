// Stub class generated by rmic, do not edit.
// Contents subject to change without notice.

package org.omg.stub.javax.ejb;

import java.lang.String;
import java.lang.Throwable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import javax.ejb.EJBHome;
import javax.ejb.HomeHandle;
import javax.rmi.CORBA.Stub;
import javax.rmi.CORBA.Util;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.RemarshalException;
import org.omg.CORBA.portable.ServantObject;

public class _HomeHandle_Stub extends Stub implements HomeHandle,
Remote {
    
    private static final String[] _type_ids = {
        "RMI:javax.ejb.HomeHandle:0000000000000000"
    };
    
    public String[] _ids() { 
        return _type_ids;
    }
    
    public EJBHome getEJBHome() throws RemoteException {
        while(true) {
            if (!Util.isLocal(this)) {
                InputStream in = null;
                try {
                    try {
                        OutputStream out = _request("_get_EJBHome", true);
                        in = _invoke(out);
                        return (EJBHome) in.read_Object(EJBHome.class);
                    } catch (ApplicationException ex) {
                        in = ex.getInputStream();
                        String id = in.read_string();
                        throw new UnexpectedException(id);
                    } catch (RemarshalException ex) {
                        continue;
                    }
                } catch (SystemException ex) {
                    throw Util.mapSystemException(ex);
                } finally {
                    _releaseReply(in);
                }
            } else {
                ServantObject so = _servant_preinvoke("_get_EJBHome",javax.ejb.HomeHandle.class);
                if (so == null) {
                    continue;
                }
                try {
                    EJBHome result = ((javax.ejb.HomeHandle)so.servant).getEJBHome();
                    return (EJBHome)Util.copyObject(result,_orb());
                } catch (Throwable ex) {
                    Throwable exCopy = (Throwable)Util.copyObject(ex,_orb());
                    throw Util.wrapException(exCopy);
                } finally {
                    _servant_postinvoke(so);
                }
            }
        }
    }
}
