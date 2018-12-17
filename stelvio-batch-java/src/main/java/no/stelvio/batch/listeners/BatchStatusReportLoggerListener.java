package no.stelvio.batch.listeners;

import static no.stelvio.batch.listeners.support.ListenerSupport.formatMillisecondsDurationAsHumanReadableString;

import java.net.InetAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import no.stelvio.common.log.InfoLogger;

/**
 * Logs a batch job status report after job has been run.
 *
 * @author person47fb8408b8e4 (Accenture)
 * @author Jar Jar Binks (Accenture)
 * @author persone38597605f58 (Capgemini)
 * @version $Id$
 */
public final class BatchStatusReportLoggerListener implements JobExecutionListener, InitializingBean {

    public static final String ORACLE= "oracle";

    private Map<String, String> dbTypeToSql = new HashMap<String, String>() {{
        put(ORACLE, "SELECT SYS_CONTEXT('USERENV', 'CURRENT_SCHEMA') FROM DUAL");
    }};

    private String databaseType = ORACLE;

    private String currentSchema;

    /** Spring injected. */
    private InfoLogger infoLogger;

    /** Spring injected. */
    private List<String> sqlStatistics;

    /** Spring injected. */
    private JdbcTemplate jdbcTemplate;

    @Override
    public void afterJob(JobExecution jobExecution) {

        infoLogger.info(formatJobExecution(jobExecution) + formatStepExecution(jobExecution.getStepExecutions()));

        if (sqlStatistics != null) {
            infoLogger.info(formatSqlStatistics());
            sqlStatistics.clear();
        }
    }

    /**
     * Format the list of SQL statistics
     *
     * @return The formated String
     */
    private Object formatSqlStatistics() {

        StringBuilder sb = new StringBuilder();
        sb.append("\n\n\n");
        sb.append("=================================================================================================================\n");
        sb.append("=============================================== SQL Statistics Summary ==========================================\n");
        sb.append("+===============================================================================================================+\n");
        String format = "|%1$-100s%2$-11s|\n";
        sb.append(String.format(format, "Step description", "#rows"));
        sb.append("+---------------------------------------------------------------------------------------------------------------+\n");

        for (String stepStatistics : sqlStatistics) {
            sb.append(stepStatistics).append("\n");
        }

        sb.append("+===============================================================================================================+\n");

        return sb.toString();
    }

    /**
     * Format JobExecution summary
     *
     * @param jobExecution
     *            the jobExecution
     * @return The formated String
     */
    private String formatJobExecution(JobExecution jobExecution) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("=======================================================================================================================\n");
        sb.append("=============================================== Spring Job Execution Summary ==========================================\n");
        sb.append("=======================================================================================================================\n");
        sb.append("Job name: ").append(jobExecution.getJobInstance().getJobName()).append("\n");
        sb.append("Job parameters:").append(jobExecution.getJobParameters().getParameters().toString());
        sb.append("\nTotal execution time: ").append(
                formatMillisecondsDurationAsHumanReadableString((jobExecution.getEndTime().getTime() - jobExecution
                        .getStartTime().getTime())));
        sb.append("\nHost: ").append(getHostNameAndIp());
        sb.append("\nSchema: ").append(getCurrentSchema()).append("\n");
        sb.append("\n");
        sb.append("\n");
        sb.append("+====================================================================== Spring Job Execution Summary ===============================================================================================+\n");
        sb.append("|Name                                    |ExitStatus |Exit Description                                                 |Time             |Start Time                  |End Time                     |\n");
        sb.append("+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+\n");

        String format = "|%1$-40s|%2$-11s|%3$-65s|%4$17s|%5$28s|%6$29s|\n";

        sb.append(String.format(format, jobExecution.getJobInstance().getJobName(), jobExecution.getExitStatus().getExitCode(),
                jobExecution.getExitStatus().getExitDescription(),
                formatMillisecondsDurationAsHumanReadableString((jobExecution.getEndTime().getTime() - jobExecution
                        .getStartTime().getTime())), jobExecution.getStartTime(), jobExecution.getEndTime()));

        sb.append("+===================================================================================================================================================================================================+\n");

