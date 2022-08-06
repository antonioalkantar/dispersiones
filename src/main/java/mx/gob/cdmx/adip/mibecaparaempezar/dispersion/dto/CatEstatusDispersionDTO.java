package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto;

import java.io.Serializable;

public class CatEstatusDispersionDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7556406127108271018L;
	private Long idEstatusDispersion;
	private String descripcion;
	private Boolean estatus;

	public CatEstatusDispersionDTO() {

	}
	
	public CatEstatusDispersionDTO(Long idEstatusDispersion) {
		this.idEstatusDispersion = idEstatusDispersion;
	}
	
	public CatEstatusDispersionDTO(Long idEstatusDispersion, String descripcion) {
		this.idEstatusDispersion = idEstatusDispersion;
		this.descripcion = descripcion;
	}

	public CatEstatusDispersionDTO(Long idEstatusDispersion, String descripcion, Boolean estatus) {
		this.idEstatusDispersion = idEstatusDispersion;
		this.descripcion = descripcion;
		this.estatus = estatus;
	}

	public Long getIdEstatusDispersion() {
		return idEstatusDispersion;
	}

	public void setIdEstatusDispersion(Long idEstatusDispersion) {
		this.idEstatusDispersion = idEstatusDispersion;
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
