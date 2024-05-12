package com.example.backend1;

import com.example.backend1.model.TempSubjectsModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.example.backend1.service.ProgrammesService;
import com.example.backend1.service.TempSubjectsService;
import com.example.backend1.service.SubjectsService;

@SpringBootApplication
public class Backend1Application {

	public static void main(String[] args) {
		SpringApplication.run(Backend1Application.class, args);
	}
	@Bean
	public CommandLineRunner run(ProgrammesService programmeService, TempSubjectsService tempSubjectsService, SubjectsService subjectsService) {
		return args -> {
			// Save data for each faculty in ProgrammesService
			programmeService.fetchDataAndSave("FAU");
			programmeService.fetchDataAndSave("FFI");
			programmeService.fetchDataAndSave("FPD");
			programmeService.fetchDataAndSave("FPR");
			programmeService.fetchDataAndSave("FSS");
			programmeService.fetchDataAndSave("FZS");

			// Save data for TempSubjectsService
			tempSubjectsService.fetchDataAndSave("KSN", "1VLA5");

			// Save data for SubjectsService
			subjectsService.fetchDataAndSave("3071", "LS");
		};
	}

}