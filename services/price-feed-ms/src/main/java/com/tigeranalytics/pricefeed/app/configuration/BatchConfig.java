package com.tigeranalytics.pricefeed.app.configuration;

import com.tigeranalytics.pricefeed.app.entity.PricingFeed;
import com.tigeranalytics.pricefeed.app.repository.PricingFeedRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.beans.PropertyEditorSupport;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class BatchConfig {

    private PricingFeedRepository pricingFeedRepository;

    @Bean
    @StepScope
    public FlatFileItemReader<PricingFeed> reader(@Value("#{jobParameters[fullPathFileName]}") String pathToFile) {
        FlatFileItemReader<PricingFeed> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource(new File(pathToFile)));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<PricingFeed> lineMapper() {
        DefaultLineMapper<PricingFeed> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("storeId", "sku", "productName", "price", "date");
        BeanWrapperFieldSetMapper<PricingFeed> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(PricingFeed.class);
        fieldSetMapper.setCustomEditors(Map.of(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalDate.parse(text, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
        }));
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public PricingFeedProcessor processor() {
        return new PricingFeedProcessor();
    }

    @Bean
    public RepositoryItemWriter<PricingFeed> writer() {
        RepositoryItemWriter<PricingFeed> writer = new RepositoryItemWriter<>();
        writer.setRepository(pricingFeedRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }

    @Bean
    public Step csvFileToDatabaseStep(JobRepository jobRepository,
                                      PlatformTransactionManager transactionManager,
                                      FlatFileItemReader<PricingFeed> reader) {
        return new StepBuilder("csv-step", jobRepository)
                .<PricingFeed, PricingFeed>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .faultTolerant()
                .retryLimit(3)
                .retry(Exception.class)
                .skipLimit(5)
                .skip(Exception.class)
                .build();
    }

    @Bean
    public Job csvFileToDatabaseJob(JobRepository jobRepository,
                                    PlatformTransactionManager transactionManager,
                                    FlatFileItemReader<PricingFeed> reader) {
        return new JobBuilder("importPricingFeeds", jobRepository)
                .flow(csvFileToDatabaseStep(jobRepository, transactionManager, reader))
                .end()
                .build();
    }
}

