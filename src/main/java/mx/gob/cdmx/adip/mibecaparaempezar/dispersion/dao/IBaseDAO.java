package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao;

import java.util.List;

public abstract class IBaseDAO<E, ID> {
	
	public abstract E buscarPorId(ID id);
	
	public abstract List<E> buscarTodos();
	
	public abstract List<E> buscarPorCriterios(E e);
	
	public abstract void actualizar(E e);
	
	public abstract void guardar(E e);
}