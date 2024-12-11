package org.geoandri.quarztest

import org.quartz.DisallowConcurrentExecution
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory
import java.util.*

@DisallowConcurrentExecution
class ProcessingJob(
    private val triggerRepository: TriggerRepository
) : Job {

    companion object {
        private val logger = LoggerFactory.getLogger(ProcessingJob::class.java)
    }

    override fun execute(context: JobExecutionContext) {
        val trigger = triggerRepository.findById(UUID.fromString(context.trigger.key.name)).get()
        triggerRepository.save(trigger.copy("RUNNING"))
        logger.info("Processing job for workspace ${context.mergedJobDataMap["workspaceId"]}")
        Thread.sleep(10000)
        triggerRepository.save(trigger.copy("COMPLETED"))
        logger.info("Job finished for workspace ${context.mergedJobDataMap["workspaceId"]}")
    }
}