package br.com.locacao.servicos;

import br.com.locacao.entidades.Clientes;
import br.com.locacao.repositorio.RepositorioCliente;
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
public class ServicoCliente extends ServicoBasico{
    
    private static final long serialVersionUID = 1L;
    
    @PersistenceContext
    private EntityManager em;    
    private RepositorioCliente cliRepositorio;
    
    @PostConstruct
    @PostActivate
    private void postConstruct(){
        cliRepositorio = new RepositorioCliente(em);
    }
    
    public ServicoCliente(){
        
    }
    
    public Clientes getCliente(int idCliente){
        return cliRepositorio.getCliente(idCliente);
    }
    
    public Clientes setCliente(Clientes usuario){
        return cliRepositorio.setCliente(usuario);
    }
    
    public void removeCliente(Clientes usuario){
        cliRepositorio.removeCliente(usuario);
    }
    
    public Clientes addCliente(Clientes usuario){
        return cliRepositorio.addCliente(usuario);
    }
    
    public List<Clientes> getClientes(){
        return cliRepositorio.getCliente();
    }
    
    public List<Clientes> getClientesByParam(String nome){
        return cliRepositorio.getClienteByParam(nome);
    }
    
    public String verificaExistente(String cpf, String cnpj, String rg){
        return cliRepositorio.verificaExistente(cpf, cnpj, rg);
    }
    
    public Clientes getClienteRg(String rg){
        return cliRepositorio.getClienteRg(rg);
    }
}
