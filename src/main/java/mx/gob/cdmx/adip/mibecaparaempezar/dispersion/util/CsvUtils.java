package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.input.BOMInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.CsvToBeanFilter;

import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.environment.Environment;

public class CsvUtils {
	
	private static final Logger LOGGER = LogManager.getLogger(CsvUtils.class);
	
	/**
	 * Método genérico que regresa un listado de objetos con el contenido del CSV indicado en el path.
	 * La clase que se envíe debe contener las anotaciones @CsvBindByName(column = "mi_columna") o  @CsvBindByPosition(position = 0)
	 * 
	 * Nota: Originalmente solo se necesita un FileReader en lugar de un BOMInputStream, pero por ejemplo con los CSV's guardados 
	 * en Excel con UTF-8 tiene un bug al leer la primer columna. Eso se solventa usando esta clase.
	 * 
	 * https://stackoverflow.com/questions/56189424/opencsv-csvtobean-first-column-not-read-for-utf-8-without-bom
	 * 
	 * @param <T>
	 * @param path
	 * @param t
	 * @return
	 * @throws IllegalStateException
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> List<T> convertCsvToList(String path, T t) throws IllegalStateException, FileNotFoundException {
		List<T> list = new ArrayList<T>();
		try(FileInputStream fileInputStream = new FileInputStream(path);
				BOMInputStream bomInputStream = new BOMInputStream(fileInputStream);
				InputStreamReader inputStreamReader = new InputStreamReader(bomInputStream, StandardCharsets.UTF_8);) {
//			return new CsvToBeanBuilder(new FileReader(path))
			list = new CsvToBeanBuilder(inputStreamReader)
					.withFilter(new EmptyLineFilter())
	                .withType(t.getClass())
	                .build()
	                .parse();
		} catch (IOException e) {
			LOGGER.warn("No se pudo cerrar un recurso del archivo CSV adecuadamente:", e);
		} 
		return list;
	}
	
	/**
	 * Filtro para que ignore las líneas vacías de los archivos CSV
	 * @author raul
	 */
	private static class EmptyLineFilter implements CsvToBeanFilter {

	 	public boolean allowLine(String[] line) {
	 		boolean blankLine = line.length == 1 && line[0].isEmpty();
	 		return !blankLine;
	    }

	 }
	
	public static void addDataToCSV(String path, List<String[]> lst)
    {
        File file = new File(path);
        FileWriter outputfile = null;
        try {
        	if (file.exists()) {
        		outputfile = new FileWriter(file, true);
        	} else {
        		outputfile = new FileWriter(file);
        	}
  
            CSVWriter writer = new CSVWriter(outputfile, 
            								 CSVWriter.DEFAULT_SEPARATOR,
                                             CSVWriter.NO_QUOTE_CHARACTER,
                                             CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                                             CSVWriter.DEFAULT_LINE_END);
            writer.writeAll(lst);
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
