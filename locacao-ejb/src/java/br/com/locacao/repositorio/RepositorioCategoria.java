package br.com.locacao.repositorio;

import br.com.locacao.entidades.Categorias;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author vitor
 */
public class RepositorioCategoria extends RepositorioBasico{
    
    public static final long serialversionUID = 1L;
    
    public RepositorioCategoria(EntityManager entityManager) {
        super(entityManager);
    }
    
    public Categorias addCategoria(Categorias categoria){
        return setEntity(Categorias.class, categoria);
    }
    
    public Categorias setCategoria(Categorias categoria){
        return setEntity(Categorias.class, categoria);
    }
    
    public void removeCategoria(Categorias categoria){
        removeEntity(categoria);
    }
    
    public Categorias getCategoria(int idCategoria){
        return getEntity(Categorias.class, idCategoria);
    }
    
    public List<Categorias> getCategoria(){
        return getPureList(Categorias.class, "select c from Categorias c");
    }
    
    public List<Categorias> getCategoriaByNome(String nome){
        return getPureList(Categorias.class, "select c from Categoiastring c where c.nome like ?1", nome+"%");
    }
}
