package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto;

import java.io.Serializable;

/**
 * @author raul
 */
public class ResultadoEjecucionDTO implements Serializable{
	
	private static final long serialVersionUID = 1071443940818972923L;
	
	private int totalRegistros;
	private int totalCorrectos;
	private int totalIncorrectos;
	
	public int getTotalRegistros() {
		return totalRegistros;
	}
	public void setTotalRegistros(int totalRegistros) {
		this.totalRegistros = totalRegistros;
	}
	public int getTotalCorrectos() {
		return totalCorrectos;
	}
	public void setTotalCorrectos(int totalCorrectos) {
		this.totalCorrectos = totalCorrectos;
	}
	public int getTotalIncorrectos() {
		return totalIncorrectos;
	}
	public void setTotalIncorrectos(int totalIncorrectos) {
		this.totalIncorrectos = totalIncorrectos;
	}
}