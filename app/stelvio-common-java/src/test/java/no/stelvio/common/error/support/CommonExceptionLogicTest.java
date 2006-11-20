package no.stelvio.common.error.support;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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

        assertEquals(copy.getErrorId(), cel.getErrorId(), "Error id not copied correctly");
        assertEquals(copy.getUserId(), cel.getUserId(), "User id not copied correctly");
        assertEquals(copy.getScreenId(), cel.getScreenId(), "Screen id not copied correctly");
        assertEquals(copy.getProcessId(), cel.getProcessId(), "Process id not copied correctly");
        assertEquals(copy.getTransactionId(), cel.getTransactionId(), "Transaction id not copied correctly");
        assertEquals(copy.getTemplateArguments(), cel.getTemplateArguments(), "Arguments not copied correctly");
        assertEquals(copy.isLogged(), cel.isLogged(), "Logged not copied correctly");
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

        assertEquals(argCopy[0], "arg1", "Arguments not copied safely");        
    }

    @Test
    public void argumentsCopiedAreEqualToSource() {
        Object[] argCopy = cel.getTemplateArguments();

        assertEquals(argCopy[0], arguments[0], "Class argument not copied correctly");
        assertEquals(argCopy[1], arguments[1], "double argument not copied correctly");
        assertEquals(argCopy[2], arguments[2], "String argument not copied correctly");
    }

    @Test
    public void canSetThatAnExceptionIsLogged() {
        assertEquals(false, cel.isLogged(), "Should not be set logged");
        cel.setLogged();
        assertEquals(true, cel.isLogged(), "Should be set logged");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void exceptionIsThrownWhenArgsIsNull() {
        createDummy("test");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void exceptionIsThrownWhenArgsIsEmpty() {
        createDummy("test", new Object[] {});
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void exceptionIsThrownWhenMessageTemplateIsNull() {
        createDummy(null, arguments);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void exceptionIsThrownWhenMessageTemplateIsEmpty() {
        createDummy("", arguments);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void exceptionIsThrownWhenNoArgsInTemplate() {
        createDummy("test", arguments);
    }

    /**
     * Number of arguments in the template and sent to the constructor cannot be different.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void exceptionIsThrownWhenNumOfArgsAreDifferent() {
        createDummy("test{0}", arguments);
    }

    @Test
    public void messageIsGeneratedFromTemplateAndArgs() {
        CommonExceptionLogic cel = createDummy("nr1:{0},nr2:{1,number,integer},nr3:{2}", arguments);
        assertEquals(cel.getMessage(), "nr1:class java.lang.String,nr2:1,nr3:arg", "Not the correct messageFrom;");
    }

    @BeforeMethod
    public void createArguments() {
        arguments = new Object[] {String.class, 1.1, "arg"};
        cel = new DefaultCommonExceptionLogic(arguments);
    }


    private CommonExceptionLogic createDummy(final String messageTemplate, Object... arguments) {
        return new CommonExceptionLogic(arguments) {
            protected String getMessageTemplate() {
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

        protected String getMessageTemplate() {
            return "{0}{1}{2}";
        }
    }
}
