package org.geoandri.quarztest

import org.quartz.JobBuilder.newJob
import org.quartz.JobDetail
import org.quartz.JobKey
import org.quartz.SimpleScheduleBuilder
import org.quartz.TriggerBuilder
import org.slf4j.LoggerFactory
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.util.*


@RestController
class JobController(
    private val quartzScheduler: SchedulerFactoryBean,
    private val sbomJobRepository: SbomJobRepository
) {

    companion object {
        private val logger = LoggerFactory.getLogger(JobController::class.java)
    }

    // Method to post a job that contains a DTO with the job message and the runId
    @PostMapping("/job")
    fun postJob(@RequestBody jobDTORequest: JobDTORequest): JobDTOResponse {
        // This will enqueue the job to be executed using Quartz
        logger.info("Received Job request for workspace ${jobDTORequest.workspaceId} with message: ${jobDTORequest.message}")

        val jobId = UUID.randomUUID()
        val jobKey = JobKey.jobKey("workspaceId", jobDTORequest.workspaceId.toString())

        if (!quartzScheduler.scheduler.checkExists(jobKey)) {
            val jobDetail: JobDetail = newJob(ProcessingJob::class.java)
                .withIdentity(jobKey) // Group by workspaceId
                .usingJobData("workspaceId", jobDTORequest.workspaceId.toString())
                .storeDurably()
                .requestRecovery(true)
                .build()

            quartzScheduler.scheduler.addJob(jobDetail, true)
        }

        val trigger = TriggerBuilder.newTrigger()
            .withIdentity("$jobId", jobDTORequest.workspaceId.toString())
            .forJob(jobKey)
            .startNow()
            .usingJobData("originalTriggerName", "$jobId")
            .usingJobData("message", jobDTORequest.message)
            .build()

        quartzScheduler.scheduler.scheduleJob(trigger)
        val job = sbomJobRepository.save(
            SbomJobEntity(
                jobId,
                jobDTORequest.workspaceId,
                "SCHEDULED",
                jobDTORequest.message,
                Instant.now(),
                Instant.now()
            )
        )

        return job.toDTO()
    }

    @GetMapping("/job/{jobId}")
    fun getJob(@PathVariable jobId: UUID): JobDTOResponse {
        // This will return the status of the job
        val job = sbomJobRepository.findById(jobId).orElseThrow {
            throw JobNotFoundException()
        }

        return job.toDTO()
    }
}