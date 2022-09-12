package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto;

import java.io.Serializable;

import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.util.Constantes;


public class CatMunicipiosDTO implements Serializable{

	private static final long serialVersionUID = -2515632900281761299L;
	private Integer idMunicipio;
	private CatEstadosDTO catEstadosDTO;
	private String descripcion;
	private String cveAlcaldia;
	
	public CatMunicipiosDTO() {
		this.catEstadosDTO =  new CatEstadosDTO();
	}
	
	public CatMunicipiosDTO(String cveMunicipio) {
		this.cveAlcaldia = cveMunicipio;
	}
	
	/**
	 * @param idMunicipio
	 * @param descripcion
	 */
	public CatMunicipiosDTO(Integer idMunicipio, String descripcion) {	
		this.idMunicipio = idMunicipio;
		this.descripcion = descripcion;
	}
	public CatMunicipiosDTO(Integer idMunicipio) {	
		this.idMunicipio = idMunicipio;
	}

	//Constructor para namedquery = findALL
	public CatMunicipiosDTO(Integer idMunicipio, String descripcion, String cveAlcaldia, int idEstado) {
		this.idMunicipio = idMunicipio;
		this.catEstadosDTO = new CatEstadosDTO(idEstado, null, null);
		this.descripcion = descripcion;
		this.cveAlcaldia = cveAlcaldia;
	}
	
	public CatMunicipiosDTO(Integer idMunicipio, String descripcion, Integer idEstado, String estado) {
		this.idMunicipio = idMunicipio;
		this.descripcion = descripcion;
		this.catEstadosDTO = new CatEstadosDTO(idEstado, estado);
	}

	/**
	 * @return the idMunicipio
	 */
	public Integer getIdMunicipio() {
		return idMunicipio;
	}

	/**
	 * @param idMunicipio the idMunicipio to set
	 */
	public void setIdMunicipio(Integer idMunicipio) {
		this.idMunicipio = idMunicipio;
	}

	/**
	 * @return the catEstadosDTO
	 */
	public CatEstadosDTO getCatEstadosDTO() {
		return catEstadosDTO;
	}

	/**
	 * @param catEstadosDTO the catEstadosDTO to set
	 */
	public void setCatEstadosDTO(CatEstadosDTO catEstadosDTO) {
		this.catEstadosDTO = catEstadosDTO;
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
	 * @return the cveAlcaldia
	 */
	public String getCveAlcaldia() {
		return cveAlcaldia;
	}

	/**
	 * @param cveAlcaldia the cveAlcaldia to set
	 */
	public void setCveAlcaldia(String cveAlcaldia) {
		this.cveAlcaldia = cveAlcaldia;
	}
	
	public static Integer getIdMunicipioByIdAlcaldiaAEFCM(String idAlcaldiaAEFCM) {
		Integer id = Constantes.INT_VALOR_CERO;		
		switch(idAlcaldiaAEFCM) {
			case "013" :
				id  = 2;
			      break;
			case "014" :
				id  = 3;
			      break;
			case "015" :
				id  = 4;
			      break;
			case "016" :
				id  = 5;
			      break;
			case "017" :
				id  = 6;
			      break;
			case "018" :
				id  = 7;
			      break;
			case "019" :
				id  = 8;
			      break;
			case "020" :
				id  = 9;
			      break;
			case "021" :
				id  = 10;
			      break;
			case "022" :
				id  = 11;
			      break;
			case "023" :
				id  = 12;
			      break;
			case "024" :
				id  = 13;
			      break;
			case "025" :
				id  = 14;
			      break;
			case "026" :
				id  = 15;
			      break;
			case "027" :
				id  = 16;
			      break;
			case "028" :
				id  = 17;
			      break;
			default:
				id  = 18;
			      break;
		}
		return id;
	}
	
}
