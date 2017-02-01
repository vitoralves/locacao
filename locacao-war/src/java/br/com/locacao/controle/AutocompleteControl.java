package br.com.locacao.controle;

import br.com.locacao.entidades.Clientes;
import br.com.locacao.entidades.Produtos;
import br.com.locacao.servicos.ServicoCliente;
import br.com.locacao.servicos.ServicoProduto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Init;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author vitor
 */
@Named
@SessionScoped
public class AutocompleteControl extends BasicControl implements java.io.Serializable {
    
    @EJB
    private ServicoProduto prodService;
    private Produtos produto;
    
    public List<Produtos> completeProdutos(String produto) {
        List<Produtos> allProds = prodService.getProdutos();
        List<Produtos> filteredProds = new ArrayList<Produtos>();
         
        for (int i = 0; i < allProds.size(); i++) {
            Produtos prod = allProds.get(i);
            if(prod.getNome().toLowerCase().startsWith(produto)) {
                filteredProds.add(prod);
            }
        }         
        return filteredProds;
    }    

    public Produtos getProduto() {
        return produto;
    }

    public void setProduto(Produtos produto) {
        this.produto = produto;
    }

    
    
    
}
