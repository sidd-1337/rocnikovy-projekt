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
	//@Bean
	//public CommandLineRunner run(ProgrammesService programmeService) {
	//	return args -> {
	//		// Save data for each faculty
	//		programmeService.fetchDataAndSave("FAU");
	//		programmeService.fetchDataAndSave("FFI");
	//		programmeService.fetchDataAndSave("FPD");
	//		programmeService.fetchDataAndSave("FPR");
	//		programmeService.fetchDataAndSave("FSS");
	//		programmeService.fetchDataAndSave("FZS");
	//	};
	//}
    /*@Bean
    public CommandLineRunner run(TempSubjectsService tempSubjectsService) {
        return args -> {
            // Call fetchDataAndSave with the desired parameters
            tempSubjectsService.fetchDataAndSave("KSN", "1VLA5");
        };
    //}


    @Bean
    public CommandLineRunner run(SubjectsService subjectsService) {
        return args -> {
            // Call fetchDataAndSave with the desired parameters
            subjectsService.fetchDataAndSave("3071", "LS");
        };
    */

}