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
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.PadronExternoDTO;

public class PadronExternoDAO extends IBaseDAO<PadronExternoDTO, Integer> {

	private static final Logger LOGGER = LogManager.getLogger(PadronExternoDAO.class);

	@Override
	public PadronExternoDTO buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PadronExternoDTO> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void actualizar(PadronExternoDTO e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void guardar(PadronExternoDTO e) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public List<PadronExternoDTO> buscarPorCriterios(PadronExternoDTO e) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<PadronExternoDTO> buscarPorCurp(String curp) {
		StringBuilder strQuery = new StringBuilder();
		
		strQuery.append("SELECT ");
		strQuery.append("pe.curp as curp, ");
		strQuery.append("pe.nombres as nombres, ");
		strQuery.append("pe.primer_apellido as primerApellido, ");
		strQuery.append("pe.segundo_apellido as segundoApellido, ");
		strQuery.append("pe.cct as cct, ");
		strQuery.append("pe.nombre_cct as nombreCct, ");
		strQuery.append("pe.calle as calle, ");
		strQuery.append("pe.numero_exterior as numeroExterior, ");
		strQuery.append("pe.colonia as colonia, ");
		strQuery.append("pe.id_municipio as idMunicipio, ");
		strQuery.append("pe.municipio as municipio, ");
		strQuery.append("pe.codigo_postal as codigoPostal, ");
		strQuery.append("pe.turno as turno, ");
		strQuery.append("pe.nivel_educativo as nivelEducativo, ");
		strQuery.append("pe.grado_escolar as gradoEscolar, ");
		strQuery.append("pe.estatus as estatus, ");
		strQuery.append("pe.tipo_escuela as tipoEscuela ");
		strQuery.append("FROM mibecaparaempezar.padron_externo pe ");
		strQuery.append("WHERE pe.curp = '").append(curp).append("'");
		
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;

		List<PadronExternoDTO> lstPadronExternoDTO = new ArrayList<>();
		try {
			conn = PostgresDatasource.getInstance().getConnection();
			stm = conn.createStatement();
			rs = stm.executeQuery(strQuery.toString());
			while (rs.next()) {
				lstPadronExternoDTO.add(mapearPadronExternoDTO(rs));
			}
		} catch (SQLException e1) {
			LOGGER.error("Ocurrió un error al consultar dispersiones con el query [" + strQuery.toString() + "]:", e1);
		} finally {
			PostgresDatasource.getInstance().close(rs, stm, conn);
		}
		return lstPadronExternoDTO;
	}

	private PadronExternoDTO mapearPadronExternoDTO(ResultSet rs) throws SQLException {
		PadronExternoDTO bst = new PadronExternoDTO();
		bst.setCurp(rs.getString("curp"));
		bst.setCalle(rs.getString("nombres"));
		bst.setCodigoPostal(rs.getString("primerApellido"));
		bst.setColonia(rs.getString("segundoApellido"));
		bst.setEstatus(rs.getString("cct"));
		bst.setGradoEscolar(rs.getString("nombreCct"));
		bst.setIdMunicipio(rs.getInt("calle"));
		bst.setMunicipio(rs.getString("numeroExterior"));
		bst.setNivelEducativo(rs.getString("colonia"));
		bst.setNombreCct(rs.getString("idMunicipio"));
		bst.setNombres(rs.getString("municipio"));
		bst.setNumeroExterior(rs.getString("codigoPostal"));
		bst.setPrimerApellido(rs.getString("turno"));
		bst.setSegundoApellido(rs.getString("nivelEducativo"));
		bst.setGradoEscolar(rs.getString("gradoEscolar"));
		bst.setEstatus(rs.getString("estatus"));
		bst.setTipoEscuela(rs.getString("tipoEscuela"));
		return bst;
	}

}
