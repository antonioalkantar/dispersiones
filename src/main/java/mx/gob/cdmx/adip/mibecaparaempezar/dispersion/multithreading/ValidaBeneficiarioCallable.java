package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.multithreading;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.Callable;

import javax.xml.ws.WebServiceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.client.EntidadEducativaSoapClient;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.client.MciResponse;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao.BeneficiarioDispersionDAO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao.BeneficiarioSinDispersionDAO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao.BitacoraDAO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao.DispersionDAO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao.PadronExternoDAO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.db.PostgresDatasource;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioDispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioDispersionReporteDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioSinDispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioSolicitudTutorDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BitacoraDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatMontoApoyoDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatMotivoNoDispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatMunicipiosDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatNivelEducativoDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.DispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.PadronExternoDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.ResultadoEjecucionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.util.Constantes;

/**
 * @author raul
 */
public class ValidaBeneficiarioCallable implements Callable<ResultadoEjecucionDTO> {

	private static final Logger LOGGER = LogManager.getLogger(ValidaBeneficiarioCallable.class);

	private List<BeneficiarioSolicitudTutorDTO> lstBeneficiarios;
	private DispersionDTO dispersion;
	private List<CatMontoApoyoDTO> lstCatMontoApoyo;
	private BeneficiarioDispersionDAO beneficiarioDispersionDAO;
	private BeneficiarioSinDispersionDAO beneficiarioSinDispersionDAO;
	private BitacoraDAO bitacoraDAO;
	private DispersionDAO dispersionDAO;
	private PadronExternoDAO padronExternoDAO;

	private int indexInicio;

	public ValidaBeneficiarioCallable(DispersionDTO dispersion, List<BeneficiarioSolicitudTutorDTO> lstBeneficiarios,
			int indexInicio, List<CatMontoApoyoDTO> lstCatMontoApoyo,
			BeneficiarioDispersionDAO beneficiarioDispersionDAO,
			BeneficiarioSinDispersionDAO beneficiarioSinDispersionDAO, DispersionDAO dispersionDAO,
			BitacoraDAO bitacoraDAO, PadronExternoDAO padronExternoDAO) {
		this.lstBeneficiarios = lstBeneficiarios;
		this.dispersion = dispersion;
		this.indexInicio = indexInicio;
		this.lstCatMontoApoyo = lstCatMontoApoyo;
		this.beneficiarioDispersionDAO = beneficiarioDispersionDAO;
		this.beneficiarioSinDispersionDAO = beneficiarioSinDispersionDAO;
		this.dispersionDAO = dispersionDAO;
		this.bitacoraDAO = bitacoraDAO;
		this.padronExternoDAO = padronExternoDAO;
	}

