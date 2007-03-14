package no.stelvio.service.star.example.codestable;

import javax.ejb.EJBLocalObject;

import no.stelvio.common.codestable.factory.CodesTableItemsFactory;

/**
 * Local interface for no.stelvio.service.star.example.codestable.CodesTableFactoryBean bean.
 */
public interface CodesTableFactoryLocal extends CodesTableItemsFactory, EJBLocalObject {
}
