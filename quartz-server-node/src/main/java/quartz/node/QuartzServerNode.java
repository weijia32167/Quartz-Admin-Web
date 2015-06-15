package quartz.node;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzServerNode {

	public static void main(String[] args) throws SchedulerException {
		SchedulerFactory schedFact = new StdSchedulerFactory("quartz.properties");
		Scheduler sched = schedFact.getScheduler();
		sched.start();
	}
}
