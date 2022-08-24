package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.db.PostgresDatasource;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioDTO;
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
		strQuery.append("  b.curp_beneficiario as curpBeneficiario ");
		strQuery.append("FROM mibecaparaempezar.tutor t ");
		strQuery.append("INNER JOIN mibecaparaempezar.solicitud s ");
		strQuery.append("  on t.id_usuario_llave_cdmx = s.id_usuario_llave_cdmx ");
		strQuery.append("INNER JOIN mibecaparaempezar.crc_beneficiario_solicitud cbs ");
		strQuery.append("  on s.id_solicitud = cbs.id_solicitud ");
		strQuery.append("INNER JOIN mibecaparaempezar.beneficiario b ");
		strQuery.append("  on cbs.id_beneficiario = b.id_beneficiario ");
		strQuery.append("INNER JOIN mibecaparaempezar.det_cuenta_beneficiario dcb ");
		strQuery.append("  on dcb.id_beneficiario = b.id_beneficiario ");
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
		strQuery.append("  bsda.id_beneficiario_sin_dispersion as idBeneficiarioSinDispersion ");
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
		strQuery.append("INNER JOIN mibecaparaempezar.beneficiario_sin_dispersion bsda ");
		strQuery.append("on bsda.curp_beneficiario = b1.curp_beneficiario ");
		strQuery.append("INNER JOIN mibecaparaempezar.det_cuenta_beneficiario dcb ");
		strQuery.append("  on dcb.id_beneficiario = b1.id_beneficiario ");
		strQuery.append("WHERE bsda.id_dispersion = ").append(idDispersion);
		strQuery.append("	  	AND bsda.id_beneficiario_dispersion IS NULL ");
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

	private BeneficiarioSolicitudTutorDTO mapearBeneficiarioSolicitudTutorDTO(ResultSet rs, Boolean esComplementaria) throws SQLException {
		BeneficiarioSolicitudTutorDTO bst = new BeneficiarioSolicitudTutorDTO();
		bst.setIdUsuario(rs.getLong("idUsuario"));
		bst.setCurpTutor(rs.getString("curpTutor"));
		bst.setIdEstatusTutor(rs.getLong("idEstatusTutor"));
		bst.setNumeroCuenta(rs.getString("numeroCuenta"));
		bst.setIdNivelEducativo(rs.getLong("idNivelEducativo"));
		bst.setIdGradoEscolar(rs.getLong("idGradoEscolar"));
		bst.setIdBeneficiario(rs.getLong("idBeneficiario"));
		bst.setCurpBeneficiario(rs.getString("curpBeneficiario"));
		if(esComplementaria) {
			bst.setIdBeneficiarioSinDispersion(rs.getLong("idBeneficiarioSinDispersion"));
		}
		return bst;
	}

}
