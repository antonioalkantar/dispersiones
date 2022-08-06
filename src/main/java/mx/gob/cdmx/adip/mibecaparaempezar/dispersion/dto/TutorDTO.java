package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto;

import java.io.InputStream;
import java.io.Serializable;

import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.util.Constantes;

public class TutorDTO implements Serializable {

	private static final long serialVersionUID = 2079762469689863320L;

	private Long idUsuarioLlaveCdmx;
//	private CatAsentamientosDTO catAsentamientosDTO;
//	private CatEstatusDTO catEstatusDTO;
//	private CatIdentificacionOficialDTO catIdentificacionOficialDTO;
//	private CatMunicipiosDTO catMunicipiosDTO;
//	private CatTipoIneDTO catTipoIneDTO;
//	private CatComprobanteDomicilioDTO catComprobanteDomicilioDTO;
	private String nombre;
	private String primerApellido;
	private String segundoApellido;
	private String curp;
	private String telefono;
	private String correo;
	private String archivoIdentificacion;
	private String ocr;
	private String calle;
	private String numInt;
	private String numExt;
	private String codigoPostal;
	private String archivoComprobanteDomicilio;
	private String cic;
	private String claveElector;
	private String numeroEmision;
	private String nacionalidad;
	private Boolean informacionGralCorrecto;
	private Boolean domicilioCorrecto;
	private String informacionGeneralObservaciones;
	private String domicilioObservaciones;
	private boolean esExtranjero;
	private String nombreCompletoTutor;
	private boolean esMayorTresBeneficiarios;
	private boolean esCasaHogar;
	private String nombreCasaHogar;
	private String motivoMayorTresBeneficiarios;
	private boolean documentoLlave;
	private Integer idDocumentoLlave;
	private String lada;

	// Auxiliares documentos
	private InputStream fileIdentifica;
	private byte[] contentFileIdentifica;
	private InputStream fileComprobanteDom;
	private byte[] contentFileComprobanteDom;

	public TutorDTO() {
//		catMunicipiosDTO = new CatMunicipiosDTO();
//		catIdentificacionOficialDTO = new CatIdentificacionOficialDTO();
//		catComprobanteDomicilioDTO = new CatComprobanteDomicilioDTO();
	}

	public TutorDTO(Long idUsuarioLlaveCdmx) {
		this.idUsuarioLlaveCdmx = idUsuarioLlaveCdmx;
	}

