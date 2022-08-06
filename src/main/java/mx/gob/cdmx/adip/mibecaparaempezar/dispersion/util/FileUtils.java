package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

	private FileUtils() {
	}
	
	public static boolean existsFileOrDirectory(String pathFileOrDirectory) throws SecurityException {
		Path path = Paths.get(pathFileOrDirectory);
		return Files.exists(path);
	}
	
}
