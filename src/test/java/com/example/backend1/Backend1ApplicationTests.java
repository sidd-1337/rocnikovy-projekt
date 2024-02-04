package com.example.backend1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
//import org.junit.platform.suite.api.SuiteExecutionMode;
import com.example.backend1.controller.OborIdIntegrationTest;
import com.example.backend1.controller.ProgrammeIntegrationTest;
import com.example.backend1.controller.SubjectIntegrationTest;
import com.example.backend1.repository.DuplicityTest;



@Suite
//@SuiteExecutionMode(SuiteExecutionMode.CONCURRENT) // Optional: run tests concurrently
@SelectClasses({
		OborIdIntegrationTest.class,
		ProgrammeIntegrationTest.class,
		SubjectIntegrationTest.class,
		DuplicityTest.class // Assuming DuplicityTest contains both duplicity tests you mentioned
})
@SpringBootTest
class Backend1ApplicationTests {

	@Test
	void contextLoads() {
	}

}