	public TutorDTO(Long idUsuarioLlaveCdmx, String nombre, String primerApellido, String segundoApellido, String curp,
			String telefono, String correo, Integer idIdentificacion, String descripcionIdentifica, Integer idCatLlave,
			String archivoIdentificacion, String ocr, String calle, String numInt, String numExt, String codigoPostal,
			Integer idComprobanteDomicilio, String descripcionComp, String archivoComprobanteDomicilio, Integer idMunicipio,
			Integer idAsentamiento, String cic, String claveElector, String numeroEmision, String nacionalidad,
			Boolean informacionGralCorrecto, Boolean domicilioCorrecto, Integer idEstatus, String descripcion,
			String informacionGeneralObservaciones, String domicilioObservaciones, Integer idTipoIne,
			boolean esExtranjero , boolean esMayorTresBeneficiarios, boolean esCasaHogar, String nombreCasaHogar,
			String motivoMayorTresBeneficiarios, boolean documentoLlave, Integer idDocumentoLlave
			) {

		this.idUsuarioLlaveCdmx = idUsuarioLlaveCdmx;
		this.nombre = nombre;
		this.primerApellido = primerApellido;
		this.segundoApellido = segundoApellido;
		this.nombreCompletoTutor = nombre + " " + primerApellido + " " + segundoApellido;
		this.curp = curp;
		this.telefono = telefono;
		this.correo = correo;
//		this.catIdentificacionOficialDTO = idIdentificacion != null
//				? new CatIdentificacionOficialDTO(idIdentificacion, descripcionIdentifica, true, idCatLlave)
//				: new CatIdentificacionOficialDTO();
		this.archivoIdentificacion = archivoIdentificacion;
		this.ocr = ocr;
		this.calle = calle;
		this.numInt = numInt;
		this.numExt = numExt;
		this.codigoPostal = codigoPostal;
//		this.catComprobanteDomicilioDTO = new CatComprobanteDomicilioDTO(idComprobanteDomicilio, descripcionComp);
		this.archivoComprobanteDomicilio = archivoComprobanteDomicilio;
//		this.catMunicipiosDTO = idMunicipio != null ? new CatMunicipiosDTO(idMunicipio) : null;
//		this.catAsentamientosDTO = idAsentamiento != null ? new CatAsentamientosDTO(idAsentamiento) : null;
		this.cic = cic;
		this.claveElector = claveElector;
		this.numeroEmision = numeroEmision;
		this.nacionalidad = nacionalidad;
		this.informacionGralCorrecto = informacionGralCorrecto;
		this.domicilioCorrecto = domicilioCorrecto;
//		this.catEstatusDTO = idEstatus != null ? new CatEstatusDTO(idEstatus, descripcion) : null;
		this.informacionGeneralObservaciones = informacionGeneralObservaciones;
		this.domicilioObservaciones = domicilioObservaciones;
//		this.catTipoIneDTO = idTipoIne != null ? new CatTipoIneDTO(idTipoIne) : null;
		this.esExtranjero = esExtranjero;
		this.esMayorTresBeneficiarios = esMayorTresBeneficiarios;
		this.esCasaHogar = esCasaHogar;
		this.nombreCasaHogar = nombreCasaHogar;
		this.motivoMayorTresBeneficiarios = motivoMayorTresBeneficiarios;
		this.documentoLlave = documentoLlave;
		this.idDocumentoLlave = idDocumentoLlave;
	}

	public TutorDTO(Long idUsuarioLlaveCdmx, String nombre, String primerApellido, String segundoApellido, String curp,
			String telefono, String correo, boolean esExtranjero) {

		this.idUsuarioLlaveCdmx = idUsuarioLlaveCdmx;
		this.nombre = nombre;
		this.primerApellido = primerApellido;
		this.segundoApellido = segundoApellido;
		this.nombreCompletoTutor = nombre + " " + primerApellido + " " + segundoApellido;
		this.curp = curp;
		this.telefono = telefono;
		this.correo = correo;
		this.esExtranjero = esExtranjero;
	}
	
	public TutorDTO(Long idUsuarioLlaveCdmx, String nombre, String primerApellido, String segundoApellido, String curp) {
		this.idUsuarioLlaveCdmx = idUsuarioLlaveCdmx;
		this.nombreCompletoTutor = nombre + " " + primerApellido + " " + segundoApellido;
		this.curp = curp;
	}
	
	public TutorDTO(Long idUsuarioLlaveCdmx, String nombre, String primerApellido, String segundoApellido, String curp, Integer idEstatusTutor, String estatusTutor) {
		this.idUsuarioLlaveCdmx = idUsuarioLlaveCdmx;
		this.nombreCompletoTutor = nombre + " " + primerApellido + " " + segundoApellido;
		this.curp = curp;
//		this.catEstatusDTO = new CatEstatusDTO(idEstatusTutor, estatusTutor);
	}
	
	public TutorDTO(String nombre, String primerApellido, String segundoApellido) {
		this.nombre = nombre;
		this.primerApellido = primerApellido;
		this.segundoApellido = segundoApellido;
	}

	public Long getIdUsuarioLlaveCdmx() {
		return idUsuarioLlaveCdmx;
	}

