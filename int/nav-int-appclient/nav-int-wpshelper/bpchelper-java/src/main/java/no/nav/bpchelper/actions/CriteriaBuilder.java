package no.nav.bpchelper.actions;

import java.text.ParseException;
import java.util.Date;
import java.util.StringTokenizer;

import no.nav.bpchelper.cmdoptions.OptionOpts;
import no.nav.bpchelper.cmdoptions.OptionsBuilder;
import no.nav.bpchelper.query.Criteria;
import no.nav.bpchelper.query.Criterion;
import no.nav.bpchelper.query.LogicalExpression;
import no.nav.bpchelper.query.Restrictions;

import org.apache.commons.cli.CommandLine;

import com.ibm.bpe.api.ActivityInstanceData;
import com.ibm.bpe.clientmodel.bean.ProcessInstanceBean;

public class CriteriaBuilder {
	public static Criteria build(CommandLine commandLine) {
		Criteria criteria = new Criteria();

		if (!commandLine.hasOption(OptionOpts.ALL_STATES)) {
			// Static criteria added to all queries
			Criterion processStateFailedCriterion = Restrictions.eq("PROCESS_INSTANCE.STATE", ProcessInstanceBean.STATE_FAILED);
			Criterion processStateTerminatedCriterion = Restrictions.eq("PROCESS_INSTANCE.STATE",
					ProcessInstanceBean.STATE_TERMINATED);
			LogicalExpression processStateCriterion = Restrictions.or(processStateFailedCriterion, processStateTerminatedCriterion);
	
			Criterion activityStateFailedCriterion = Restrictions.eq("ACTIVITY.STATE", ActivityInstanceData.STATE_FAILED);
			Criterion activityStateStoppedCriterion = Restrictions.eq("ACTIVITY.STATE", ActivityInstanceData.STATE_STOPPED);
			LogicalExpression activityStateCriterion = Restrictions.or(activityStateFailedCriterion, activityStateStoppedCriterion);
	
			criteria.add(Restrictions.or(processStateCriterion, activityStateCriterion));
		}

		// Dynamic criteria added if options specified
		if (commandLine.hasOption(OptionOpts.FILTER_PROCESS_STARTED_TIME_FRAME)) {
			StringTokenizer st = new StringTokenizer(commandLine.getOptionValue(OptionOpts.FILTER_PROCESS_STARTED_TIME_FRAME),
					"-");
			Date processStartedAfterDate = parseDate(st.nextToken());
			criteria.add(Restrictions.ge("PROCESS_INSTANCE.STARTED", processStartedAfterDate));
			Date processStartedBeforeDate = parseDate(st.nextToken());
			criteria.add(Restrictions.le("PROCESS_INSTANCE.STARTED", processStartedBeforeDate));
		}
		if (commandLine.hasOption(OptionOpts.FILTER_PROCESS_TEMPLATE_NAME)) {
			String processTemplateName = commandLine.getOptionValue(OptionOpts.FILTER_PROCESS_TEMPLATE_NAME);
			if (processTemplateName.contains("%")) {
				criteria.add(Restrictions.like("PROCESS_INSTANCE.TEMPLATE_NAME", processTemplateName));
			} else {
				criteria.add(Restrictions.eq("PROCESS_INSTANCE.TEMPLATE_NAME", processTemplateName));
			}
		}
		if (commandLine.hasOption(OptionOpts.FILTER_PROCESS_CUSTOM_PROPERTY)) {
			String[] customPropertiesFilterArgs = commandLine.getOptionValues(OptionOpts.FILTER_PROCESS_CUSTOM_PROPERTY);
			int propertyCount = customPropertiesFilterArgs.length / 2;
			for (int i = 0; i < propertyCount; i++) {
				String viewName = "PROCESS_ATTRIBUTE" + (i + 1);

				String propertyName = customPropertiesFilterArgs[i * 2];
				if (propertyName.contains("%")) {
					criteria.add(Restrictions.like(viewName + ".NAME", propertyName));
				} else {
					criteria.add(Restrictions.eq(viewName + ".NAME", propertyName));
				}

				String propertyValue = customPropertiesFilterArgs[i * 2 + 1];
				if (propertyValue.contains("%")) {
					criteria.add(Restrictions.like(viewName + ".VALUE", propertyValue));
				} else {
					criteria.add(Restrictions.eq(viewName + ".VALUE", propertyValue));
				}
			}
		}
		if (commandLine.hasOption(OptionOpts.FILTER_ACTIVITY_NAME)) {
			String activityName = commandLine.getOptionValue(OptionOpts.FILTER_ACTIVITY_NAME);
			if (activityName.contains("%")) {
				criteria.add(Restrictions.like("ACTIVITY.TEMPLATE_NAME", activityName));
			} else {
				criteria.add(Restrictions.eq("ACTIVITY.TEMPLATE_NAME", activityName));
			}
		}
		
		if (commandLine.hasOption(OptionOpts.LIMIT)) {
			criteria.setResultRowLimit(Integer.parseInt(commandLine.getOptionValue(OptionOpts.LIMIT)));
			if(commandLine.getOptionValue(OptionOpts.ACTION).toUpperCase().equals("RESUME"))
				criteria.add(Restrictions.eq("PROCESS_INSTANCE.STATE", ProcessInstanceBean.STATE_SUSPENDED));
			
			if(commandLine.getOptionValue(OptionOpts.ACTION).toUpperCase().equals("SUSPEND"))
				criteria.add(Restrictions.eq("PROCESS_INSTANCE.STATE", ProcessInstanceBean.STATE_RUNNING));
		}

		return criteria;
	}

	private static Date parseDate(String dateString) {
		try {
			return OptionsBuilder.TIMESTAMP_FORMAT.parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
