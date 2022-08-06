package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatCicloEscolarDTO;

public class CatCicloEscolarDAO extends IBaseDAO<CatCicloEscolarDTO, Integer>{

	private static final Logger LOGGER = LogManager.getLogger(CatCicloEscolarDAO.class);
	
	@Override
	public CatCicloEscolarDTO buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CatCicloEscolarDTO> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void actualizar(CatCicloEscolarDTO e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardar(CatCicloEscolarDTO e) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<CatCicloEscolarDTO> buscarPorCriterios(CatCicloEscolarDTO e) {
		// TODO Auto-generated method stub
		return null;
	}
}
