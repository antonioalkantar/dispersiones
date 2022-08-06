package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto;

import java.io.Serializable;

public class CatNivelEducativoDTO implements Serializable {

	private static final long serialVersionUID = -12228519343647581L;

	private Integer idNivel;
	private String descripcion;
	private Boolean estatus;

	public CatNivelEducativoDTO() {
	}

	public CatNivelEducativoDTO(Integer idNivel) {
		this.idNivel = idNivel;
	}
	
	public CatNivelEducativoDTO(Integer idNivel, String descripcion) {
		this.idNivel = idNivel;
		this.descripcion = descripcion;
	}

	
	public CatNivelEducativoDTO(Integer idNivel, String descripcion, Boolean estatus) {
		this.idNivel = idNivel;
		this.descripcion = descripcion;
		this.estatus = estatus;
	}

	public Integer getIdNivel() {
		return idNivel;
	}

	public void setIdNivel(Integer idNivel) {
		this.idNivel = idNivel;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Boolean getEstatus() {
		return estatus;
	}

	public void setEstatus(Boolean estatus) {
		this.estatus = estatus;
	}
	

}
