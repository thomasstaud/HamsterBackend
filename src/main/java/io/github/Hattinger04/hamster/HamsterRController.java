package io.github.Hattinger04.hamster;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.Hattinger04.RestServices;
import io.github.Hattinger04.hamster.model.Hamster;
import io.github.Hattinger04.hamsterEvaluation.workbench.Workbench;

@RestController
@RequestMapping("/hamster")
public class HamsterRController {

	@Autowired
	private RestServices restServices; 
	
	private Workbench wb = Workbench.getWorkbench(); 

	// TODO: Unterordner / nicht nur eine Datei! 
	/**
	 * Creates new File and will create a parent folder if not existing
	 * It also writes the given program into the file
	 * 
	 * @param path
	 */
	private boolean createNewFile(String path, String program) {
		try {
			File file = new File(path); 
			file.getParentFile().mkdir(); 
			file.createNewFile();
			FileOutputStream fileOutputStream = new FileOutputStream(file, false);
			fileOutputStream.write(program.getBytes());
			fileOutputStream.flush();
			fileOutputStream.close();
			return true; 
		} catch (IOException e) {
			return false; 
		}
	}
	
//	@PreAuthorize("hasAuthority('ADMIN')") // no idea if this is actually working like that
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/defaultTerrain")
	@ResponseBody
	public String defaultTerrain(@RequestBody String json) {
		System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
		System.out.println(json);
		Hamster hamster = restServices.deserializeHamster(json);
		System.out.println(hamster.toString());
		String path = String.format("src/main/resources/hamster/%s/%s.ham", SecurityContextHolder.getContext().getAuthentication().getName(), hamster.getProgramName());
		if(createNewFile(path, hamster.getProgram())) {
			return restServices.serialize(wb.startProgram(path));
		}
		return restServices.errorMessage("File could not be created!"); 
	}
	
	public String exisitingTerrain(@RequestBody String json) {
		return ""; 
	}
	
	public String newTerrain(@RequestBody String json) {
		return ""; 
	}
	
}
