package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatNivelEducativoDTO;

public class CatNivelEducativoDAO extends IBaseDAO<CatNivelEducativoDTO, Integer>{

	private static final Logger LOGGER = LogManager.getLogger(CatNivelEducativoDAO.class);
	
	@Override
	public CatNivelEducativoDTO buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CatNivelEducativoDTO> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void actualizar(CatNivelEducativoDTO e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void guardar(CatNivelEducativoDTO e) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<CatNivelEducativoDTO> buscarPorCriterios(CatNivelEducativoDTO e) {
		// TODO Auto-generated method stub
		return null;
	}
}
