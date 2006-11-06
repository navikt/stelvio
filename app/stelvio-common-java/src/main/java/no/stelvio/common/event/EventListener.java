package no.stelvio.common.event;

import org.springframework.context.ApplicationListener;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo should it extend AppListener? Then it must implement onApplicationEvent --> not correct naming?
 * @todo is the class name ok? should not start with the same as the package -> could be ApplicationListener
 */
public interface EventListener extends ApplicationListener {
}
