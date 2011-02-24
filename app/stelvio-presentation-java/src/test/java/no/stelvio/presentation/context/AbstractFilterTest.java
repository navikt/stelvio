package no.stelvio.presentation.context;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * Abstract class for unit tests of ServletFilters.
 * 
 * @author person983601e0e117 (Accenture)
 * 
 */
public abstract class AbstractFilterTest {

	private Mockery context = new Mockery();

	/**
	 * Executes a filter in a simulator with a supplied filter chain.
	 * 
	 * @param filter
	 *            filter
	 * @param request
	 *            request
	 * @param chain
	 *            a filterchain
	 * @param filterName
	 *            name of the filter, this param is optional, will be set in the filterconfig if not supplied, default value is
	 *            'no name'
	 * @return response
	 * @throws ServletException
	 *             servlet exception
	 * @throws IOException
	 *             exception
	 */
	protected MockHttpServletResponse executeFilterSimulator(Filter filter, ServletRequest request, FilterChain chain,
			String... filterName) throws ServletException, IOException {

		String nameOfFilter = filterName.length == 0 ? "no name" : filterName[0];

		filter.init(new MockFilterConfig(nameOfFilter));

		MockHttpServletResponse response = new MockHttpServletResponse();

		filter.doFilter(request, response, chain);

		filter.destroy();

		return response;
	}

	/**
	 * Simulates a filter with a mocked filterchain.
	 * 
	 * @param filter
	 *            filter
	 * @param request
	 *            request
	 * @param expectChainToProceed
	 *            indicates whether proceed from the filter is expected
	 * @param filterName
	 *            name of the filter, this param is optional, will be set in the filterconfig if not supplied default value is
	 *            'no name'
	 * @return response
	 * @throws ServletException
	 *             servlet exception
	 * @throws IOException
	 *             exception
	 */
	protected MockHttpServletResponse executeFilterSimulator(Filter filter, ServletRequest request,
			boolean expectChainToProceed, String... filterName) throws ServletException, IOException {
		FilterChain chain = createFilterChain(expectChainToProceed);

		String nameOfFilter = filterName.length == 0 ? "no name" : filterName[0];

		filter.init(new MockFilterConfig(nameOfFilter));

		MockHttpServletResponse response = new MockHttpServletResponse();

		filter.doFilter(request, response, chain);

		filter.destroy();

		return response;

	}

	/**
	 * Create a filter chain.
	 * 
	 * @param expectChainToProceed
	 *            chain
	 * @return filter chain
	 * @throws ServletException
	 *             servlet exception
	 * @throws IOException
	 *             exception
	 */
	private FilterChain createFilterChain(boolean expectChainToProceed) throws IOException, ServletException {
		final FilterChain chain = context.mock(FilterChain.class);
		if (expectChainToProceed) {
			context.checking(new Expectations() {
				{
					allowing(chain).doFilter(with(aNonNull(ServletRequest.class)), with(aNonNull(ServletResponse.class)));
				}
			});
		} else {
			context.checking(new Expectations() {
				{
					never(chain).doFilter(with(aNonNull(ServletRequest.class)), with(aNonNull(ServletResponse.class)));
				}
			});
		}
		return chain;
	}

}
