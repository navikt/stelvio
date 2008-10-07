package no.nav.bpchelper.actions;

import java.text.ParseException;
import java.util.Date;

import no.nav.bpchelper.cmdoptions.OptionOpts;
import no.nav.bpchelper.cmdoptions.OptionsBuilder;
import no.nav.bpchelper.query.Criteria;
import no.nav.bpchelper.query.Criterion;
import no.nav.bpchelper.query.LogicalExpression;
import no.nav.bpchelper.query.Restrictions;

import org.apache.commons.cli.CommandLine;

import com.ibm.bpe.api.ActivityInstanceData;
import com.ibm.bpe.api.ProcessInstanceData;

public class CriteriaBuilder {
	public static Criteria build(CommandLine commandLine) {
		Criteria criteria = new Criteria();

		// Static criteria added to all queries
		Criterion processStateFailedCriterion = Restrictions.eq("PROCESS_INSTANCE.STATE", ProcessInstanceData.STATE_FAILED);
		Criterion processStateTerminatedCriterion = Restrictions.eq("PROCESS_INSTANCE.STATE",
				ProcessInstanceData.STATE_TERMINATED);
		LogicalExpression processStateCriterion = Restrictions.or(processStateFailedCriterion, processStateTerminatedCriterion);
		
		Criterion activityStateFailedCriterion = Restrictions.eq("ACTIVITY.STATE", ActivityInstanceData.STATE_FAILED);
		Criterion activityStateStoppedCriterion = Restrictions.eq("ACTIVITY.STATE", ActivityInstanceData.STATE_STOPPED);
		LogicalExpression activityStateCriterion = Restrictions.or(activityStateFailedCriterion, activityStateStoppedCriterion);
		
		criteria.add(Restrictions.or(processStateCriterion, activityStateCriterion));

		// Dynamic criteria added if options specified
		if (commandLine.hasOption(OptionOpts.FILTER_STARTED_AFTER)) {
			Date date = parseDate(commandLine.getOptionValue(OptionOpts.FILTER_STARTED_AFTER));
			criteria.add(Restrictions.ge("PROCESS_INSTANCE.STARTED", date));
		}
		if (commandLine.hasOption(OptionOpts.FILTER_STARTED_BEFORE)) {
			Date date = parseDate(commandLine.getOptionValue(OptionOpts.FILTER_STARTED_BEFORE));
			criteria.add(Restrictions.le("PROCESS_INSTANCE.STARTED", date));
		}
		if (commandLine.hasOption(OptionOpts.FILTER_TEMPLATE_NAME)) {
			String processTemplateName = commandLine.getOptionValue(OptionOpts.FILTER_TEMPLATE_NAME);
			if (processTemplateName.contains("%")) {
				criteria.add(Restrictions.like("PROCESS_INSTANCE.TEMPLATE_NAME", processTemplateName));
			} else {
				criteria.add(Restrictions.eq("PROCESS_INSTANCE.TEMPLATE_NAME", processTemplateName));
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
