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
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatCicloEscolarDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatMontoApoyoDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatNivelEducativoDTO;

public class CatMontoApoyoDAO extends IBaseDAO<CatMontoApoyoDTO, Integer> {

	private static final Logger LOGGER = LogManager.getLogger(CatMontoApoyoDAO.class);

	@Override
	public CatMontoApoyoDTO buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CatMontoApoyoDTO> buscarTodos() {
		StringBuilder strQuery = new StringBuilder();

		strQuery.append("SELECT ");
		strQuery.append("  cma.id_monto_apoyo as idMontoApoyo, ");
		strQuery.append("  cma.id_nivel_educativo as idNivelEducativo, ");
		strQuery.append("  cma.id_ciclo_escolar as idCicloEscolar, ");
		strQuery.append("  cma.monto as monto, ");
		strQuery.append("  cma.estatus as estatus, ");
		strQuery.append("  cma.fecha_creacion as fechaCreacion ");
		strQuery.append("FROM cat_monto_apoyo cma ");

		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;

		List<CatMontoApoyoDTO> lstCatMontoApoyo = new ArrayList<>();
		try {
			conn = PostgresDatasource.getInstance().getConnection();
			stm = conn.createStatement();
			rs = stm.executeQuery(strQuery.toString());
			while (rs.next()) {
				lstCatMontoApoyo.add(mapearCatMontoApoyoDTO(rs));
			}
		} catch (SQLException e1) {
			LOGGER.error("Ocurrió un error al consultar dispersiones con el query [" + strQuery.toString() + "]:", e1);
		} finally {
			PostgresDatasource.getInstance().close(rs, stm, conn);
		}
		return lstCatMontoApoyo;
	}

	public void actualizar(CatMontoApoyoDTO e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void guardar(CatMontoApoyoDTO e) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<CatMontoApoyoDTO> buscarPorCriterios(CatMontoApoyoDTO e) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private CatMontoApoyoDTO mapearCatMontoApoyoDTO(ResultSet rs) throws SQLException {
		CatMontoApoyoDTO catMontoApoyo = new CatMontoApoyoDTO();
		catMontoApoyo.setIdMontoApoyo(rs.getInt("idMontoApoyo"));
		catMontoApoyo.setCatNivelEducativoDTO(new CatNivelEducativoDTO(rs.getInt("idNivelEducativo")));
		catMontoApoyo.setCatCicloEscolarDTO(new CatCicloEscolarDTO(rs.getLong("idCicloEscolar")));
		catMontoApoyo.setMonto(rs.getDouble("monto"));
		catMontoApoyo.setEstatus(rs.getBoolean("estatus"));
		catMontoApoyo.setFechaCreacion(rs.getDate("fechaCreacion"));
		return catMontoApoyo;
	}

	
}
