package br.com.locacao.repositorio;

import br.com.locacao.entidades.Clientes;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author vitor
 */
public class RepositorioCliente extends RepositorioBasico {

    public static final long serialversionUID = 1L;

    public RepositorioCliente(EntityManager entityManager) {
        super(entityManager);
    }

    public Clientes addCliente(Clientes cliente) {
        return setEntity(Clientes.class, cliente);
    }

    public Clientes setCliente(Clientes cliente) {
        return setEntity(Clientes.class, cliente);
    }

    public void removeCliente(Clientes cliente) {
        removeEntity(cliente);
    }

    public Clientes getCliente(int idCliente) {
        return getEntity(Clientes.class, idCliente);
    }

    public List<Clientes> getCliente() {
        return getPureList(Clientes.class, "select c from Clientes c order by c.nome ");
    }

    public List<Clientes> getClienteByParam(String nome) {
        return getPureList(Clientes.class, "select c from Clientes c "
                + "where upper(c.nome) like upper(?1) ", nome + "%");
    }

    public String verificaExistente(String cpf, String cnpj, String rg) {
        Query q = entityManager.createQuery("select c from Clientes c where c.cpf like ?1 or c.cnpj like ?2 or c.rg like ?3");
        q.setParameter(1, cpf);
        q.setParameter(2, cnpj);
        q.setParameter(3, rg);

        try {
            Clientes c = (Clientes) q.getSingleResult();
            if (c.getIdCliente() > 0) {
                return "CPF ou CNPJ ou RG já foram cadastrados para o cliente " + c.getNome() + " código:" + c.getIdCliente();
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }
    
    public Clientes getClienteRg(String rg){
        Query q = entityManager.createQuery("select c from Clientes c where c.rg = ?1");
        q.setParameter(1, rg);
        
        return (Clientes) q.getSingleResult();
    }
}
