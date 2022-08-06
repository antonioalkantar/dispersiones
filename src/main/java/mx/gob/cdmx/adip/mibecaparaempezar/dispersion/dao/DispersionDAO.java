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
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.DispersionDTO;

public class DispersionDAO extends IBaseDAO<DispersionDTO, Integer>{

	private static final Logger LOGGER = LogManager.getLogger(DispersionDAO.class);
	
	@Override
	public DispersionDTO buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DispersionDTO> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DispersionDTO> buscarPorCriterios(DispersionDTO e) {
		StringBuilder strQuery = new StringBuilder();
		strQuery.append(" SELECT ... ");
		strQuery.append(" FROM  ");
		strQuery.append(" WHERE 1=1");
		//El orden es importante, ya que por ejemplo, se pudo pedir en un primer archivo que se cargara la CURP X y en un segundo archivo pedir que se dé de baja, si se procesan en desorden no va a dar el resultado esperado
		strQuery.append(" ORDER BY created_at asc "); 
		
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		
		List<DispersionDTO> lstDispersionesDTO = new ArrayList<>();
		try{
			conn = PostgresDatasource.getInstance().getConnection();
			stm = conn.createStatement();
			rs = stm.executeQuery(strQuery.toString());
            while(rs.next()){                
            	//lstDispersionesDTO.add(new DispersionDTO(rs.getLong("id") ));                
            }
		} catch (SQLException e1) {
			LOGGER.error("Ocurrió un error al consultar dispersiones con el query ["+strQuery.toString()+"]:", e1);
		} finally {
			PostgresDatasource.getInstance().close(rs, stm, conn);
		}
		return lstDispersionesDTO;
	}

	@Override
	public void actualizar(DispersionDTO e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardar(DispersionDTO e) {
		// TODO Auto-generated method stub
	}

	public void actualizarEstatus(long idDispersion, int idEstatusDispersion) {
		StringBuilder strQuery = new StringBuilder();
		strQuery.append(" UPDATE ....");
		strQuery.append(" WHERE  id = ").append(idDispersion);
		
		Connection conn = null;
		Statement stm = null;
		try{
			conn = PostgresDatasource.getInstance().getConnection();
			stm = conn.createStatement();
			int registrosAfectados = stm.executeUpdate(strQuery.toString());
			if(registrosAfectados < 1) {
				throw new IllegalArgumentException("El idDispersion "+idDispersion+" no se actualizó su estatus ["+strQuery.toString()+"]");
			}
		} catch (SQLException e1) {
			LOGGER.error("Ocurrió un error al actualizar un archivo de padrón con el DML ["+strQuery.toString()+"]:", e1);
		} finally {
			PostgresDatasource.getInstance().close(null, stm, conn);
		}
	}

	public void actualizarContadores(DispersionDTO dispersionDTO) {
		StringBuilder strQuery = new StringBuilder();
		strQuery.append(" UPDATE ..... ");
		
		Connection conn = null;
		Statement stm = null;
		try{
			conn = PostgresDatasource.getInstance().getConnection();
			stm = conn.createStatement();
			int registrosAfectados = stm.executeUpdate(strQuery.toString());
			if(registrosAfectados < 1) {
				//throw new IllegalArgumentException("Para el idDispersion "+dispersionDTO.getIdDispersion()+" no se actualizaron sus contadores ["+strQuery.toString()+"]");
			}
		} catch (SQLException e1) {
			LOGGER.error("Ocurrió un error al actualizar los contadores de una dispersión con el DML ["+strQuery.toString()+"]:", e1);
		} finally {
			PostgresDatasource.getInstance().close(null, stm, conn);
		}
	}
}
