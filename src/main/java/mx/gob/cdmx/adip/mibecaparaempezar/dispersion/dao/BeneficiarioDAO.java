package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.db.PostgresDatasource;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioDispersionReporteDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioSolicitudTutorDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatCicloEscolarDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatEstatusDispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatPeriodoEscolarDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatTipoDispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.DispersionDTO;

public class BeneficiarioDAO extends IBaseDAO<BeneficiarioDTO, Integer> {

	private static final Logger LOGGER = LogManager.getLogger(BeneficiarioDAO.class);

	@Override
	public BeneficiarioDTO buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BeneficiarioDTO> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void actualizar(BeneficiarioDTO e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void guardar(BeneficiarioDTO e) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<BeneficiarioDTO> buscarPorCriterios(BeneficiarioDTO e) {
		return null;
	}

	public List<BeneficiarioSolicitudTutorDTO> buscarBeneficiariosActivos() {
		StringBuilder strQuery = new StringBuilder();
		
		strQuery.append("SELECT ");
		strQuery.append("  t.id_usuario_llave_cdmx as idUsuario, ");
		strQuery.append("  t.curp as curpTutor, ");
		strQuery.append("  t.id_estatus as idEstatusTutor, ");
		strQuery.append("  dcb.numero_cuenta as numeroCuenta, ");
		strQuery.append("  s.id_nivel_educativo as idNivelEducativo, ");
		strQuery.append("  s.grado_escolar as idGradoEscolar, ");
		strQuery.append("  b.id_beneficiario as idBeneficiario, ");
		strQuery.append("  b.curp_beneficiario as curpBeneficiario, ");
		strQuery.append("  b.fecha_registro as fechaRegistro, ");
		strQuery.append("  s.id_solicitud as idSolicitud, ");
		strQuery.append("  s.id_estatus_beneficiario as idEstatusBeneficiario, ");
		strQuery.append("  s.externo as esExterno, ");
		strQuery.append("  s.cct as cct,  ");
		strQuery.append("  s.turno as turno,  ");
		strQuery.append("  s.grado_escolar as gradoEscolar, "); 
		strQuery.append("  s.nombre as nombreCct, ");
		strQuery.append("  s.calle as calle,  ");
		strQuery.append("  s.colonia as colonia,  ");
		strQuery.append("  s.id_alcaldia as alcaldia,  ");
		strQuery.append("  s.codigopostal as codigoPostal  ");
		strQuery.append("FROM mibecaparaempezar.tutor t ");
		strQuery.append("INNER JOIN mibecaparaempezar.solicitud s ");
		strQuery.append("  on t.id_usuario_llave_cdmx = s.id_usuario_llave_cdmx ");
		strQuery.append("INNER JOIN mibecaparaempezar.crc_beneficiario_solicitud cbs ");
		strQuery.append("  on s.id_solicitud = cbs.id_solicitud ");
		strQuery.append("INNER JOIN mibecaparaempezar.beneficiario b ");
		strQuery.append("  on cbs.id_beneficiario = b.id_beneficiario ");
		strQuery.append("LEFT JOIN mibecaparaempezar.det_cuenta_beneficiario dcb ");
		strQuery.append("  on dcb.id_beneficiario = b.id_beneficiario ");
		strQuery.append("INNER JOIN mibecaparaempezar.cat_estatus ce ");
		strQuery.append("  on ce.id_estatus = t.id_estatus  ");
		strQuery.append("INNER JOIN mibecaparaempezar.cat_municipios cm ");
		strQuery.append("  on cm.id_municipio = s.id_alcaldia  ");
		strQuery.append("INNER JOIN mibecaparaempezar.encuesta e  ");
		strQuery.append("  on e.id_solicitud = s.id_solicitud  ");
		strQuery.append("INNER JOIN mibecaparaempezar.cat_estatus_beneficiario ceb  ");
		strQuery.append("  on ceb.id_estatus_beneficiario = s.id_estatus_beneficiario  ");
		strQuery.append("INNER JOIN mibecaparaempezar.cat_ciclo_escolar cce  ");
		strQuery.append("  on cce.id_ciclo_escolar = e.id_ciclo_escolar  ");
		strQuery.append("WHERE ce.id_estatus !=  1 AND cce.estatus = true  ");
//		strQuery.append("AND s.id_nivel_educativo = 1 and t.id_estatus = 6  ");
//		strQuery.append("AND b.curp_beneficiario = 'MAMA170901HDFRNNA1'");
		strQuery.append("ORDER BY ");
		strQuery.append("  s.fecha_solicitud ASC ");

		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;

		List<BeneficiarioSolicitudTutorDTO> lstBeneficiariosSolTutorDTO = new ArrayList<>();
		try {
			conn = PostgresDatasource.getInstance().getConnection();
			stm = conn.createStatement();
			rs = stm.executeQuery(strQuery.toString());
			while (rs.next()) {
				lstBeneficiariosSolTutorDTO.add(mapearBeneficiarioSolicitudTutorDTO(rs, false));
			}
		} catch (SQLException e1) {
			LOGGER.error("Ocurrió un error al consultar dispersiones con el query [" + strQuery.toString() + "]:", e1);
		} finally {
			PostgresDatasource.getInstance().close(rs, stm, conn);
		}
		return lstBeneficiariosSolTutorDTO;
	}
	
	public List<BeneficiarioSolicitudTutorDTO> buscarBeneficiariosActivosComplementaria(long idDispersion) {
		StringBuilder strQuery = new StringBuilder();

		strQuery.append("SELECT ");
		strQuery.append("  t.id_usuario_llave_cdmx as idUsuario, ");
		strQuery.append("  t.curp as curpTutor, ");
		strQuery.append("  t.id_estatus as idEstatusTutor, ");
		strQuery.append("  dcb.numero_cuenta as numeroCuenta, ");
		strQuery.append("  s.id_nivel_educativo as idNivelEducativo, ");
		strQuery.append("  s.grado_escolar as idGradoEscolar, ");
		strQuery.append("  b1.id_beneficiario as idBeneficiario, ");
		strQuery.append("  b1.curp_beneficiario as curpBeneficiario, ");
		strQuery.append("  b1.fecha_registro as fechaRegistro, ");
		strQuery.append("  s.id_solicitud as idSolicitud, ");
		strQuery.append("  s.id_estatus_beneficiario as idEstatusBeneficiario, ");
		strQuery.append("  s.externo as esExterno, ");
		strQuery.append("  s.cct as cct, ");
		strQuery.append("  s.turno as turno, ");
		strQuery.append("  s.grado_escolar as gradoEscolar, ");
		strQuery.append("  s.nombre as nombreCct, ");
		strQuery.append("  s.calle as calle, ");
		strQuery.append("  s.colonia as colonia, ");
		strQuery.append("  s.id_alcaldia as alcaldia, ");
		strQuery.append("  s.codigopostal as codigoPostal ");
//		strQuery.append("  bsda.id_beneficiario_sin_dispersion as idBeneficiarioSinDispersion ");
		strQuery.append("FROM mibecaparaempezar.tutor t ");
		strQuery.append("INNER JOIN mibecaparaempezar.solicitud s ");
		strQuery.append("  on t.id_usuario_llave_cdmx = s.id_usuario_llave_cdmx ");
		strQuery.append("INNER JOIN mibecaparaempezar.crc_beneficiario_solicitud cbs ");
		strQuery.append("  on s.id_solicitud = cbs.id_solicitud ");
		strQuery.append("INNER JOIN ( ");
		strQuery.append(" select b1.* from ( ");
		strQuery.append(" select * from mibecaparaempezar.beneficiario b WHERE b.curp_beneficiario IN ( ");
		strQuery.append("  	select bs.curp_beneficiario from mibecaparaempezar.beneficiario_sin_dispersion bs where bs.id_dispersion = ").append(idDispersion); 
		strQuery.append("  	AND bs.id_beneficiario_dispersion IS NULL");
		strQuery.append("  )) as b1 ");
		strQuery.append(" ) b1 on b1.id_beneficiario = cbs.id_beneficiario ");
//		strQuery.append("INNER JOIN mibecaparaempezar.beneficiario_sin_dispersion bsda ");
//		strQuery.append("  on bsda.curp_beneficiario = b1.curp_beneficiario ");
		strQuery.append("LEFT JOIN mibecaparaempezar.det_cuenta_beneficiario dcb ");
		strQuery.append("  on dcb.id_beneficiario = b1.id_beneficiario ");
		strQuery.append("INNER JOIN mibecaparaempezar.cat_estatus ce ");
		strQuery.append("  on ce.id_estatus = t.id_estatus  ");
		strQuery.append("INNER JOIN mibecaparaempezar.cat_municipios cm  ");
		strQuery.append("  on cm.id_municipio = s.id_alcaldia  ");
		strQuery.append("INNER JOIN mibecaparaempezar.encuesta e  ");
		strQuery.append("  on e.id_solicitud = s.id_solicitud  ");
		strQuery.append("INNER JOIN mibecaparaempezar.cat_estatus_beneficiario ceb  ");
		strQuery.append("  on ceb.id_estatus_beneficiario = s.id_estatus_beneficiario  ");
		strQuery.append("INNER JOIN mibecaparaempezar.cat_ciclo_escolar cce ");
		strQuery.append("  on cce.id_ciclo_escolar = e.id_ciclo_escolar ");
		strQuery.append("WHERE  ");
//		strQuery.append("WHERE bsda.id_dispersion = ").append(idDispersion);
//		strQuery.append("  AND bsda.id_beneficiario_dispersion IS NULL ");
//		strQuery.append("  AND ce.id_estatus !=  1 AND cce.estatus = true ");
		strQuery.append("  ce.id_estatus !=  1 AND cce.estatus = true ");
		strQuery.append("ORDER BY ");
		strQuery.append("  s.fecha_solicitud asc; ");

		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;

		List<BeneficiarioSolicitudTutorDTO> lstBeneficiariosSolTutorDTO = new ArrayList<>();
		try {
			conn = PostgresDatasource.getInstance().getConnection();
			stm = conn.createStatement();
			rs = stm.executeQuery(strQuery.toString());
			while (rs.next()) {
				lstBeneficiariosSolTutorDTO.add(mapearBeneficiarioSolicitudTutorDTO(rs, true));
			}
		} catch (SQLException e1) {
			LOGGER.error("Ocurrió un error al consultar dispersiones con el query [" + strQuery.toString() + "]:", e1);
		} finally {
			PostgresDatasource.getInstance().close(rs, stm, conn);
		}
		return lstBeneficiariosSolTutorDTO;
	}
	
	public List<BeneficiarioDispersionReporteDTO> consultarBeneficiariosDispersadosPorIdDispersion(DispersionDTO dispersion) {
		StringBuilder strQuery = new StringBuilder();

		strQuery.append(" SELECT ");
		strQuery.append(" t2.id_nivel_educativo AS idNivelEducativo, ");
		strQuery.append(" 	t.curp AS curpTutor, ");
		strQuery.append(" 	t2.curp_beneficiario AS curpBeneficiario, ");
		strQuery.append(" 	dcb.numero_cuenta AS numeroCuenta, ");
		strQuery.append(" 	t2.monto AS monto ");
		strQuery.append(" FROM ( ");
		strQuery.append(" 	SELECT t1.curp_beneficiario, SUM(t1.monto) AS monto, t1.id_nivel_educativo FROM ( ");
		strQuery.append(" 				SELECT bd.id_dispersion, bd.id_nivel_educativo, bd.curp_beneficiario, cma.monto FROM mibecaparaempezar.beneficiario_dispersion bd ");
		strQuery.append(" 				INNER JOIN mibecaparaempezar.cat_monto_apoyo cma ");
		strQuery.append(" 				ON cma.id_monto_apoyo = bd.id_monto_apoyo ");
		strQuery.append(" 	ORDER BY bd.curp_beneficiario ASC) t1 WHERE t1.id_dispersion = ? ");
		strQuery.append(" GROUP BY t1.curp_beneficiario, t1.monto, t1.id_dispersion, t1.id_nivel_educativo) t2 ");
		strQuery.append(" INNER JOIN mibecaparaempezar.beneficiario b ");
		strQuery.append(" ON b.curp_beneficiario = t2.curp_beneficiario ");
		strQuery.append(" INNER JOIN mibecaparaempezar.det_cuenta_beneficiario dcb ");
		strQuery.append(" ON dcb.id_beneficiario = b.id_beneficiario ");
		strQuery.append(" INNER JOIN mibecaparaempezar.crc_beneficiario_solicitud cbs ");
		strQuery.append(" ON b.id_beneficiario = cbs.id_beneficiario ");
		strQuery.append(" INNER JOIN mibecaparaempezar.solicitud s ");
		strQuery.append(" ON cbs.id_solicitud = s.id_solicitud ");
		strQuery.append(" INNER JOIN mibecaparaempezar.tutor t ");
		strQuery.append(" ON t.id_usuario_llave_cdmx = s.id_usuario_llave_cdmx; ");

		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<BeneficiarioDispersionReporteDTO> lstBeneficiariosDispersionReporteDTO = new ArrayList<>();
		try {
			conn = PostgresDatasource.getInstance().getConnection();
			pstm = conn.prepareStatement(strQuery.toString());
			pstm.setLong(1, dispersion.getIdDispersion());
			rs = pstm.executeQuery();
			while (rs.next()) {
				lstBeneficiariosDispersionReporteDTO.add(mapearBeneficiarioDispersionReporteDTO(rs));
			}
		} catch (SQLException e1) {
			LOGGER.error(
					"Ocurrió un error al consultar la información de los beneficiarios dispersados [" + strQuery.toString() + "]:",
					e1);
		} finally {
			PostgresDatasource.getInstance().close(null, pstm, conn);
		}
		return lstBeneficiariosDispersionReporteDTO;
	}

	private BeneficiarioSolicitudTutorDTO mapearBeneficiarioSolicitudTutorDTO(ResultSet rs, Boolean esComplementaria) throws SQLException {
		BeneficiarioSolicitudTutorDTO bst = new BeneficiarioSolicitudTutorDTO();
		bst.setIdUsuario(rs.getLong("idUsuario"));
		bst.setCurpTutor(rs.getString("curpTutor"));
		bst.setIdEstatusTutor(rs.getLong("idEstatusTutor"));
		bst.setNumeroCuenta(rs.getString("numeroCuenta"));
		bst.setIdNivelEducativo(rs.getLong("idNivelEducativo"));
		bst.setIdGradoEscolar(rs.getString("idGradoEscolar"));
		bst.setIdBeneficiario(rs.getLong("idBeneficiario"));
		bst.setCurpBeneficiario(rs.getString("curpBeneficiario"));
		bst.setFechaRegistro(rs.getDate("fechaRegistro"));
		bst.setIdSolicitud(rs.getLong("idSolicitud"));
		bst.setIdEstatusBeneficiario(rs.getLong("idEstatusBeneficiario"));
		bst.setEsExterno(rs.getBoolean("esExterno"));
		bst.setCctSolicitud(rs.getString("cct"));
		bst.setTurnoSolicitud(rs.getString("turno"));
		bst.setGradoEscolarSolicitud(rs.getString("gradoEscolar"));
		bst.setNombreCctSolicitud(rs.getString("nombreCct"));
		bst.setCalleSolicitud(rs.getString("calle"));
		bst.setColoniaSolicitud(rs.getString("colonia"));
		bst.setAlcaldiaSolicitud(rs.getLong("alcaldia"));
		bst.setCodigoPostalSolicitud(rs.getString("codigoPostal"));
//		if(esComplementaria) {
//			bst.setIdBeneficiarioSinDispersion(rs.getLong("idBeneficiarioSinDispersion"));
//		}
		return bst;
	}
	
	private BeneficiarioDispersionReporteDTO mapearBeneficiarioDispersionReporteDTO(ResultSet rs) throws SQLException {
		BeneficiarioDispersionReporteDTO bdr = new BeneficiarioDispersionReporteDTO();
		bdr.setIdNivelEducativo(rs.getInt("idNivelEducativo"));
		bdr.setCurpTutor(rs.getString("curpTutor"));
		bdr.setCurpBeneficiario(rs.getString("curpBeneficiario"));
		bdr.setNumeroCuenta(rs.getString("numeroCuenta"));
		bdr.setMonto(rs.getDouble("monto"));
		return bdr;
	}

}
