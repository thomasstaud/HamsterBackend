package io.github.Hattinger04.hamster;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.Hattinger04.RestServices;
import io.github.Hattinger04.hamster.model.Hamster;
import io.github.Hattinger04.hamsterEvaluation.workbench.Workbench;
import io.github.Hattinger04.hamsterEvaluation.workbench.Workbench.TerrainForm;

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
	private File createNewFile(String path) {
		try {
			File file = new File(path); 
			file.getParentFile().mkdir(); 
			file.createNewFile();
			return file; 
		} catch (IOException e) {
			return null; 
		}
	}
	
	private boolean writeProgramToFile(File file, String program) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file, false);
			fileOutputStream.write(program.getBytes());
			fileOutputStream.flush();
			fileOutputStream.close();
			return true; 
		} catch (IOException | NullPointerException e) {
			return false; 
		}
	}
	
	@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER')")
	@PostMapping("/defaultTerrain")
	@ResponseBody
	public String defaultTerrain(@RequestBody String json) {
		Hamster hamster = restServices.deserializeHamster(json);
		String path = String.format("src/main/resources/hamster/%s/%s.ham", SecurityContextHolder.getContext().getAuthentication().getName(), hamster.getProgramName());
		createNewFile(path); 
		writeProgramToFile(new File(path), hamster.getProgram());
		wb.getJsonObject().clear();
		return restServices.serialize(wb.startProgram(path));	
	}
	
	// not tested!
	public String exisitingTerrain(@RequestBody String json) {
		Hamster hamster = restServices.deserializeHamster(json);
		String hamsterPath = String.format("src/main/resources/hamster/%s/%s.ham", SecurityContextHolder.getContext().getAuthentication().getName(), hamster.getProgramName());
		String terrainPath = String.format("src/main/resources/hamster/%s/%s.ter", SecurityContextHolder.getContext().getAuthentication().getName(), hamster.getTerrainName());
		createNewFile(hamsterPath); 
		writeProgramToFile(new File(hamsterPath), hamster.getProgram());
		wb.getJsonObject().clear(); 
		return restServices.serialize(wb.startProgram(hamsterPath, terrainPath));
	}
	
	// not tested!
	public String newTerrain(@RequestBody String json) {
		Hamster hamster = restServices.deserializeHamster(json);
		String hamsterPath = String.format("src/main/resources/hamster/%s/%s.ham", SecurityContextHolder.getContext().getAuthentication().getName(), hamster.getProgramName());
		String terrainPath = String.format("src/main/resources/hamster/%s/%s.ter", SecurityContextHolder.getContext().getAuthentication().getName(), hamster.getTerrainName());
		createNewFile(hamsterPath); 
		createNewFile(terrainPath); 
		writeProgramToFile(new File(hamsterPath), hamster.getProgram());
		wb.getJsonObject().clear(); 
		return restServices.serialize(wb.startProgram(hamsterPath, terrainPath, 
				wb.new TerrainForm(hamster.getLeange(), hamster.getBreite(), hamster.getCorn(), hamster.getCornAnzahl(), hamster.getWall(), hamster.getX(), hamster.getY())));
	}
	
}
