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
	private ContadorErroresSynchronized contadorErrores = new ContadorErroresSynchronized();

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

	@SuppressWarnings("deprecation")
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
				
				try {
					LOGGER.info("******************* SE INICIA PROCESO DEL CURP: " + beneficiario.getCurpBeneficiario());
					if (beneficiario.getEsExterno().booleanValue()) {
						LOGGER.info("******************* PADRON EXTERNO = TRUE y CON CURP: " + beneficiario.getCurpBeneficiario());
						// Revisamos en la bd el padron externo
						List<PadronExternoDTO> lstPadronExternoDTO = padronExternoDAO.buscarPorCurp(beneficiario.getCurpBeneficiario());
						if (lstPadronExternoDTO.size() > 0) {
							LOGGER.info("******************* SI EXISTE INFORMACION EN BD DEL PADRON *********************");
							PadronExternoDTO padronExterno = lstPadronExternoDTO.get(0);
							if (beneficiario.getIdEstatusBeneficiario().intValue() == 1 && beneficiario.getIdEstatusTutor().intValue() == Constantes.ID_ESTATUS_APROBADA && padronExterno.getEstatus().equalsIgnoreCase(Constantes.PADRON_EXTERNO_ACTIVO)) {
								if (beneficiario.getIdNivelEducativo().intValue() == Constantes.ID_OTRO) {
									agregarBeneficiarioQueNoSeDispersaraPorPadronExterno(beneficiariosSinDispersion, beneficiario);
								}
								if(beneficiario.getFechaRegistro().after(dispersion.getFechaEjecucion())) {
									agregarBeneficiarioQueNoSeDispersaraPorFechaRegistro(beneficiariosSinDispersion, beneficiario);
								}
								LOGGER.info("******************* DISPERSAMOS PADRON EXTERNO CON CURP: " + beneficiario.getCurpBeneficiario());
								agregarBeneficiarioQueSeDispersaraPorPadronExterno(beneficiariosConDispersion, beneficiario);
							} else {
								LOGGER.info("******************* NO DISPERSAMOS PADRON EXTERNO CON CURP: " + beneficiario.getCurpBeneficiario());
								agregarBeneficiarioQueNoSeDispersaraPorPadronExterno(beneficiariosSinDispersion, beneficiario);
							}
						} else {
							LOGGER.info("******************* NO EXISTE EN BD EL PADRON EXTERNO CON CURP: " + beneficiario.getCurpBeneficiario());
							agregarBeneficiarioQueNoSeDispersaraPorPadronExterno(beneficiariosSinDispersion, beneficiario);
						}
					}
				} catch (Exception e) {
					LOGGER.error("Ocurrio un error desconocido con Padron Externo: ", e);
					LOGGER.error("Ocurrio un error desconocido con Padron Externo DEL CURP :" + beneficiario.getCurpBeneficiario());
					// Se pasa la lista de beneficiarios sin dispersion automaticamente
					agregarBeneficiarioQueNoSeDispersaraPorErrorDesconocido(beneficiariosSinDispersion, beneficiario);
					continue;
				}
				
				try {
					if (!beneficiario.getEsExterno().booleanValue()) {
						// Ejecutar WS
						MciResponse respuesta = null;
						try {
							respuesta = EntidadEducativaSoapClient.consultarCurp(beneficiario.getCurpBeneficiario());
						} catch (Exception e) {
							LOGGER.error("Ocurrio un error en la respuesta del WS Autoridad Educativa: ", e);
							LOGGER.error("Ocurrio un error en la respuesta del WS Autoridad Educativa DEL CURP :" + beneficiario.getCurpBeneficiario());
							// Se pasa la lista de beneficiarios sin dispersion automaticamente porque el WS de Autoridad Educativa falla por alguna razon
							agregarBeneficiarioQueNoSeDispersaraPorErrorWS(beneficiariosSinDispersion, beneficiario, respuesta);
							continue;
						}
						LOGGER.info("******************* PADRON EXTERNO = FALSE y CON CURP: " + beneficiario.getCurpBeneficiario());
						// Validar que el beneficiario está vigente.
						if (respuesta.getEstatus().equalsIgnoreCase(Constantes.BENEFICIARIO_ACTIVO) && beneficiario.getIdEstatusTutor().intValue() == Constantes.ID_ESTATUS_APROBADA
								// && beneficiario.getNumeroCuenta() != null && !beneficiario.getNumeroCuenta().isEmpty()
							) {
							// Se valida si existen cambios para hacer la actualizacion y guardar la bitacora de cambios
							obtenerDiferencias(bitacoraCambios, beneficiario, respuesta);
							// Si el beneficiario esta vigente se pasa a la lista de beneficiarios con dispersion
							LOGGER.info("******************* BENEFICIARIO ACTIVO Y TUTOR APROBADO - CURP: " + beneficiario.getCurpBeneficiario());
							if (respuesta.getNivelEducativoFIBIEDCDMXId() == Constantes.ID_OTRO) {
								agregarBeneficiarioQueNoSeDispersara(beneficiariosSinDispersion, beneficiario, respuesta);
							}
							if(beneficiario.getFechaRegistro().after(dispersion.getFechaEjecucion())) {
								agregarBeneficiarioQueNoSeDispersaraPorFechaRegistro(beneficiariosSinDispersion, beneficiario);
							}
							if(!respuesta.getTipoEscuela().equalsIgnoreCase(Constantes.PUBLICA)) {
								agregarBeneficiarioQueNoSeDispersaraPorTipoEscuelaNoPublica(beneficiariosSinDispersion, beneficiario);
							}
							agregarBeneficiarioQueSeDispersara(beneficiariosConDispersion, beneficiario, respuesta);
						} else {
							if(!respuesta.getEstatus().equalsIgnoreCase(Constantes.BENEFICIARIO_LOCALIZADO)) {
								// Se valida si existen cambios para hacer la actualizacion y guardar la bitacora de cambios
								obtenerDiferencias(bitacoraCambios, beneficiario, respuesta);
							}
							LOGGER.info("******************* BENEFICIARIO NO ACTIVO O TUTOR NO APROBADO - CURP: " + beneficiario.getCurpBeneficiario());
							// Si no esta vigente, se pasa a la lista de beneficiarios sin dispersion
							agregarBeneficiarioQueNoSeDispersara(beneficiariosSinDispersion, beneficiario, respuesta);
						}
					}
				} catch (Exception e) {
					LOGGER.error("Ocurrio un error desconocido: ", e);
					LOGGER.error("Ocurrio un error desconocido DEL CURP :" + beneficiario.getCurpBeneficiario());
					// Se pasa la lista de beneficiarios sin dispersion automaticamente
					agregarBeneficiarioQueNoSeDispersaraPorErrorDesconocido(beneficiariosSinDispersion, beneficiario);
					continue;
				}
			}

			resultadosConDispersion = beneficiarioDispersionDAO.guardarLista(conn, beneficiariosConDispersion);
			LOGGER.info("DISPERSADOS: " + beneficiariosConDispersion.size());
			resultadosSinDispersion = beneficiarioSinDispersionDAO.guardarLista(conn, beneficiariosSinDispersion);
			LOGGER.info("SIN DISPERSAR: " + beneficiariosSinDispersion.size());

			// Se actualizan los registros y se persisten los registros de bitacora
			if (bitacoraCambios.size() > 0) {
				bitacoraDAO.actualizarDatosSolicitud(bitacoraCambios);
				resgitrosBitacora = bitacoraDAO.guardar(conn, bitacoraCambios);
				LOGGER.info("Registros en bitáctora actualizados: " + resgitrosBitacora.length);
			}
			
			conn.commit();
			conn.setAutoCommit(true);

			// Se suman los totales.
			resultadoEjecucionDTO.setTotalRegistros(lstBeneficiarios.size());
			resultadoEjecucionDTO.setTotalDispersados(resultadosConDispersion.length);
			resultadoEjecucionDTO.setTotalNoDispersados(resultadosSinDispersion.length);

			contadorErrores.mostrarCurpsConErroresPorHilo(Thread.currentThread().getName());
			
			ContadorProgresoSynchronized.incrementarAvance(resultadosConDispersion.length + resultadosSinDispersion.length);

			LOGGER.info("El hilo " + Thread.currentThread().getName() + " termino la ejecucion de "
					+ lstBeneficiarios.size() + " registros - Dispersados: " + resultadosConDispersion.length
					+ ", No Dispersados: " + resultadosSinDispersion.length);
		} catch (Exception e) {
			LOGGER.error("Ocurrio un error en la ejecucion del Thread: ", e);
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
		beneficiarioDispersion.setCurpTutor(beneficiario.getCurpTutor()); // CURP Tutor
		beneficiarioDispersion.setCurpBeneficiario(beneficiario.getCurpBeneficiario()); // CURP Beneficiario
		beneficiarioDispersion.setCatCicloEscolar(dispersion.getCatCicloEscolar()); // Ciclo Escolar
		beneficiarioDispersion.setCatPeriodoEscolar(dispersion.getCatPeriodoEscolar()); // Periodo Escolar
		beneficiarioDispersion.setCatNiveEducativo(new CatNivelEducativoDTO(respuesta.getNivelEducativoFIBIEDCDMXId())); // Nivel Educativo
		beneficiarioDispersion.setCatMontoApoyo(obtenerCatMontoApoyo((long) respuesta.getNivelEducativoFIBIEDCDMXId(),dispersion.getCatCicloEscolar().getIdCicloEscolar())); // Monto Apoyo
		beneficiarioDispersion.setFechaCreacion(new Date()); // Fecha Creacion
		if (dispersion.getCatTipoDispersion().getIdTipoDispersion().longValue() == Constantes.ID_TIPO_DISPERSION_COMPLEMENTARIA) {
			beneficiarioDispersion.setEsComplementaria(true); // Es complementaria
//			beneficiarioDispersion.setIdBeneficiarioSinDispersion(beneficiario.getIdBeneficiarioSinDispersion());
		} else {
			beneficiarioDispersion.setEsComplementaria(false); // Es complementaria
			beneficiarioDispersion.setIdBeneficiarioSinDispersion(null); // Beneficiario Sin Dispersion
		}
		beneficiarioDispersion.setNumeroCuenta(beneficiario.getNumeroCuenta());
		
		LOGGER.info("SI DISPERSAMOS AL CURP: " + beneficiario.getCurpBeneficiario());
		beneficiariosConDispersion.add(beneficiarioDispersion);
	}
	
	private void agregarBeneficiarioQueSeDispersaraPorPadronExterno(List<BeneficiarioDispersionDTO> beneficiariosConDispersion, BeneficiarioSolicitudTutorDTO beneficiario) {
		BeneficiarioDispersionDTO beneficiarioDispersion = new BeneficiarioDispersionDTO();
		beneficiarioDispersion.setDispersion(dispersion); // Dispersion
		beneficiarioDispersion.setCurpBeneficiario(beneficiario.getCurpBeneficiario()); // CURP Beneficiario
		beneficiarioDispersion.setCurpTutor(beneficiario.getCurpTutor()); // CURP Tutor
		beneficiarioDispersion.setCatCicloEscolar(dispersion.getCatCicloEscolar()); // Ciclo Escolar
		beneficiarioDispersion.setCatPeriodoEscolar(dispersion.getCatPeriodoEscolar()); // Periodo Escolar
		beneficiarioDispersion.setCatNiveEducativo(new CatNivelEducativoDTO(beneficiario.getIdNivelEducativo().intValue())); // Nivel Educativo
		beneficiarioDispersion.setCatMontoApoyo(obtenerCatMontoApoyo(beneficiario.getIdNivelEducativo(),dispersion.getCatCicloEscolar().getIdCicloEscolar())); // Monto Apoyo
		beneficiarioDispersion.setFechaCreacion(new Date()); // Fecha Creacion
		if (dispersion.getCatTipoDispersion().getIdTipoDispersion().longValue() == Constantes.ID_TIPO_DISPERSION_COMPLEMENTARIA) {
			beneficiarioDispersion.setEsComplementaria(true); // Es complementaria
//			beneficiarioDispersion.setIdBeneficiarioSinDispersion(beneficiario.getIdBeneficiarioSinDispersion());
		} else {
			beneficiarioDispersion.setEsComplementaria(false); // Es complementaria
			beneficiarioDispersion.setIdBeneficiarioSinDispersion(null); // Beneficiario Sin Dispersion
		}
		beneficiarioDispersion.setNumeroCuenta(beneficiario.getNumeroCuenta());
		
		LOGGER.info("SI DISPERSAMOS con Padron Externo AL CURP: " + beneficiario.getCurpBeneficiario());
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
			ContadorErroresSynchronized.agregarCurpNoDispersadaPorNumeroCuentaVacia(beneficiario.getCurpBeneficiario());
			contadorErrores.agregarCurpNoDispersadaPorNumeroCuentaVaciaPorHilo(beneficiario.getCurpBeneficiario());
			beneficiarioSinDispersion.setCatMotivoNoDispersion(new CatMotivoNoDispersionDTO(Constantes.BENEFICIARIO_SIN_NUMERO_CUENTA)); // Motivo No Dispersion Sin Cuenta
		} else if (beneficiario.getIdEstatusTutor().intValue() != Constantes.ID_ESTATUS_APROBADA) {
			ContadorErroresSynchronized.agregarCurpNoDispersadaPorTutorNoAprobado(beneficiario.getCurpBeneficiario());
			contadorErrores.agregarCurpNoDispersadaPorTutorNoAprobadoPorHilo(beneficiario.getCurpBeneficiario());
			beneficiarioSinDispersion.setCatMotivoNoDispersion(new CatMotivoNoDispersionDTO(Constantes.TUTOR_NO_APROBADO)); // Motivo No Dispersion Tutor No Aprobado
		} else if (!respuesta.getEstatus().equalsIgnoreCase(Constantes.BENEFICIARIO_ACTIVO)) {
			ContadorErroresSynchronized.agregarCurpNoDispersadaPorBeneficiarioNoActivo(beneficiario.getCurpBeneficiario());
			contadorErrores.agregarCurpNoDispersadaPorBeneficiarioNoActivoPorHilo(beneficiario.getCurpBeneficiario());
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
		if(respuesta.getNivelEducativoFIBIEDCDMXId() == Constantes.ID_OTRO) {
			LOGGER.info("SE INACTIVA POR NIVEL EDUCATIVO 99 AL CURP: " + beneficiario.getCurpBeneficiario() + " CON ID SOLICITUD: " + beneficiario.getIdSolicitud());
			dispersionDAO.desactivarBeneficiarioSolictud(beneficiario.getIdSolicitud());
		}
	}
	
	private void agregarBeneficiarioQueNoSeDispersaraPorFechaRegistro(List<BeneficiarioSinDispersionDTO> beneficiariosSinDispersion, BeneficiarioSolicitudTutorDTO beneficiario) {	
		BeneficiarioSinDispersionDTO beneficiarioSinDispersion = new BeneficiarioSinDispersionDTO();
		beneficiarioSinDispersion.setDispersion(dispersion); // Dispersion
		beneficiarioSinDispersion.setCurpBeneficiario(beneficiario.getCurpBeneficiario()); // CURP Beneficiario
		beneficiarioSinDispersion.setCurpTutor(beneficiario.getCurpTutor()); // CURP Tutor
		beneficiarioSinDispersion.setCatCicloEscolar(dispersion.getCatCicloEscolar()); // Ciclo Escolar
		beneficiarioSinDispersion.setCatPeriodoEscolar(dispersion.getCatPeriodoEscolar()); // Periodo Escolar
		beneficiarioSinDispersion.setCatNiveEducativo(new CatNivelEducativoDTO(beneficiario.getIdNivelEducativo().intValue())); // Nivel Educativo
		beneficiarioSinDispersion.setCatMotivoNoDispersion(new CatMotivoNoDispersionDTO(Constantes.FECHA_REGISTRO_ES_MENOR)); // Motivo No Dispersion Fallo Servicio Autoridad Educativa
		beneficiarioSinDispersion.setCatMontoApoyo(obtenerCatMontoApoyo(beneficiario.getIdNivelEducativo(),dispersion.getCatCicloEscolar().getIdCicloEscolar())); // Monto Apoyo
		beneficiarioSinDispersion.setFechaCreacion(new Date()); // Fecha Creacion
		beneficiarioSinDispersion.setIdBeneficiarioDispersion(null); // Beneficiario Dispersion
		beneficiarioSinDispersion.setNumeroCuenta(beneficiario.getNumeroCuenta());
		
		if(beneficiario.getIdNivelEducativo().intValue() == Constantes.ID_OTRO) {
			LOGGER.info("SE INACTIVA POR NIVEL EDUCATIVO 99 AL CURP: " + beneficiario.getCurpBeneficiario() + " CON ID SOLICITUD: " + beneficiario.getIdSolicitud());
			dispersionDAO.desactivarBeneficiarioSolictud(beneficiario.getIdSolicitud());
		}

		LOGGER.info("NO DISPERSAMOS POR FECHA REGISTRO con Padron Externo AL CURP: " + beneficiario.getCurpBeneficiario());
		beneficiariosSinDispersion.add(beneficiarioSinDispersion);
	}
	
	private void agregarBeneficiarioQueNoSeDispersaraPorTipoEscuelaNoPublica(List<BeneficiarioSinDispersionDTO> beneficiariosSinDispersion, BeneficiarioSolicitudTutorDTO beneficiario) {	
		BeneficiarioSinDispersionDTO beneficiarioSinDispersion = new BeneficiarioSinDispersionDTO();
		beneficiarioSinDispersion.setDispersion(dispersion); // Dispersion
		beneficiarioSinDispersion.setCurpBeneficiario(beneficiario.getCurpBeneficiario()); // CURP Beneficiario
		beneficiarioSinDispersion.setCurpTutor(beneficiario.getCurpTutor()); // CURP Tutor
		beneficiarioSinDispersion.setCatCicloEscolar(dispersion.getCatCicloEscolar()); // Ciclo Escolar
		beneficiarioSinDispersion.setCatPeriodoEscolar(dispersion.getCatPeriodoEscolar()); // Periodo Escolar
		beneficiarioSinDispersion.setCatNiveEducativo(new CatNivelEducativoDTO(beneficiario.getIdNivelEducativo().intValue())); // Nivel Educativo
		beneficiarioSinDispersion.setCatMotivoNoDispersion(new CatMotivoNoDispersionDTO(Constantes.ESCUELA_NO_PUBLICA)); // Motivo No Dispersion Fallo Servicio Autoridad Educativa
		beneficiarioSinDispersion.setCatMontoApoyo(obtenerCatMontoApoyo(beneficiario.getIdNivelEducativo(),dispersion.getCatCicloEscolar().getIdCicloEscolar())); // Monto Apoyo
		beneficiarioSinDispersion.setFechaCreacion(new Date()); // Fecha Creacion
		beneficiarioSinDispersion.setIdBeneficiarioDispersion(null); // Beneficiario Dispersion
		beneficiarioSinDispersion.setNumeroCuenta(beneficiario.getNumeroCuenta());
		
		if(beneficiario.getIdNivelEducativo().intValue() == Constantes.ID_OTRO) {
			LOGGER.info("SE INACTIVA POR NIVEL EDUCATIVO 99 AL CURP: " + beneficiario.getCurpBeneficiario() + " CON ID SOLICITUD: " + beneficiario.getIdSolicitud());
			dispersionDAO.desactivarBeneficiarioSolictud(beneficiario.getIdSolicitud());
		}

		LOGGER.info("NO DISPERSAMOS POR FECHA REGISTRO con Padron Externo AL CURP: " + beneficiario.getCurpBeneficiario());
		beneficiariosSinDispersion.add(beneficiarioSinDispersion);
	}
	
	private void agregarBeneficiarioQueNoSeDispersaraPorPadronExterno(List<BeneficiarioSinDispersionDTO> beneficiariosSinDispersion, BeneficiarioSolicitudTutorDTO beneficiario) {	
		BeneficiarioSinDispersionDTO beneficiarioSinDispersion = new BeneficiarioSinDispersionDTO();
		beneficiarioSinDispersion.setDispersion(dispersion); // Dispersion
		beneficiarioSinDispersion.setCurpBeneficiario(beneficiario.getCurpBeneficiario()); // CURP Beneficiario
		beneficiarioSinDispersion.setCurpTutor(beneficiario.getCurpTutor()); // CURP Tutor
		beneficiarioSinDispersion.setCatCicloEscolar(dispersion.getCatCicloEscolar()); // Ciclo Escolar
		beneficiarioSinDispersion.setCatPeriodoEscolar(dispersion.getCatPeriodoEscolar()); // Periodo Escolar
		beneficiarioSinDispersion.setCatNiveEducativo(new CatNivelEducativoDTO(beneficiario.getIdNivelEducativo().intValue())); // Nivel Educativo
		if (beneficiario.getNumeroCuenta() == null) {
			ContadorErroresSynchronized.agregarCurpNoDispersadaPorNumeroCuentaVacia(beneficiario.getCurpBeneficiario());
			contadorErrores.agregarCurpNoDispersadaPorNumeroCuentaVaciaPorHilo(beneficiario.getCurpBeneficiario());
			beneficiarioSinDispersion.setCatMotivoNoDispersion(new CatMotivoNoDispersionDTO(Constantes.BENEFICIARIO_SIN_NUMERO_CUENTA)); // Motivo No Dispersion Sin Cuenta
		} else if (beneficiario.getIdEstatusTutor().intValue() != Constantes.ID_ESTATUS_APROBADA) {
			ContadorErroresSynchronized.agregarCurpNoDispersadaPorTutorNoAprobado(beneficiario.getCurpBeneficiario());
			contadorErrores.agregarCurpNoDispersadaPorTutorNoAprobadoPorHilo(beneficiario.getCurpBeneficiario());
			beneficiarioSinDispersion.setCatMotivoNoDispersion(new CatMotivoNoDispersionDTO(Constantes.TUTOR_NO_APROBADO)); // Motivo No Dispersion Tutor No Aprobado
		}
		beneficiarioSinDispersion.setCatMontoApoyo(obtenerCatMontoApoyo(beneficiario.getIdNivelEducativo(),dispersion.getCatCicloEscolar().getIdCicloEscolar())); // Monto Apoyo
		beneficiarioSinDispersion.setFechaCreacion(new Date()); // Fecha Creacion
		beneficiarioSinDispersion.setIdBeneficiarioDispersion(null); // Beneficiario Dispersion
		beneficiarioSinDispersion.setNumeroCuenta(beneficiario.getNumeroCuenta());
		
		if(beneficiario.getIdNivelEducativo().intValue() == Constantes.ID_OTRO) {
			LOGGER.info("SE INACTIVA POR NIVEL EDUCATIVO 99 AL CURP: " + beneficiario.getCurpBeneficiario() + " CON ID SOLICITUD: " + beneficiario.getIdSolicitud());
			dispersionDAO.desactivarBeneficiarioSolictud(beneficiario.getIdSolicitud());
		}

		LOGGER.info("NO DISPERSAMOS con Padron Externo AL CURP: " + beneficiario.getCurpBeneficiario());
		beneficiariosSinDispersion.add(beneficiarioSinDispersion);
	}
	
	private void agregarBeneficiarioQueNoSeDispersaraPorErrorWS(List<BeneficiarioSinDispersionDTO> beneficiariosSinDispersion, BeneficiarioSolicitudTutorDTO beneficiario, MciResponse respuesta) {	
		BeneficiarioSinDispersionDTO beneficiarioSinDispersion = new BeneficiarioSinDispersionDTO();
		beneficiarioSinDispersion.setDispersion(dispersion); // Dispersion
		beneficiarioSinDispersion.setCurpBeneficiario(beneficiario.getCurpBeneficiario()); // CURP Beneficiario
		beneficiarioSinDispersion.setCurpTutor(beneficiario.getCurpTutor()); // CURP Tutor
		beneficiarioSinDispersion.setCatCicloEscolar(dispersion.getCatCicloEscolar()); // Ciclo Escolar
		beneficiarioSinDispersion.setCatPeriodoEscolar(dispersion.getCatPeriodoEscolar()); // Periodo Escolar
		beneficiarioSinDispersion.setCatNiveEducativo(new CatNivelEducativoDTO(beneficiario.getIdNivelEducativo().intValue())); // Nivel Educativo
		beneficiarioSinDispersion.setCatMotivoNoDispersion(new CatMotivoNoDispersionDTO(Constantes.FALLO_SERVICIO_AUTORIDAD_EDUCATIVA)); // Motivo No Dispersion Fallo Servicio Autoridad Educativa
		beneficiarioSinDispersion.setCatMontoApoyo(obtenerCatMontoApoyo(beneficiario.getIdNivelEducativo(),dispersion.getCatCicloEscolar().getIdCicloEscolar())); // Monto Apoyo
		beneficiarioSinDispersion.setFechaCreacion(new Date()); // Fecha Creacion
		beneficiarioSinDispersion.setIdBeneficiarioDispersion(null); // Beneficiario Dispersion
		beneficiarioSinDispersion.setNumeroCuenta(beneficiario.getNumeroCuenta());

		ContadorErroresSynchronized.agregarCurpNoDispersadaPorErrorWS(beneficiario.getCurpBeneficiario());
		contadorErrores.agregarCurpNoDispersadaPorErrorWSPorHilo(beneficiario.getCurpBeneficiario());
		
		LOGGER.info("NO DISPERSAMOS AL CURP POR FALLA EN WS: " + beneficiario.getCurpBeneficiario());
		beneficiariosSinDispersion.add(beneficiarioSinDispersion);
	}
	
	private void agregarBeneficiarioQueNoSeDispersaraPorErrorDesconocido(List<BeneficiarioSinDispersionDTO> beneficiariosSinDispersion, BeneficiarioSolicitudTutorDTO beneficiario) {	
		BeneficiarioSinDispersionDTO beneficiarioSinDispersion = new BeneficiarioSinDispersionDTO();
		beneficiarioSinDispersion.setDispersion(dispersion); // Dispersion
		beneficiarioSinDispersion.setCurpBeneficiario(beneficiario.getCurpBeneficiario()); // CURP Beneficiario
		beneficiarioSinDispersion.setCurpTutor(beneficiario.getCurpTutor()); // CURP Tutor
		beneficiarioSinDispersion.setCatCicloEscolar(dispersion.getCatCicloEscolar()); // Ciclo Escolar
		beneficiarioSinDispersion.setCatPeriodoEscolar(dispersion.getCatPeriodoEscolar()); // Periodo Escolar
		beneficiarioSinDispersion.setCatNiveEducativo(new CatNivelEducativoDTO(beneficiario.getIdNivelEducativo().intValue())); // Nivel Educativo
		
		ContadorErroresSynchronized.agregarCurpNoDispersadaPorNumeroCuentaVacia(beneficiario.getCurpBeneficiario());
		contadorErrores.agregarCurpNoDispersadaPorNumeroCuentaVaciaPorHilo(beneficiario.getCurpBeneficiario());
		beneficiarioSinDispersion.setCatMotivoNoDispersion(new CatMotivoNoDispersionDTO(Constantes.FALLO_ERROR_DESCONOCIDO)); // Motivo No Dispersion Sin Cuenta
		
		beneficiarioSinDispersion.setCatMontoApoyo(obtenerCatMontoApoyo(beneficiario.getIdNivelEducativo(),dispersion.getCatCicloEscolar().getIdCicloEscolar())); // Monto Apoyo
		beneficiarioSinDispersion.setFechaCreacion(new Date()); // Fecha Creacion
		beneficiarioSinDispersion.setIdBeneficiarioDispersion(null); // Beneficiario Dispersion
		beneficiarioSinDispersion.setNumeroCuenta(beneficiario.getNumeroCuenta());

		LOGGER.info("NO DISPERSAMOS POR ERROR DESCONOCIDO AL CURP: " + beneficiario.getCurpBeneficiario());
		beneficiariosSinDispersion.add(beneficiarioSinDispersion);
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
		int contador = 0;
		LOGGER.info("**************** INICIO DIFERENCIAS DEL CURP: " + beneficiarioSolicitud.getCurpBeneficiario());
		
		LOGGER.info("DIFERENCIA ESTATUS: Actual-" + beneficiarioSolicitud.getIdEstatusBeneficiario() + " Nuevo-" + entidadEducativa.getEstatus());
		// Se comparan los objetos para obtener las diferencias del estatus del beneficiario
		if (beneficiarioSolicitud.getIdEstatusBeneficiario().intValue() != Constantes.STATUS_BENEFICIARIO_ACTIVO.intValue()) {
			LOGGER.info("ENTRA A DIFERENCIA ESTATUS: Actual-" + beneficiarioSolicitud.getIdEstatusBeneficiario() + " Nuevo-" + entidadEducativa.getEstatus());
			if (entidadEducativa.getEstatus().equalsIgnoreCase(Constantes.BENEFICIARIO_ACTIVO)) {
				bitacora.setActualizaEstatus(true);
				bitacora.setEstatusBeneficiarioAnterior(Constantes.BENEFICIARIO_INACTIVO);
				bitacora.setEstatusBeneficiarioActualizado(entidadEducativa.getEstatus());
				++contador;
			}
		}

		LOGGER.info("DIFERENCIA NIVEL EDUCATIVO: Actual-" + beneficiarioSolicitud.getIdNivelEducativo() + " Nuevo-" + entidadEducativa.getNivelEducativoFIBIEDCDMXId());
		// Se comparan los objetos para obtener las diferencias del nivel educativo
		if (beneficiarioSolicitud.getIdNivelEducativo().intValue() != entidadEducativa.getNivelEducativoFIBIEDCDMXId()) {
			LOGGER.info("ENTRA A DIFERENCIA NIVEL EDUCATIVO: Actual-" + beneficiarioSolicitud.getIdNivelEducativo() + " Nuevo-" + entidadEducativa.getNivelEducativoFIBIEDCDMXId());
			bitacora.setActualizaNivelEducativo(true);
			bitacora.setIdNivelEducativoAnterior(beneficiarioSolicitud.getIdNivelEducativo());
			bitacora.setIdNivelEducativoActualizado((long) entidadEducativa.getNivelEducativoFIBIEDCDMXId());
			++contador;
		}

		LOGGER.info("DIFERENCIA CCT: Actual-" + beneficiarioSolicitud.getCctSolicitud() + " Nuevo-" + entidadEducativa.getCct());
		// Se comparan los objetos para obtener las diferencias del cct
		if (!beneficiarioSolicitud.getCctSolicitud().equalsIgnoreCase(entidadEducativa.getCct())) {
			LOGGER.info("ENTRA A DIFERENCIA CCT: Actual-" + beneficiarioSolicitud.getCctSolicitud() + " Nuevo-" + entidadEducativa.getCct());
			bitacora.setActualizaCct(true);
			bitacora.setCctAnterior(beneficiarioSolicitud.getCctSolicitud());
			bitacora.setCctActualizado(entidadEducativa.getCct());
			++contador;
		}

		LOGGER.info("DIFERENCIA TURNO: Actual-" + beneficiarioSolicitud.getTurnoSolicitud() + " Nuevo-" + entidadEducativa.getTurno());
		// Se comparan los objetos para obtener las diferencias del turno
		if (!beneficiarioSolicitud.getTurnoSolicitud().equals(entidadEducativa.getTurno())) {
			LOGGER.info("ENTRA A DIFERENCIA TURNO: Actual-" + beneficiarioSolicitud.getTurnoSolicitud() + " Nuevo-" + entidadEducativa.getTurno());
			bitacora.setActualizaTurno(true);
			bitacora.setIdTurnoAnterior(beneficiarioSolicitud.getTurnoSolicitud());
			bitacora.setIdTurnoActualizado(entidadEducativa.getTurno());
			++contador;
		}

		LOGGER.info("DIFERENCIA GRADO ESCOLAR: Actual-" + beneficiarioSolicitud.getGradoEscolarSolicitud() + " Nuevo-" + entidadEducativa.getGradoEscolar());
		// Se comparan los objetos para obtener las diferencias del grado escolar
		if (!beneficiarioSolicitud.getGradoEscolarSolicitud().equals(String.valueOf(entidadEducativa.getGradoEscolar()))) {
			LOGGER.info("ENTRA A DIFERENCIA GRADO ESCOLAR: Actual-" + beneficiarioSolicitud.getGradoEscolarSolicitud() + " Nuevo-" + entidadEducativa.getGradoEscolar());
			bitacora.setActualizaGradoEscolar(true);
			bitacora.setGradoEscolarAnterior(beneficiarioSolicitud.getGradoEscolarSolicitud());
			bitacora.setGradoEscolarActualizado(String.valueOf(entidadEducativa.getGradoEscolar()));
			++contador;
		}

		LOGGER.info("DIFERENCIA NOMBRE CCT: Actual-" + beneficiarioSolicitud.getNombreCctSolicitud() + " Nuevo-" + entidadEducativa.getNombreCCT());
		// Se comparan los objetos para obtener las diferencias del nombre cct
		if (!beneficiarioSolicitud.getNombreCctSolicitud().equalsIgnoreCase(entidadEducativa.getNombreCCT())) {
			LOGGER.info("ENTRA A DIFERENCIA NOMBRE CCT: Actual-" + beneficiarioSolicitud.getNombreCctSolicitud() + " Nuevo-" + entidadEducativa.getNombreCCT());
			bitacora.setActualizaNombre(true);
			bitacora.setNombreCctAnterior(beneficiarioSolicitud.getNombreCctSolicitud());
			bitacora.setNombreCctActualizado(entidadEducativa.getNombreCCT());
			++contador;
		}

		LOGGER.info("DIFERENCIA CALLE: Actual-" + beneficiarioSolicitud.getCalleSolicitud() + " Nuevo-" + entidadEducativa.getCalle());
		// Se comparan los objetos para obtener las diferencias de la calle
		if (!beneficiarioSolicitud.getCalleSolicitud().equalsIgnoreCase(entidadEducativa.getCalle())) {
			LOGGER.info("ENTRA A DIFERENCIA CALLE: Actual-" + beneficiarioSolicitud.getCalleSolicitud() + " Nuevo-" + entidadEducativa.getCalle());
			bitacora.setActualizaCalle(true);
			bitacora.setCalleCctAnterior(beneficiarioSolicitud.getCalleSolicitud());
			bitacora.setCalleCctActualizado(entidadEducativa.getCalle());
			++contador;
		}

		LOGGER.info("DIFERENCIA COLONIA: Actual-" + beneficiarioSolicitud.getColoniaSolicitud() + " Nuevo-" + entidadEducativa.getColonia());
		// Se comparan los objetos para obtener las diferencias de la colonia
		if (!beneficiarioSolicitud.getColoniaSolicitud().equalsIgnoreCase(entidadEducativa.getColonia())) {
			LOGGER.info("ENTRA A DIFERENCIA COLONIA: Actual-" + beneficiarioSolicitud.getColoniaSolicitud() + " Nuevo-" + entidadEducativa.getColonia());
			bitacora.setActualizaColonia(true);
			bitacora.setColoniaCctAnterior(beneficiarioSolicitud.getColoniaSolicitud());
			bitacora.setColoniaCctActualizado(entidadEducativa.getColonia());
			++contador;
		}

//		LOGGER.info("DIFERENCIA ALCALDIA: Actual-" + beneficiarioSolicitud.getAlcaldiaSolicitud() + " Nuevo-" + entidadEducativa.getAlcaldiaId());
//		// Se comparan los objetos para obtener las diferencias de la alcaldia
//		if (!beneficiarioSolicitud.getAlcaldiaSolicitud().toString().equals(entidadEducativa.getAlcaldiaId())) {
//			LOGGER.info("ENTRA A DIFERENCIA ALCALDIA: Actual-" + beneficiarioSolicitud.getAlcaldiaSolicitud() + " Nuevo-" + entidadEducativa.getAlcaldiaId());
//			bitacora.setActualizaAlcaldia(true);
//			bitacora.setIdAlcaldiaCctAnterior(beneficiarioSolicitud.getAlcaldiaSolicitud());
//			bitacora.setIdAlcaldiaCctActualizado(new Long(getIdMunicipioByIdAlcaldiaAEFCM(entidadEducativa.getAlcaldiaId())));
//		}

		LOGGER.info("DIFERENCIA CODIGO POSTAL: Actual-" + beneficiarioSolicitud.getCodigoPostalSolicitud() + " Nuevo-" + entidadEducativa.getCodigoPostal());
		// Se comparan los objetos para obtener las diferencias del codigoPostal
		if (!beneficiarioSolicitud.getCodigoPostalSolicitud().equalsIgnoreCase(entidadEducativa.getCodigoPostal())) {
			LOGGER.info("ENTRA A DIFERENCIA CODIGO POSTAL: Actual-" + beneficiarioSolicitud.getCodigoPostalSolicitud() + " Nuevo-" + entidadEducativa.getCodigoPostal());
			bitacora.setActualizaCodigoPostal(true);
			bitacora.setCodigoPostalCctAnterior(beneficiarioSolicitud.getCodigoPostalSolicitud());
			bitacora.setCodigoPostalCctActualizado(entidadEducativa.getCodigoPostal());
			++contador;
		}

		bitacora.setIdDispersion(dispersion.getIdDispersion());
		bitacora.setIdSolicitud(beneficiarioSolicitud.getIdSolicitud());
		bitacora.setIdBeneficiario(beneficiarioSolicitud.getIdBeneficiario());
		bitacora.setFechaRegistro(new Date());

		if (contador > 0) {
			lstBitacoraCambios.add(bitacora);
		}
		LOGGER.info("**************** FIN DIFERENCIAS DEL CURP: " + beneficiarioSolicitud.getCurpBeneficiario());
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
