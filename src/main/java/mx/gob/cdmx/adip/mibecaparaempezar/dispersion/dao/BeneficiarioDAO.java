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
		strQuery.append("  t.id_estatus as estatusTutor, ");
		strQuery.append("  s.cct as numeroCuenta, ");
		strQuery.append("  s.id_nivel_educativo as idNivelEducativo, ");
		strQuery.append("  s.grado_escolar as idGradoEscolar, ");
		strQuery.append("  b.id_beneficiario as idBeneficiario, ");
		strQuery.append("  b.curp_beneficiario as curpBeneficiario ");
		strQuery.append("FROM tutor t ");
		strQuery.append("INNER JOIN solicitud s ");
		strQuery.append("  on t.id_usuario_llave_cdmx = s.id_usuario_llave_cdmx ");
		strQuery.append("INNER JOIN crc_beneficiario_solicitud cbs ");
		strQuery.append("  on s.id_solicitud = cbs.id_solicitud ");
		strQuery.append("INNER JOIN beneficiario b ");
		strQuery.append("  on cbs.id_beneficiario = b.id_beneficiario ");
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
				lstBeneficiariosSolTutorDTO.add(mapearBeneficiarioSolicitudTutorDTO(rs));
			}
		} catch (SQLException e1) {
			LOGGER.error("Ocurrió un error al consultar dispersiones con el query [" + strQuery.toString() + "]:", e1);
		} finally {
			PostgresDatasource.getInstance().close(rs, stm, conn);
		}
		return lstBeneficiariosSolTutorDTO;
	}

	private BeneficiarioSolicitudTutorDTO mapearBeneficiarioSolicitudTutorDTO(ResultSet rs) throws SQLException {
		BeneficiarioSolicitudTutorDTO bst = new BeneficiarioSolicitudTutorDTO();
		bst.setIdUsuario(rs.getLong("idUsuario"));
		bst.setCurpTutor(rs.getString("curpTutor"));
		bst.setIdEstatusTutor(rs.getLong("idEstatusTutor"));
		bst.setNumeroCuenta(rs.getString("numeroCuenta"));
		bst.setIdNivelEducativo(rs.getLong("idNivelEducativo"));
		bst.setIdGradoEscolar(rs.getLong("idGradoEscolar"));
		bst.setIdBeneficiario(rs.getLong("idBeneficiario"));
		bst.setCurpBeneficiario(rs.getString("curpBeneficiario"));
		return bst;
	}

}
