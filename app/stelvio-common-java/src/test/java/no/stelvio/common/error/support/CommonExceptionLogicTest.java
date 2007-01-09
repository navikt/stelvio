package no.stelvio.common.error.support;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.eq;
import static org.hamcrest.core.IsEqual.isFalse;
import static org.hamcrest.core.IsEqual.isTrue;
import org.junit.Before;
import org.junit.Test;


/**
 * Unit test for {@link CommonExceptionLogic}.
 *
 * @author personf8e9850ed756
 */
public class CommonExceptionLogicTest {
    private Object[] arguments;
    private CommonExceptionLogic cel;

    @Test
    public void everyFieldIsCopiedByCopyConstructor() {
        CommonExceptionLogic copy = new DefaultCommonExceptionLogic(cel);

        assertThat(copy.getErrorId(), eq(cel.getErrorId()));
        assertThat(copy.getUserId(), eq(cel.getUserId()));
        assertThat(copy.getScreenId(), eq(cel.getScreenId()));
        assertThat(copy.getProcessId(), eq(cel.getProcessId()));
        assertThat(copy.getTransactionId(), eq(cel.getTransactionId()));
        assertThat(copy.getTemplateArguments(), eq(cel.getTemplateArguments()));
        assertThat(copy.isLogged(), eq(cel.isLogged()));
    }

    /**
     * Arguments as input to constructor should be copied, not just hold onto the reference.
     */
    @Test
    public void argumentsAreCopiedSafely() {
        Object[] arguments = new Object[]{"arg1", "arg2", "arg3"};
        CommonExceptionLogic cel = new DefaultCommonExceptionLogic(arguments);

        arguments[0] = "changed";
        Object[] argCopy = cel.getTemplateArguments();

        assertThat((String) argCopy[0], eq("arg1"));
    }

    @Test
    public void argumentsCopiedAreEqualToSource() {
        Object[] argCopy = cel.getTemplateArguments();

        assertThat(argCopy[0], eq(arguments[0]));
        assertThat(argCopy[1], eq(arguments[1]));
        assertThat(argCopy[2], eq(arguments[2]));
    }

    @Test
    public void canSetThatAnExceptionIsLogged() {
        assertThat(cel.isLogged(), isFalse());
        cel.setLogged();
        assertThat(cel.isLogged(), isTrue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionIsThrownWhenArgsIsNull() {
        createDummy("test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionIsThrownWhenArgsIsEmpty() {
        createDummy("test", new Object[] {});
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionIsThrownWhenMessageTemplateIsNull() {
        createDummy(null, arguments);
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionIsThrownWhenMessageTemplateIsEmpty() {
        createDummy("", arguments);
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionIsThrownWhenNoArgsInTemplate() {
        createDummy("test", arguments);
    }

    /**
     * Number of arguments in the template and sent to the constructor cannot be different.
     */
    @Test(expected = IllegalArgumentException.class)
    public void exceptionIsThrownWhenNumOfArgsAreDifferent() {
        createDummy("test{0}", arguments);
    }

    @Test
    public void messageIsGeneratedFromTemplateAndArgs() {
        CommonExceptionLogic cel = createDummy("nr1:{0},nr2:{1,number,integer},nr3:{2}", arguments);
        assertThat(cel.getMessage(), eq("nr1:class java.lang.String,nr2:1,nr3:arg"));
    }

    @Before
    public void createArguments() {
        arguments = new Object[] {String.class, 1.1, "arg"};
        cel = new DefaultCommonExceptionLogic(arguments);
    }


    private CommonExceptionLogic createDummy(final String messageTemplate, Object... arguments) {
        return new CommonExceptionLogic(arguments) {
            protected String messageTemplate(final int numArgs) {
                return messageTemplate;
            }
        };
    }

    private static class DefaultCommonExceptionLogic extends CommonExceptionLogic {
        public DefaultCommonExceptionLogic(Object[] templateArguments) {
            super(templateArguments);
        }

        public DefaultCommonExceptionLogic(CommonExceptionLogic cel) {
            super(cel);
        }

        protected String messageTemplate(final int numArgs) {
            return "{0}{1}{2}";
        }
    }
}
