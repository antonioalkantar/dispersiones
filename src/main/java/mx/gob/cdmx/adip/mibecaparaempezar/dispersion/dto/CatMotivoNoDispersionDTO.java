package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto;

import java.io.Serializable;

public class CatMotivoNoDispersionDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7556406127108271018L;
	private Long idMotivoNoDispersion;
	private String descripcion;
	private Boolean estatus;

	public CatMotivoNoDispersionDTO() {

	}
	
	public CatMotivoNoDispersionDTO(Long idMotivoNoDispersion) {
		this.idMotivoNoDispersion = idMotivoNoDispersion;
	}
	
	public CatMotivoNoDispersionDTO(Long idMotivoNoDispersion, String descripcion) {
		this.idMotivoNoDispersion = idMotivoNoDispersion;
		this.descripcion = descripcion;
	}

	public CatMotivoNoDispersionDTO(Long idMotivoNoDispersion, String descripcion, Boolean estatus) {
		this.idMotivoNoDispersion = idMotivoNoDispersion;
		this.descripcion = descripcion;
		this.estatus = estatus;
	}

	public Long getIdMotivoNoDispersion() {
		return idMotivoNoDispersion;
	}

	public void setIdMotivoNoDispersion(Long idMotivoNoDispersion) {
		this.idMotivoNoDispersion = idMotivoNoDispersion;
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
