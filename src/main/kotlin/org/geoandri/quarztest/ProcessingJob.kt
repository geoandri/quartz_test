package org.geoandri.quarztest

import org.quartz.DisallowConcurrentExecution
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory
import java.util.*

@DisallowConcurrentExecution
class ProcessingJob(
    private val sbomJobRepository: SbomJobRepository
) : Job {

    companion object {
        private val logger = LoggerFactory.getLogger(ProcessingJob::class.java)
    }

    override fun execute(context: JobExecutionContext) {
        val originalTriggerName = context.mergedJobDataMap.getString("originalTriggerName")

        sbomJobRepository.findById(UUID.fromString(originalTriggerName)).ifPresent {
            sbomJobRepository.save(it.copy("RUNNING"))
            logger.info("Processing job for workspace ${it.workspaceId} with message: ${it.message}")
            Thread.sleep(60000)
            sbomJobRepository.save(it.copy("COMPLETED"))
            logger.info("Job finished for workspace ${it.workspaceId} with message: ${it.message}")
        }
    }
}