	@Override
	public ResultadoEjecucionDTO call() throws Exception {
		Connection conn = null;
		ResultadoEjecucionDTO resultadoEjecucionDTO = new ResultadoEjecucionDTO();

		List<BeneficiarioDispersionDTO> beneficiariosConDispersion = new ArrayList<>();
		List<BeneficiarioSinDispersionDTO> beneficiariosSinDispersion = new ArrayList<>();
		List<BitacoraDTO> bitacoraCambios = new ArrayList<>();

		try {
			conn = PostgresDatasource.getInstance().getConnection();
			conn.setAutoCommit(false);
			validaEstatusConexion(conn);

			// Se insertan todos los preparedStatements
			int[] resultadosConDispersion = null;
			int[] resultadosSinDispersion = null;
			int[] resgitrosBitacora = null;

			for (BeneficiarioSolicitudTutorDTO beneficiario : lstBeneficiarios) {

				// Ejecutar WS
				MciResponse respuesta = null;
				try {
					respuesta = EntidadEducativaSoapClient.consultarCurp(beneficiario.getCurpBeneficiario());
				} catch (Exception e) {
					LOGGER.error("Ocurrió un error en la respuesta del WS Autoridad Educativa:", e);
					// Se pasa la lista de beneficiarios sin dispersion automaticamente porque el WS de Autoridad Educativa falla por alguna razon
					agregarBeneficiarioQueNoSeDispersaraPorErrorWS(beneficiariosSinDispersion, beneficiario, respuesta);
					continue;
				}
				
				if (!beneficiario.getEsExterno().booleanValue()) {
					LOGGER.info("******************* PADRON EXTERNO = FALSE *********************");
					// Validar que el beneficiario está vigente.
					if (respuesta.getEstatus().equals(Constantes.BENEFICIARIO_ACTIVO)
							&& beneficiario.getIdEstatusTutor() == Constantes.ID_ESTATUS_APROBADA
							&& !(beneficiario.getNumeroCuenta() == null)) {
						// Se valida si existen cambios para hacer la actualizacion y guardar la bitacora de cambios
						obtenerDiferencias(bitacoraCambios, beneficiario, respuesta);
						// Si el beneficiario esta vigente se pasa a la lista de beneficiarios con dispersion
						LOGGER.info("******************* BENEFICIARIO ACTIVO Y TUTOR APROBADO - CURP: " + beneficiario.getCurpBeneficiario());
						agregarBeneficiarioQueSeDispersara(beneficiariosConDispersion, beneficiario, respuesta);
					} else {
						LOGGER.info("******************* BENEFICIARIO NO ACTIVO O TUTOR NO APROBADO - CURP: " + beneficiario.getCurpBeneficiario());
						// Si no esta vigente, se pasa a la lista de beneficiarios sin dispersion
						agregarBeneficiarioQueNoSeDispersara(beneficiariosSinDispersion, beneficiario, respuesta);
					}
				} else {
					LOGGER.info("******************* PADRON EXTERNO = TRUE *********************");
					// Revisamos en la bd el padron externo
					List<PadronExternoDTO> lstPadronExternoDTO = padronExternoDAO.buscarPorCurp(beneficiario.getCurpBeneficiario());
					if (lstPadronExternoDTO.size() > 0) {
						LOGGER.info("******************* SI EXISTE INFORMACION EN BD DEL PADRON *********************");
						PadronExternoDTO padronExterno = lstPadronExternoDTO.get(0);
						if (padronExterno.getEstatus().equals(Constantes.PADRON_EXTERNO_ACTIVO)) {
							agregarBeneficiarioQueSeDispersara(beneficiariosConDispersion, beneficiario, respuesta);
						} else {
							agregarBeneficiarioQueNoSeDispersara(beneficiariosSinDispersion, beneficiario, respuesta);
						}
					} else {
						LOGGER.info("******************* NO EXISTE EN BD EL PADRON EXTERNO CON CURP: " + beneficiario.getCurpBeneficiario());
						agregarBeneficiarioQueNoSeDispersara(beneficiariosSinDispersion, beneficiario, respuesta);
					}
				}
			}

			LOGGER.info("DISPERSADOS: " + beneficiariosConDispersion.size());
			LOGGER.info("SIN DISPERSAR: " + beneficiariosSinDispersion.size());
			resultadosConDispersion = beneficiarioDispersionDAO.guardarLista(conn, beneficiariosConDispersion);
			resultadosSinDispersion = beneficiarioSinDispersionDAO.guardarLista(conn, beneficiariosSinDispersion);

			// Se actualizan los registros y se persisten los registros de bitacora
			bitacoraDAO.actualizarDatosSolicitud(bitacoraCambios);
			resgitrosBitacora = bitacoraDAO.guardar(conn, bitacoraCambios);
			LOGGER.info("Registros en bitáctora actualizados: " + resgitrosBitacora.length);

			conn.commit();
			conn.setAutoCommit(true);

			// Se suman los totales.
			resultadoEjecucionDTO.setTotalRegistros(lstBeneficiarios.size());
			resultadoEjecucionDTO.setTotalDispersados(resultadosConDispersion.length);
			resultadoEjecucionDTO.setTotalNoDispersados(resultadosSinDispersion.length);

			ContadorProgresoSynchronized
					.incrementarAvance(resultadosConDispersion.length + resultadosSinDispersion.length);

			LOGGER.info("El hilo " + Thread.currentThread().getName() + " terminó la ejecución de "
					+ lstBeneficiarios.size() + " registros Dispersados: " + resultadosConDispersion.length
					+ ", No Dispersados: " + resultadosSinDispersion.length);
		} catch (Exception e) {
			LOGGER.error("Ocurrió un error en la ejecución del Thread: ", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					LOGGER.warn("No se pudo cerrar una conexión");
				}
			}
		}
		return resultadoEjecucionDTO;
	}
	
	private void agregarBeneficiarioQueSeDispersara(List<BeneficiarioDispersionDTO> beneficiariosConDispersion, BeneficiarioSolicitudTutorDTO beneficiario, MciResponse respuesta) {
		BeneficiarioDispersionDTO beneficiarioDispersion = new BeneficiarioDispersionDTO();
		beneficiarioDispersion.setDispersion(dispersion); // Dispersion
		beneficiarioDispersion.setCurpBeneficiario(beneficiario.getCurpBeneficiario()); // CURP Beneficiario
		beneficiarioDispersion.setCurpTutor(beneficiario.getCurpTutor()); // CURP Tutor
		beneficiarioDispersion.setCatCicloEscolar(dispersion.getCatCicloEscolar()); // Ciclo Escolar
		beneficiarioDispersion.setCatPeriodoEscolar(dispersion.getCatPeriodoEscolar()); // Periodo Escolar
		beneficiarioDispersion.setCatNiveEducativo(
				new CatNivelEducativoDTO(respuesta.getNivelEducativoFIBIEDCDMXId() != 0
						? respuesta.getNivelEducativoFIBIEDCDMXId()
						: beneficiario.getIdNivelEducativo().intValue())); // Nivel Educativo
		beneficiarioDispersion.setCatMontoApoyo(obtenerCatMontoApoyo(
				respuesta.getNivelEducativoFIBIEDCDMXId() != 0
						? (long) respuesta.getNivelEducativoFIBIEDCDMXId()
						: beneficiario.getIdNivelEducativo(),
				dispersion.getCatCicloEscolar().getIdCicloEscolar())); // Monto Apoyo
		beneficiarioDispersion.setFechaCreacion(new Date()); // Fecha Creacion
		if (dispersion.getCatTipoDispersion()
				.getIdTipoDispersion() == Constantes.ID_TIPO_DISPERSION_COMPLEMENTARIA) {
			beneficiarioDispersion.setEsComplementaria(true); // Es complementaria
		} else {
			beneficiarioDispersion.setEsComplementaria(false); // Es complementaria
		}
		beneficiarioDispersion.setIdBeneficiarioSinDispersion(null); // Beneficiario Sin Dispersion
		beneficiarioDispersion.setNumeroCuenta(beneficiario.getNumeroCuenta());
		if (dispersion.getCatTipoDispersion()
				.getIdTipoDispersion() == Constantes.ID_TIPO_DISPERSION_COMPLEMENTARIA) {
			beneficiarioDispersion
					.setIdBeneficiarioSinDispersion(beneficiario.getIdBeneficiarioSinDispersion());
		}

		LOGGER.info("SI DISPERSAMOS AL CURP: " + beneficiario.getCurpBeneficiario());
		beneficiariosConDispersion.add(beneficiarioDispersion);
	}
	
	private void agregarBeneficiarioQueNoSeDispersara(List<BeneficiarioSinDispersionDTO> beneficiariosSinDispersion, BeneficiarioSolicitudTutorDTO beneficiario, MciResponse respuesta) {	
		BeneficiarioSinDispersionDTO beneficiarioSinDispersion = new BeneficiarioSinDispersionDTO();
		beneficiarioSinDispersion.setDispersion(dispersion); // Dispersion
		beneficiarioSinDispersion.setCurpBeneficiario(beneficiario.getCurpBeneficiario()); // CURP Beneficiario
		beneficiarioSinDispersion.setCurpTutor(beneficiario.getCurpTutor()); // CURP Tutor
		beneficiarioSinDispersion.setCatCicloEscolar(dispersion.getCatCicloEscolar()); // Ciclo Escolar
		beneficiarioSinDispersion.setCatPeriodoEscolar(dispersion.getCatPeriodoEscolar()); // Periodo Escolar
		beneficiarioSinDispersion.setCatNiveEducativo(
				new CatNivelEducativoDTO(respuesta.getNivelEducativoFIBIEDCDMXId() != 0
						? respuesta.getNivelEducativoFIBIEDCDMXId()
						: beneficiario.getIdNivelEducativo().intValue())); // Nivel Educativo
		if (beneficiario.getNumeroCuenta() == null) {
			beneficiarioSinDispersion.setCatMotivoNoDispersion(new CatMotivoNoDispersionDTO(Constantes.BENEFICIARIO_SIN_NUMERO_CUENTA)); // Motivo No Dispersion Sin Cuenta
		} else if (beneficiario.getIdEstatusTutor() != Constantes.ID_ESTATUS_APROBADA) {
			beneficiarioSinDispersion.setCatMotivoNoDispersion(new CatMotivoNoDispersionDTO(Constantes.TUTOR_NO_APROBADO)); // Motivo No Dispersion Tutor No Aprobado
		} else if (!respuesta.getEstatus().equals(Constantes.BENEFICIARIO_ACTIVO)) {
			beneficiarioSinDispersion.setCatMotivoNoDispersion(new CatMotivoNoDispersionDTO(Constantes.BENEFICIARIO_NO_ACTIVO)); // Motivo No Dispersion Beneficiario No Activo
		}  
		beneficiarioSinDispersion.setCatMontoApoyo(obtenerCatMontoApoyo(
				respuesta.getNivelEducativoFIBIEDCDMXId() != 0
						? (long) respuesta.getNivelEducativoFIBIEDCDMXId()
						: beneficiario.getIdNivelEducativo(),
				dispersion.getCatCicloEscolar().getIdCicloEscolar())); // Monto Apoyo
		beneficiarioSinDispersion.setFechaCreacion(new Date()); // Fecha Creacion
		beneficiarioSinDispersion.setIdBeneficiarioDispersion(null); // Beneficiario Dispersion
		beneficiarioSinDispersion.setNumeroCuenta(beneficiario.getNumeroCuenta());

		LOGGER.info("NO DISPERSAMOS AL CURP: " + beneficiario.getCurpBeneficiario());
		beneficiariosSinDispersion.add(beneficiarioSinDispersion);
		
		if(!respuesta.getEstatus().equals(Constantes.BENEFICIARIO_ACTIVO)) {
			LOGGER.info("SE INACTIVA AL CURP: " + beneficiario.getCurpBeneficiario() + " CON ID SOLICITUD: " + beneficiario.getIdSolicitud());
			dispersionDAO.desactivarBeneficiarioSolictud(beneficiario.getIdSolicitud());
		}
	}
	
	private void agregarBeneficiarioQueNoSeDispersaraPorErrorWS(List<BeneficiarioSinDispersionDTO> beneficiariosSinDispersion, BeneficiarioSolicitudTutorDTO beneficiario, MciResponse respuesta) {	
		BeneficiarioSinDispersionDTO beneficiarioSinDispersion = new BeneficiarioSinDispersionDTO();
		beneficiarioSinDispersion.setDispersion(dispersion); // Dispersion
		beneficiarioSinDispersion.setCurpBeneficiario(beneficiario.getCurpBeneficiario()); // CURP Beneficiario
		beneficiarioSinDispersion.setCurpTutor(beneficiario.getCurpTutor()); // CURP Tutor
		beneficiarioSinDispersion.setCatCicloEscolar(dispersion.getCatCicloEscolar()); // Ciclo Escolar
		beneficiarioSinDispersion.setCatPeriodoEscolar(dispersion.getCatPeriodoEscolar()); // Periodo Escolar
		beneficiarioSinDispersion.setCatNiveEducativo(
				new CatNivelEducativoDTO(respuesta.getNivelEducativoFIBIEDCDMXId() != 0
						? respuesta.getNivelEducativoFIBIEDCDMXId()
						: beneficiario.getIdNivelEducativo().intValue())); // Nivel Educativo
		beneficiarioSinDispersion.setCatMotivoNoDispersion(new CatMotivoNoDispersionDTO(Constantes.FALLO_SERVICIO_AUTORIDAD_EDUCATIVA)); // Motivo No Dispersion Fallo Servicio Autoridad Educativa
		beneficiarioSinDispersion.setCatMontoApoyo(obtenerCatMontoApoyo(
				respuesta.getNivelEducativoFIBIEDCDMXId() != 0
						? (long) respuesta.getNivelEducativoFIBIEDCDMXId()
						: beneficiario.getIdNivelEducativo(),
				dispersion.getCatCicloEscolar().getIdCicloEscolar())); // Monto Apoyo
		beneficiarioSinDispersion.setFechaCreacion(new Date()); // Fecha Creacion
		beneficiarioSinDispersion.setIdBeneficiarioDispersion(null); // Beneficiario Dispersion
		beneficiarioSinDispersion.setNumeroCuenta(beneficiario.getNumeroCuenta());

		LOGGER.info("NO DISPERSAMOS AL CURP: " + beneficiario.getCurpBeneficiario());
		beneficiariosSinDispersion.add(beneficiarioSinDispersion);
		
		if(!respuesta.getEstatus().equals(Constantes.BENEFICIARIO_ACTIVO)) {
			LOGGER.info("SE INACTIVA AL CURP: " + beneficiario.getCurpBeneficiario() + " CON ID SOLICITUD: " + beneficiario.getIdSolicitud());
			dispersionDAO.desactivarBeneficiarioSolictud(beneficiario.getIdSolicitud());
		}
	}

	private void obtenerDiferencias(List<BitacoraDTO> lstBitacoraCambios,
			BeneficiarioSolicitudTutorDTO beneficiarioSolicitud, MciResponse entidadEducativa) {
		BitacoraDTO bitacora = new BitacoraDTO();
		bitacora.setActualizaCct(false);
		bitacora.setActualizaNombre(false);
		bitacora.setActualizaCalle(false);
		bitacora.setActualizaColonia(false);
		bitacora.setActualizaTurno(false);
		bitacora.setActualizaAlcaldia(false);
		bitacora.setActualizaNivelEducativo(false);
		bitacora.setActualizaGradoEscolar(false);
		bitacora.setActualizaCodigoPostal(false);
		bitacora.setActualizaEstatus(false);
		
		// Se comparan los objetos para obtener las diferencias del estatus del beneficiario
		if (beneficiarioSolicitud.getIdEstatusBeneficiario() != Constantes.STATUS_BENEFICIARIO_ACTIVO) {
			if (entidadEducativa.getEstatus().equals(Constantes.BENEFICIARIO_ACTIVO)) {
			bitacora.setActualizaEstatus(true);
			bitacora.setEstatusBeneficiarioAnterior(Constantes.BENEFICIARIO_INACTIVO);
			bitacora.setEstatusBeneficiarioActualizado(entidadEducativa.getEstatus());
			}
		}

		// Se comparan los objetos para obtener las diferencias del nivel educativo
		if (!beneficiarioSolicitud.getIdNivelEducativo().toString().equals(String.valueOf(entidadEducativa.getNivelEducativoFIBIEDCDMXId()))) {
			bitacora.setActualizaNivelEducativo(true);
			bitacora.setIdNivelEducativoAnterior(beneficiarioSolicitud.getIdNivelEducativo());
			bitacora.setIdNivelEducativoActualizado((long) entidadEducativa.getNivelEducativoFIBIEDCDMXId());
		}

		// Se comparan los objetos para obtener las diferencias del cct
		if (!beneficiarioSolicitud.getCctSolicitud().equals(entidadEducativa.getCct())) {
			bitacora.setActualizaCct(true);
			bitacora.setCctAnterior(beneficiarioSolicitud.getCctSolicitud());
			bitacora.setCctActualizado(entidadEducativa.getCct());
		}

		// Se comparan los objetos para obtener las diferencias del turno
		if (!beneficiarioSolicitud.getTurnoSolicitud().equals(String.valueOf(entidadEducativa.getTurnoId()))) {
			bitacora.setActualizaTurno(true);
			bitacora.setIdTurnoAnterior(beneficiarioSolicitud.getTurnoSolicitud());
			bitacora.setIdTurnoActualizado(String.valueOf(entidadEducativa.getTurnoId()));
		}

		// Se comparan los objetos para obtener las diferencias del grado escolar
		if (!beneficiarioSolicitud.getGradoEscolarSolicitud().toString().equals(String.valueOf(entidadEducativa.getGradoEscolar()))) {
			bitacora.setActualizaGradoEscolar(true);
			bitacora.setGradoEscolarAnterior(beneficiarioSolicitud.getGradoEscolarSolicitud());
			bitacora.setGradoEscolarActualizado(String.valueOf(entidadEducativa.getGradoEscolar()));
		}

		// Se comparan los objetos para obtener las diferencias del nombre cct
		if (!beneficiarioSolicitud.getNombreCctSolicitud().equals(entidadEducativa.getNombreCCT())) {
			bitacora.setActualizaNombre(true);
			bitacora.setNombreCctAnterior(beneficiarioSolicitud.getNombreCctSolicitud());
			bitacora.setNombreCctActualizado(entidadEducativa.getNombreCCT());
		}

		// Se comparan los objetos para obtener las diferencias de la calle
		if (!beneficiarioSolicitud.getCalleSolicitud().equals(entidadEducativa.getCalle())) {
			bitacora.setActualizaCalle(true);
			bitacora.setCalleCctAnterior(beneficiarioSolicitud.getCalleSolicitud());
			bitacora.setCalleCctActualizado(entidadEducativa.getCalle());
		}

		// Se comparan los objetos para obtener las diferencias de la colonia
		if (!beneficiarioSolicitud.getColoniaSolicitud().equals(entidadEducativa.getColonia())) {
			bitacora.setActualizaColonia(true);
			bitacora.setColoniaCctAnterior(beneficiarioSolicitud.getColoniaSolicitud());
			bitacora.setColoniaCctActualizado(entidadEducativa.getColonia());
		}

		// Se comparan los objetos para obtener las diferencias de la alcaldia
		if (!beneficiarioSolicitud.getAlcaldiaSolicitud().toString().equals(entidadEducativa.getAlcaldiaId())) {
			bitacora.setActualizaAlcaldia(true);
			bitacora.setIdAlcaldiaCctAnterior(beneficiarioSolicitud.getAlcaldiaSolicitud());
			bitacora.setIdAlcaldiaCctActualizado(new Long(getIdMunicipioByIdAlcaldiaAEFCM(entidadEducativa.getAlcaldiaId())));
		}

		// Se comparan los objetos para obtener las diferencias del codigoPostal
		if (!beneficiarioSolicitud.getCodigoPostalSolicitud().equals(entidadEducativa.getCodigoPostal())) {
			bitacora.setActualizaCodigoPostal(true);
			bitacora.setCodigoPostalCctAnterior(beneficiarioSolicitud.getCodigoPostalSolicitud());
			bitacora.setCodigoPostalCctActualizado(entidadEducativa.getCodigoPostal());
		}

		bitacora.setIdDispersion(dispersion.getIdDispersion());
		bitacora.setIdSolicitud(beneficiarioSolicitud.getIdSolicitud());
		bitacora.setIdBeneficiario(beneficiarioSolicitud.getIdBeneficiario());
		bitacora.setFechaRegistro(new Date());

		lstBitacoraCambios.add(bitacora);
	}
	
	public Integer getIdMunicipioByIdAlcaldiaAEFCM(String idAlcaldiaAEFCM) {
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

	private void validaEstatusConexion(Connection conn) throws SQLException {
		if (conn.isClosed()) {
			LOGGER.warn("Se dectectó conexión cerrada. Se renueva la conexión");
			conn = PostgresDatasource.getInstance().getConnection();
		}
	}

	private CatMontoApoyoDTO obtenerCatMontoApoyo(Long idNivelEducativo, Long idCicloEscolar) {
		return lstCatMontoApoyo.stream()
				.filter(cat -> cat.getCatNivelEducativoDTO().getIdNivel().intValue() == idNivelEducativo
						&& cat.getCatCicloEscolarDTO().getIdCicloEscolar() == idCicloEscolar)
				.findFirst().get();
	}

}
