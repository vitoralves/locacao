package br.com.locacao.servicos;

import br.com.locacao.entidades.Produtos;
import br.com.locacao.repositorio.RepositorioProduto;
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
public class ServicoProduto extends ServicoBasico{
    
    private static final long serialVersionUID = 1L;
    
    @PersistenceContext
    private EntityManager em;    
    private RepositorioProduto prodRepositorio;
    
    @PostConstruct
    @PostActivate
    private void postConstruct(){
        prodRepositorio = new RepositorioProduto(em);
    }
    
    public ServicoProduto(){
        
    }
    
    public Produtos getProduto(int idProduto){
        return prodRepositorio.getProduto(idProduto);
    }
    
    public Produtos setProduto(Produtos usuario){
        return prodRepositorio.setProduto(usuario);
    }
    
    public void removeProduto(Produtos usuario){
        prodRepositorio.removeProduto(usuario);
    }
    
    public Produtos addProduto(Produtos usuario){
        return prodRepositorio.addProduto(usuario);
    }
    
    public List<Produtos> getProdutos(){
        return prodRepositorio.getProduto();
    }
    
    public List<Produtos> get10Produtos(){
        return  prodRepositorio.get10Produtos();
    }
    
    public List<Produtos> getProdutosByName(String nome){
        return prodRepositorio.getProdutosByName(nome);
    }
    
    public int getQuantidadeEstoque(){
        return prodRepositorio.getQuantidadeEstoque();
    }
}
