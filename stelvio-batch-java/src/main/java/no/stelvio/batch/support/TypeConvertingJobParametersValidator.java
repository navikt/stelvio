package no.stelvio.batch.support;

import static java.util.stream.Collectors.toMap;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameter.ParameterType;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.InitializingBean;

/**
 * A job parameter validator that validates type conversion from string input to configured types. It delegates validation of
 * required and optional parameters to {@link DefaultJobParametersValidator}. Implements
 * {@link JobExecutionListener#beforeJob(JobExecution)} in order to put validated parameters on the job execution context.
 *
 */
public class TypeConvertingJobParametersValidator extends JobExecutionListenerSupport implements JobParametersValidator,
        InitializingBean {
    private DefaultJobParametersValidator defaultValidator;
    private List<? extends StringJobParameter> requiredParameters = new ArrayList<>();
    private List<? extends StringJobParameter> optionalParameters = new ArrayList<>();
    private Map<String, StringJobParameter> allParameters;
    private ExecutionContext jobExecutionContext;

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        defaultValidator.validate(parameters);
        validateTypeConversions(parameters);
    }

    private void validateTypeConversions(JobParameters parameters) throws JobParametersInvalidException {
        Map<String, JobParameter> jobParameters = parameters.getParameters();
        for (Entry<String, JobParameter> jobParameter : jobParameters.entrySet()) {
            validateParameter(jobParameter);
        }
    }

    private void validateParameter(Entry<String, JobParameter> jobParameter) throws JobParametersInvalidException {
        StringJobParameter parameter = allParameters.get(jobParameter.getKey());
        if (jobParameter.getValue().getType() != ParameterType.STRING) {
            throw new JobParametersInvalidException("The input JobParameters must be of type String");
        }
        parameter.validateTypeConversion((String) jobParameter.getValue().getValue());
    }

    /**
     * Base class for type conversion validation.
     *
     *
     */
    public static abstract class StringJobParameter {
        private final String key;

        public StringJobParameter(String key) {
            this.key = key;
        }

        public abstract void validateTypeConversion(String jobParameterValue) throws JobParametersInvalidException;

        /**
         * @return the key
         */
        public String getKey() {
            return key;
        }

        public abstract Object getValue();
    }

    /**
     * Defines type conversion from string to string for a job parameter.
     *
     *
     */
    public static class StringStringJobParameter extends StringJobParameter {
        private String value;

        public StringStringJobParameter(String key) {
            super(key);
        }

        @Override
        public void validateTypeConversion(String jobParameterValue) throws JobParametersInvalidException {
            this.value = jobParameterValue;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    /**
     * Defines type conversion from string to File for a job parameter.
     * Checks if inputString is a file, if not throws {@link JobParametersInvalidException}
     *
     *
     */
    public static class StringFileJobParameter extends StringJobParameter {
        private String fileLocation;

        public StringFileJobParameter(String key) {
            super(key);
        }

        @Override
        public void validateTypeConversion(String jobParameterValue) throws JobParametersInvalidException {
            this.fileLocation = jobParameterValue;
            File file = new File(fileLocation);
            if(file.isDirectory()) {
                throw new JobParametersInvalidException("Jobparameter " + jobParameterValue + " is not a file");
            }
        }

        @Override
        public Object getValue() {
            return fileLocation;
        }

    }

    /**
     * Defines type conversion from string to date for a job parameter.
     *
     *
     */
    public static class StringDateJobParameter extends StringJobParameter {
        private final DateFormat dateformat;
        private Date value;

        public StringDateJobParameter(String key, String pattern) {
            super(key);
            this.dateformat = new SimpleDateFormat(pattern);
            dateformat.setLenient(false);
        }

        @Override
        public void validateTypeConversion(String jobParameterValue) throws JobParametersInvalidException {
            try {
                this.value = dateformat.parse(jobParameterValue);
                if (!dateformat.format(getValue()).equals(jobParameterValue)) {
                    throwValidationException(jobParameterValue);
                }
            } catch (ParseException e) {
                throwValidationException(jobParameterValue);
            }
        }

        private void throwValidationException(String jobParameterValue) throws JobParametersInvalidException {
            throw new JobParametersInvalidException("Invalid date input for parameter:"
                    + getKey() + ", value=" + jobParameterValue);
        }

        @Override
        public Date getValue() {
            return value;
        }
    }

    /**
     * Defines type conversion from string to long for a job parameter.
     *
     *
     */
    public static class StringLongJobParameter extends StringJobParameter {
        private long value;

        public StringLongJobParameter(String key) {
            super(key);
        }

        @Override
        public void validateTypeConversion(String jobParameterValue) throws JobParametersInvalidException {
            try {
                this.value = Long.parseLong(jobParameterValue);
            } catch (NumberFormatException e) {
                throw new JobParametersInvalidException("Invalid long input for parameter:" + getKey());
            }
        }

        @Override
        public Long getValue() {
            return value;
        }
    }

    public static class StringShortJobParameter extends StringJobParameter {
        private Short value;

        public StringShortJobParameter(String key) {
            super(key);
        }

        @Override
        public void validateTypeConversion(String jobParameterValue) throws JobParametersInvalidException {
            try {
                this.value = Short.parseShort(jobParameterValue);
            } catch (NumberFormatException e) {
                throw new JobParametersInvalidException("Invalid short input for parameter:" + getKey());
            }
        }

        @Override
        public Short getValue() {
            return value;
        }
    }

    /**
     * Defines type conversion from string to boolean for a job parameter.
     *
     *
     */
    public static class StringBooleanJobParameter extends StringJobParameter {
        private boolean value;

        public StringBooleanJobParameter(String key) {
            super(key);
        }

        @Override
        public void validateTypeConversion(String jobParameterValue) throws JobParametersInvalidException {
            if (!isBooleanString(jobParameterValue)) {
                throw new JobParametersInvalidException("Invalid boolean input for parameter:" + getKey());
            }
            this.value = Boolean.parseBoolean(jobParameterValue);
        }

        private boolean isBooleanString(String jobParameterValue) {
            return jobParameterValue.equalsIgnoreCase(Boolean.TRUE.toString())
                    || jobParameterValue.equalsIgnoreCase(Boolean.FALSE.toString());
        }

        @Override
        public Boolean getValue() {
            return value;
        }
    }

    @SuppressWarnings("unchecked")
    public static class StringEnumJobParameter extends StringJobParameter {
        private Enum value;
        private Class enumType;

        public StringEnumJobParameter(String key, Class enumType) {
            super(key);
            this.enumType = enumType;
        }

        @Override
        public void validateTypeConversion(String jobParameterValue) throws JobParametersInvalidException {
            try {
                this.value = Enum.valueOf(enumType, jobParameterValue);
            } catch (IllegalArgumentException e) {
                throw new JobParametersInvalidException("Invalid Enum input for parameter:" + getKey());
            }
        }

        @Override
        public Enum getValue() {
            return value;
        }
    }

    /**
     * Defines type conversion from string to URI for a job parameter. Note that the URI is set as a String on the jobContext.
     *
     *
     */
    public static class StringUriJobParameter extends StringJobParameter {

        private String uri;

        public StringUriJobParameter(String key) {
            super(key);
        }

        @Override
        public String getValue() {
            return uri;
        }

        @Override
        public void validateTypeConversion(String jobParameterValue) throws JobParametersInvalidException {

            try {
                this.uri = new URI(jobParameterValue).toString();
            } catch (URISyntaxException e) {
                throw new JobParametersInvalidException("Invalid URI input for parameter: " + getKey());
            }
        }
    }

    @Override
    public void afterPropertiesSet() {
        createDefaultValidator();
        createAllParameters();

    }

    private void createAllParameters() {
        allParameters = Stream.of(requiredParameters, optionalParameters)
                .flatMap(Collection::stream)
                .collect(toMap(StringJobParameter::getKey, p -> p));
    }

    private void createDefaultValidator() {
        defaultValidator = new DefaultJobParametersValidator();
        defaultValidator.setRequiredKeys(getRequiredKeys());
        defaultValidator.setOptionalKeys(getOptionalKeys());
        defaultValidator.afterPropertiesSet();
    }

    private String[] getOptionalKeys() {
        return getKeys(optionalParameters);
    }

    private String[] getRequiredKeys() {
        return getKeys(requiredParameters);
    }

    private String[] getKeys(List<? extends StringJobParameter> parameters) {
        String[] keys = new String[parameters.size()];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = parameters.get(i).getKey();
        }
        return keys;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        jobExecutionContext = jobExecution.getExecutionContext();
        putParametersOnContext(jobExecution.getJobParameters());
    }

    private void putParametersOnContext(JobParameters jobParameters) {
        Map<String, JobParameter> parameters = jobParameters.getParameters();
        for (Entry<String, JobParameter> jobParameter : parameters.entrySet()) {
            StringJobParameter stringParameter = allParameters.get(jobParameter.getKey());
            jobExecutionContext.put(stringParameter.getKey(), stringParameter.getValue());
        }
    }

    /**
     * Sets required parameters. Batch will not start if a required parameter is missing.
     *
     * @param requiredParameters
     *            the requiredParameters to set
     */
    public void setRequiredParameters(List<? extends StringJobParameter> requiredParameters) {
        this.requiredParameters = requiredParameters;
    }

    /**
     * Sets optional parameters. Batch can start if an optional parameter is missing.
     *
     * @param optionalParameters
     *            the optionalParameters to set
     */
    public void setOptionalParameters(List<? extends StringJobParameter> optionalParameters) {
        this.optionalParameters = optionalParameters;
    }

}
