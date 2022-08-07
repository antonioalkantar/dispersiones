package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto;

import java.io.Serializable;

/**
 * @author raul
 */
public class ResultadoEjecucionDTO implements Serializable{
	
	private static final long serialVersionUID = 1071443940818972923L;
	
	private int totalRegistros;
	private int totalDispersados;
	private int totalNoDispersados;
	
	public int getTotalRegistros() {
		return totalRegistros;
	}
	public void setTotalRegistros(int totalRegistros) {
		this.totalRegistros = totalRegistros;
	}
	public int getTotalDispersados() {
		return totalDispersados;
	}
	public void setTotalDispersados(int totalDispersados) {
		this.totalDispersados = totalDispersados;
	}
	public int getTotalNoDispersados() {
		return totalNoDispersados;
	}
	public void setTotalNoDispersados(int totalNoDispersados) {
		this.totalNoDispersados = totalNoDispersados;
	}
}