	public void setIdUsuarioLlaveCdmx(Long idUsuarioLlaveCdmx) {
		this.idUsuarioLlaveCdmx = idUsuarioLlaveCdmx;
	}

//	public CatAsentamientosDTO getCatAsentamientosDTO() {
//		return catAsentamientosDTO;
//	}
//
//	public void setCatAsentamientosDTO(CatAsentamientosDTO catAsentamientosDTO) {
//		this.catAsentamientosDTO = catAsentamientosDTO;
//	}
//
//	public CatEstatusDTO getCatEstatusDTO() {
//		return catEstatusDTO;
//	}
//
//	public void setCatEstatusDTO(CatEstatusDTO catEstatusDTO) {
//		this.catEstatusDTO = catEstatusDTO;
//	}
//
//	public CatIdentificacionOficialDTO getCatIdentificacionOficialDTO() {
//		return catIdentificacionOficialDTO;
//	}
//
//	public void setCatIdentificacionOficialDTO(CatIdentificacionOficialDTO catIdentificacionOficialDTO) {
//		this.catIdentificacionOficialDTO = catIdentificacionOficialDTO;
//	}
//
//	public CatMunicipiosDTO getCatMunicipiosDTO() {
//		return catMunicipiosDTO;
//	}
//
//	public void setCatMunicipiosDTO(CatMunicipiosDTO catMunicipiosDTO) {
//		this.catMunicipiosDTO = catMunicipiosDTO;
//	}
//
//	public CatTipoIneDTO getCatTipoIneDTO() {
//		return catTipoIneDTO;
//	}
//
//	public void setCatTipoIneDTO(CatTipoIneDTO catTipoIneDTO) {
//		this.catTipoIneDTO = catTipoIneDTO;
//	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPrimerApellido() {
		return primerApellido;
	}

	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}

	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	public String getCurp() {
		return curp;
	}

	public void setCurp(String curp) {
		this.curp = curp;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getArchivoIdentificacion() {
		return archivoIdentificacion;
	}

	public void setArchivoIdentificacion(String archivoIdentificacion) {
		this.archivoIdentificacion = archivoIdentificacion;
	}

	public String getOcr() {
		return ocr;
	}

	public void setOcr(String ocr) {
		this.ocr = ocr;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public String getNumInt() {
		return numInt;
	}

	public void setNumInt(String numInt) {
		this.numInt = numInt;
	}

	public String getNumExt() {
		return numExt;
	}

	public void setNumExt(String numExt) {
		this.numExt = numExt;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

//	public CatComprobanteDomicilioDTO getCatComprobanteDomicilioDTO() {
//		return catComprobanteDomicilioDTO;
//	}
//
//	public void setCatComprobanteDomicilioDTO(CatComprobanteDomicilioDTO catComprobanteDomicilioDTO) {
//		this.catComprobanteDomicilioDTO = catComprobanteDomicilioDTO;
//	}

	public String getArchivoComprobanteDomicilio() {
		return archivoComprobanteDomicilio;
	}

	public void setArchivoComprobanteDomicilio(String archivoComprobanteDomicilio) {
		this.archivoComprobanteDomicilio = archivoComprobanteDomicilio;
	}

	public String getCic() {
		return cic;
	}

	public void setCic(String cic) {
		this.cic = cic;
	}

	public String getClaveElector() {
		return claveElector;
	}

	public void setClaveElector(String claveElector) {
		this.claveElector = claveElector;
	}

	public String getNumeroEmision() {
		return numeroEmision;
	}

	public void setNumeroEmision(String numeroEmision) {
		this.numeroEmision = numeroEmision;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public Boolean getInformacionGralCorrecto() {
		return informacionGralCorrecto;
	}

	public void setInformacionGralCorrecto(Boolean informacionGralCorrecto) {
		this.informacionGralCorrecto = informacionGralCorrecto;
	}

	public Boolean getDomicilioCorrecto() {
		return domicilioCorrecto;
	}

	public void setDomicilioCorrecto(Boolean domicilioCorrecto) {
		this.domicilioCorrecto = domicilioCorrecto;
	}

	public String getInformacionGeneralObservaciones() {
		return informacionGeneralObservaciones;
	}

	public void setInformacionGeneralObservaciones(String informacionGeneralObservaciones) {
		this.informacionGeneralObservaciones = informacionGeneralObservaciones;
	}

	public String getDomicilioObservaciones() {
		return domicilioObservaciones;
	}

	public void setDomicilioObservaciones(String domicilioObservaciones) {
		this.domicilioObservaciones = domicilioObservaciones;
	}

	public boolean isEsExtranjero() {
		return esExtranjero;
	}

	public void setEsExtranjero(boolean esExtranjero) {
		this.esExtranjero = esExtranjero;
	}

	public InputStream getFileIdentifica() {
		return fileIdentifica;
	}

	public void setFileIdentifica(InputStream fileIdentifica) {
		this.fileIdentifica = fileIdentifica;
	}

	public byte[] getContentFileIdentifica() {
		return contentFileIdentifica;
	}

	public void setContentFileIdentifica(byte[] contentFileIdentifica) {
		this.contentFileIdentifica = contentFileIdentifica;
	}

	public InputStream getFileComprobanteDom() {
		return fileComprobanteDom;
	}

	public void setFileComprobanteDom(InputStream fileComprobanteDom) {
		this.fileComprobanteDom = fileComprobanteDom;
	}

	public byte[] getContentFileComprobanteDom() {
		return contentFileComprobanteDom;
	}

	public void setContentFileComprobanteDom(byte[] contentFileComprobanteDom) {
		this.contentFileComprobanteDom = contentFileComprobanteDom;
	}

	public String getNombreArchivoIdentificaCorto() {
		return archivoIdentificacion == null ? ""
				: archivoIdentificacion.contains(Constantes.SEPARADOR_RUTA)
						? archivoIdentificacion
								.substring(archivoIdentificacion.lastIndexOf(Constantes.SEPARADOR_RUTA) + 1)
						: archivoIdentificacion;
	}

	public String getNombreArchivoComprobanteDomCorto() {
		return archivoComprobanteDomicilio == null ? ""
				: archivoComprobanteDomicilio.contains(Constantes.SEPARADOR_RUTA)
						? archivoComprobanteDomicilio
								.substring(archivoComprobanteDomicilio.lastIndexOf(Constantes.SEPARADOR_RUTA) + 1)
						: archivoComprobanteDomicilio;
	}

	public String getNombreCompletoTutor() {
		return nombreCompletoTutor;
	}

	public void setNombreCompletoTutor(String nombreCompletoTutor) {
		this.nombreCompletoTutor = nombreCompletoTutor;
	}

	public boolean isEsMayorTresBeneficiarios() {
		return esMayorTresBeneficiarios;
	}

	public void setEsMayorTresBeneficiarios(boolean esMayorTresBeneficiarios) {
		this.esMayorTresBeneficiarios = esMayorTresBeneficiarios;
	}

	public boolean isEsCasaHogar() {
		return esCasaHogar;
	}

	public void setEsCasaHogar(boolean esCasaHogar) {
		this.esCasaHogar = esCasaHogar;
	}

	public String getNombreCasaHogar() {
		return nombreCasaHogar;
	}

	public void setNombreCasaHogar(String nombreCasaHogar) {
		this.nombreCasaHogar = nombreCasaHogar;
	}

	public String getMotivoMayorTresBeneficiarios() {
		return motivoMayorTresBeneficiarios;
	}

	public void setMotivoMayorTresBeneficiarios(String motivoMayorTresBeneficiarios) {
		this.motivoMayorTresBeneficiarios = motivoMayorTresBeneficiarios;
	}

	public boolean isDocumentoLlave() {
		return documentoLlave;
	}

	public void setDocumentoLlave(boolean documentoLlave) {
		this.documentoLlave = documentoLlave;
	}

	public Integer getIdDocumentoLlave() {
		return idDocumentoLlave;
	}

	public void setIdDocumentoLlave(Integer idDocumentoLlave) {
		this.idDocumentoLlave = idDocumentoLlave;
	}

	public String getLada() {
		return lada;
	}

	public void setLada(String lada) {
		this.lada = lada;
	}
}
