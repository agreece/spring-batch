/*
 * Copyright 2006-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.batch.sample.iosample;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.job.AbstractJob;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Dave Syer
 * @since 2.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/simple-job-launcher-context.xml", "/jobs/ioSampleJob.xml",
		"/jobs/iosample/jdbcPaging.xml" })
public class TwoJobInstancesPagingFunctionalTests {

	@Autowired
	private JobLauncher launcher;

	@Autowired
	private AbstractJob job;

	@Test
	public void testLaunchJobTwice() throws Exception {
		JobExecution jobExecution = launcher.run(this.job, getJobParameters(1000.));
		assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
		assertEquals(4, jobExecution.getStepExecutions().iterator().next().getWriteCount());
		jobExecution = launcher.run(this.job, getJobParameters(1000000.));
		assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
		assertEquals(0, jobExecution.getStepExecutions().iterator().next().getWriteCount());
	}

	protected JobParameters getJobParameters(double amount) {
		return new JobParametersBuilder().addLong("timestamp", new Date().getTime()).addDouble("credit", amount)
				.toJobParameters();
	}

}