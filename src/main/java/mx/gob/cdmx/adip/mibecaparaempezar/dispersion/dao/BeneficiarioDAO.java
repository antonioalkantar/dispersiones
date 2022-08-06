package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioDTO;

public class BeneficiarioDAO extends IBaseDAO<BeneficiarioDTO, Integer>{

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
		// TODO Auto-generated method stub
		return null;
	}
}