        return sb.toString();
    }

    /**
     * Format step execution summary
     *
     * @param stepExecutions
     *            the step executions
     * @return the formated String
     */
    private String formatStepExecution(Collection<StepExecution> stepExecutions) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("+========================================================================= Step Execution Summary =================================================================================================+\n");
        sb.append("|Name                                    |ExitStatus |Time             |Read Count |Filter Count |Write Count |Read Skip Count |Write Skip Count |Process Skip Count |Commit Count |Rollback Count |\n");
        sb.append("+--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+\n");
        for (StepExecution stepExecution : stepExecutions) {
            String format = "|%1$-40s|%2$-11s|%3$17s|%4$11s|%5$13s|%6$12s|%7$16s|%8$17s|%9$19s|%10$13s|%11$15s|\n";
            sb.append(String.format(format, stepExecution.getStepName(), stepExecution.getExitStatus().getExitCode(),
                    formatMillisecondsDurationAsHumanReadableString((stepExecution.getEndTime().getTime() - stepExecution
                            .getStartTime().getTime())), stepExecution.getReadCount(), stepExecution.getFilterCount(),
                    stepExecution.getWriteCount(), stepExecution.getReadSkipCount(), stepExecution.getWriteSkipCount(),
                    stepExecution.getProcessSkipCount(), stepExecution.getCommitCount(), stepExecution.getRollbackCount()));
        }

        sb.append("+==================================================================================================================================================================================================+\n");

        return sb.toString();
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {

        String jobName = jobExecution.getJobInstance().getJobName();
        Long jobId = jobExecution.getJobId();

        String format = "==================================== Executing batch: %1$-7s(ID:%2$6s) ==============================================\n";

        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("=======================================================================================================================\n");
        sb.append(String.format(format, jobName, jobId));
        sb.append("=======================================================================================================================\n");
        sb.append("Start time: ").append(jobExecution.getStartTime()).append("\n");
        sb.append("Job parameters:").append(jobExecution.getJobParameters().getParameters().toString()).append("\n");
        sb.append("Host: ").append(getHostNameAndIp()).append("\n");
        sb.append("Schema: ").append(getCurrentSchema()).append("\n");

        sb.append("=======================================================================================================================\n");

        infoLogger.info(sb.toString());

        if (sqlStatistics != null) {
            sqlStatistics.clear();
        }

    }

    /**
     * Get host name and IP.
     *
     * @return String representing HostName and IP
     */
    private String getHostNameAndIp() {
        String myIp = "UNKNOWN";
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            InetAddress addrs[] = InetAddress.getAllByName(hostName);

            for (InetAddress addr : addrs) {
                if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress()) {
                    myIp = addr.getHostName() + "/" + addr.getHostAddress();
                }
            }
        } catch (Exception e) {
            myIp += e.getMessage();
            infoLogger.info("Exception caught when deciding IP-address and hostname to display in batch summary. "
                    + e.toString());
        }
        return myIp;

    }

    /**
     * @return the current databaseSchema
     */
    private String getCurrentSchema() {

        if (currentSchema == null){
            currentSchema = "UNKNOWN";
            String currentSchemaSql = dbTypeToSql.get(databaseType);

            if(currentSchemaSql != null) {
                currentSchema = jdbcTemplate.queryForObject(currentSchemaSql, String.class);
            }
        }

        return currentSchema;
    }

    /**
     * Setter for the InfoLogger
     *
     * @param infoLogger
     *            the infoLogger to set
     */
    public void setInfoLogger(InfoLogger infoLogger) {
        this.infoLogger = infoLogger;
    }

    /**
     * @param sqlStatistics
     *            the sqlStatistics to set
     */
    public void setSqlStatistics(List<String> sqlStatistics) {
        this.sqlStatistics = sqlStatistics;
    }

    /**
     * Sets the jdbcTemplate.
     *
     * @param jdbcTemplate
     *            the jdbcTemplate to set
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(infoLogger, "BatchStatusReportLoggerListener requires a InfoLogger");
        Assert.notNull(jdbcTemplate, "BatchStatusReportLoggerListener requires a JdbcTemplate");
    }

    /**
     * Get the databayse type
     * @return the databasetype as a String
     */
    public String getDatabaseType() {
        return databaseType;
    }

    /**
     * Sets the database type
     * @param databaseType the database type to set
     */
    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

}
