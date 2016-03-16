package br.com.locacao.repositorio;

import br.com.locacao.entidades.Clientes;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author vitor
 */
public class RepositorioCliente extends RepositorioBasico{
    
    public static final long serialversionUID = 1L;
    
    public RepositorioCliente(EntityManager entityManager) {
        super(entityManager);
    }
    
    public Clientes addCliente(Clientes cliente){
        return setEntity(Clientes.class, cliente);
    }
    
    public Clientes setCliente(Clientes cliente){
        return setEntity(Clientes.class, cliente);
    }
    
    public void removeCliente(Clientes cliente){
        removeEntity(cliente);
    }
    
    public Clientes getCliente(int idCliente){
        return getEntity(Clientes.class, idCliente);
    }
    
    public List<Clientes> getCliente(){
        return getPureList(Clientes.class, "select c from Clientes c order by c.nome ");
    }
    
    public List<Clientes> getClienteByParam(String nome){
        return getPureList(Clientes.class, "select c from Clientes c "
                + "where upper(c.nome) like upper(?1) ", nome+"%");
    }
   
}
