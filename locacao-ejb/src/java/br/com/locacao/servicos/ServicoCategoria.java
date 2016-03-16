package br.com.locacao.servicos;

import br.com.locacao.entidades.Categorias;
import br.com.locacao.repositorio.RepositorioCategoria;
import br.com.locacao.repositorio.RepositorioCategoria;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author vitor
 */

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ServicoCategoria extends ServicoBasico{
    
    private static final long serialVersionUID = 1L;
    
    @PersistenceContext
    private EntityManager em;    
    private RepositorioCategoria catRepositorio;
    
    @PostConstruct
    @PostActivate
    private void postConstruct(){
        catRepositorio = new RepositorioCategoria(em);
    }
    
    public ServicoCategoria(){
        
    }
    
    public Categorias getCategoria(int idCategoria){
        return catRepositorio.getCategoria(idCategoria);
    }
    
    public Categorias setCategoria(Categorias usuario){
        return catRepositorio.setCategoria(usuario);
    }
    
    public void removeCategoria(Categorias usuario){
        catRepositorio.removeCategoria(usuario);
    }
    
    public Categorias addCategoria(Categorias usuario){
        return catRepositorio.addCategoria(usuario);
    }
    
    public List<Categorias> getCategorias(){
        return catRepositorio.getCategoria();
    }
}
