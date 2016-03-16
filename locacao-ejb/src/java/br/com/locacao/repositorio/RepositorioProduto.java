package br.com.locacao.repositorio;

import br.com.locacao.entidades.Produtos;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author vitor
 */
public class RepositorioProduto extends RepositorioBasico{
    
    public static final long serialversionUID = 1L;
    
    public RepositorioProduto(EntityManager entityManager) {
        super(entityManager);
    }
    
    public Produtos addProduto(Produtos produto){
        return setEntity(Produtos.class, produto);
    }
    
    public Produtos setProduto(Produtos produto){
        return setEntity(Produtos.class, produto);
    }
    
    public void removeProduto(Produtos produto){
        removeEntity(produto);
    }
    
    public Produtos getProduto(int idProduto){
        return getEntity(Produtos.class, idProduto);
    }
    
    public List<Produtos> getProduto(){
        return getPureList(Produtos.class, "select p from Produtos p");
    }
    
    public List<Produtos> getProdutosByName(String nome){
        return getPureList(Produtos.class, "select p from Produtos p "
                + " where p.nome like %?1%", nome);
    }

}
