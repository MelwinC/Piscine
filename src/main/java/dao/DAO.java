package dao;

import java.util.HashMap;

/**
 * Patron de conception DAO
 *
 * @param <T> avec type g�n�rique T (comme pour ArrayList<T>)
 */
public abstract class DAO<T> {
	
	protected final HashMap<Integer, T> donnees = new HashMap<Integer, T>();
		
	/**
	 * M�thode de cr�ation d'un objet de type "T",
	 * peut �tre amen� � injecter l'id cr�� dans le programme
	 * @param obj
	 * @return boolean 
	 */
	public abstract boolean create(T obj);

	/**
	 * M�thode pour effacer selon l'id de l'objet
	 * @param obj
	 * @return boolean 
	 */
	public abstract boolean delete(T obj);

	/**
	 * M�thode de mise � jour selon l'id de l'objet
	 * @param obj
	 * @return boolean
	 */
	public abstract boolean update(T obj);

	/**
	 * M�thode de recherche des informations qui retourne un objet T
	 * @param id
	 * @return T
	 */
	public abstract T read(int id);

	
	
}