package com.tigeranalytics.pricefeed.app.controller;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final Job job;
    private final JobLauncher jobLauncher;
    private final String temp_storage = "C:\\Workspace\\Code\\POC\\RatailChainPricingFeeds\\services\\price-feed-ms\\src\\main\\resources\\";

    public JobController(JobLauncher jobLauncher, Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    /**
     * Here the file path we don't know, user can upload from any random location,
     * we can't force them to upload from certain path.
     * 1. So we need to copy the file to some storage in your VM.
     *   Once the work is done we can delete the file data from VM to free up the space(logic below).
     * 2. Alternatively, we can copy the file to DB and get the file path and their content as Byte Array.
     */
    @PostMapping("/importPricingFeeds")
    public void importCsvToDBJob(@RequestParam("file") MultipartFile file) {
        try {
            String originalFileName = file.getOriginalFilename();
            File fileToImport = new File(temp_storage + originalFileName);
            file.transferTo(fileToImport);
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("fullPathFileName", temp_storage + originalFileName)
                    .addLong("startAt", System.currentTimeMillis()).toJobParameters();
            JobExecution execution = jobLauncher.run(job, jobParameters);
            if(execution.getExitStatus().getExitCode().equals(ExitStatus.COMPLETED)) {
                /** We can delete the file from the location/folder/VM etc.
                 *  Files.deleteIfExists(Paths.get(temp_storage + originalFileName));
                 **/
            }
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException | IOException e) {
            e.printStackTrace();
        }
    }
}
