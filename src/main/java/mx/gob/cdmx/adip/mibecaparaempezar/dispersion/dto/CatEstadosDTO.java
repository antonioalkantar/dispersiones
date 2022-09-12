package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto;

import java.io.Serializable;

public class CatEstadosDTO implements Serializable{

	private static final long serialVersionUID = 7266135951031150234L;
	private Integer idEstado;
	private String descripcion;
	private String claveEstado;
	
	public CatEstadosDTO() {
		
	}
	
	/**
	 * @param idEstado
	 * @param descripcion
	 */
	public CatEstadosDTO(Integer idEstado, String descripcion) {
		this.idEstado = idEstado;
		this.descripcion = descripcion;
	}



	public CatEstadosDTO(Integer idEstado, String descripcion, String claveEstado) {
		this.idEstado = idEstado;
		this.descripcion = descripcion;
		this.claveEstado = claveEstado;
	}

	/**
	 * @return the idEstado
	 */
	public Integer getIdEstado() {
		return idEstado;
	}

	/**
	 * @param idEstado the idEstado to set
	 */
	public void setIdEstado(Integer idEstado) {
		this.idEstado = idEstado;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the claveEstado
	 */
	public String getClaveEstado() {
		return claveEstado;
	}

	/**
	 * @param claveEstado the claveEstado to set
	 */
	public void setClaveEstado(String claveEstado) {
		this.claveEstado = claveEstado;
	}
	
	
}
