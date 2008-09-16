package no.nav.bpchelper.actions;

import java.text.ParseException;
import java.util.Date;

import no.nav.bpchelper.cmdoptions.ActionOptionValues;
import no.nav.bpchelper.cmdoptions.OptionOpts;
import no.nav.bpchelper.cmdoptions.OptionsBuilder;
import no.nav.bpchelper.query.Criteria;
import no.nav.bpchelper.query.Criterion;
import no.nav.bpchelper.query.Restrictions;
import no.nav.bpchelper.query.SimpleExpression;

import org.apache.commons.cli.CommandLine;

import com.ibm.bpe.api.ActivityInstanceData;
import com.ibm.bpe.api.ProcessInstanceData;

public class ActionFactory {
	public static Action getAction(CommandLine commandLine) {
		String actionValue = commandLine.getOptionValue(OptionOpts.ACTION);
		AbstractAction action = ActionOptionValues.valueOf(actionValue).getAction();
		
		Criteria criteria = new Criteria();
		
		// Static criteria added to all queries
		Criterion processStateFailedCriterion = Restrictions.eq("PROCESS_INSTANCE.STATE", ProcessInstanceData.STATE_FAILED);
		Criterion activityStateFailedCriterion = Restrictions.eq("ACTIVITY.STATE", ActivityInstanceData.STATE_FAILED);
		criteria.add(Restrictions.or(processStateFailedCriterion, activityStateFailedCriterion));
		
		// Dynamic criteria added if options specified
		if (commandLine.hasOption(OptionOpts.STARTED_AFTER)) {
			Date date = parseDate(commandLine.getOptionValue(OptionOpts.STARTED_AFTER));
			criteria.add(Restrictions.ge("PROCESS_INSTANCE.STARTED", date));
		}
		if (commandLine.hasOption(OptionOpts.STARTED_BEFORE)) {
			Date date = parseDate(commandLine.getOptionValue(OptionOpts.STARTED_BEFORE));
			criteria.add(Restrictions.le("PROCESS_INSTANCE.STARTED", date));
		}
		if (commandLine.hasOption(OptionOpts.PROCESS_TEMPLATE_NAME)) {
			String processTemplateName = commandLine.getOptionValue(OptionOpts.PROCESS_TEMPLATE_NAME);
			if (processTemplateName.contains("%")) {
				criteria.add(Restrictions.like("PROCESS_INSTANCE.TEMPLATE_NAME", processTemplateName));
			} else {
				criteria.add(Restrictions.eq("PROCESS_INSTANCE.TEMPLATE_NAME", processTemplateName));
			}
		}
		if (commandLine.hasOption(OptionOpts.ACTIVITY_NAME)) {
			String activityName = commandLine.getOptionValue(OptionOpts.ACTIVITY_NAME);
			if (activityName.contains("%")) {
				criteria.add(Restrictions.like("ACTIVITY.TEMPLATE_NAME", activityName));
			} else {
				criteria.add(Restrictions.eq("ACTIVITY.TEMPLATE_NAME", activityName));
			}
		}
		
		action.setCriteria(criteria);
		
		return action;
	}

	private static Date parseDate(String dateString) {
		try {
			return OptionsBuilder.TIMESTAMP_FORMAT.parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
