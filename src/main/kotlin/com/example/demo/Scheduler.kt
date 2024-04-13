package com.example.demo

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.lang.System.Logger

@Service
class Scheduler {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Scheduled(cron = "\${my.cron.value}")
    fun fixedRateSchedulerTask() {
        logger.info("Hi!")
    }

}