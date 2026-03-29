 package at.ac.htlinn.hamsterbackend.hamsterEvaluation.debugger.model;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterFile;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench.Utils;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterProgram;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.SecureClassLoader;

/**
 * 
 * @author Daniel Jasper
 */
public class HamsterClassLoader extends SecureClassLoader {
	private HamsterFile file;
	
	public HamsterClassLoader() {
	}
	
	public HamsterProgram getHamsterInstance(HamsterFile file) {
		this.file = file;
		try {
			FileInputStream in = new FileInputStream(file.getAbsoluteClass());
			byte[] buffer = new byte[in.available()];
			in.read(buffer);
			in.close();
			Class<?> clazz = defineClass(file.getName(), buffer, 0, buffer.length);
			return (HamsterProgram) clazz.newInstance();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		File f = new File(file.getDir() + Utils.FSEP + name + ".class");
		FileInputStream in;
		try {
			in = new FileInputStream(f);
			if (f.exists()) {
				byte[] buffer = new byte[in.available()];
				in.read(buffer);
				in.close();
				Class<?> clazz = defineClass(name, buffer, 0, buffer.length);
				return clazz;
			} 
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		return super.loadClass(name);
	}
}
