package br.com.locacao.controle;

import br.com.locacao.entidades.Produtos;
import br.com.locacao.servicos.ServicoProduto;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author vitor
 */
@Named
@ViewScoped
public class EstoqueControl extends BasicControl implements java.io.Serializable {

    @EJB
    private ServicoProduto prodService;
    private String nomeProd;
    private List<Produtos> listProdutos;
    private boolean showAdd = false;
    private boolean showEdit = false;
    private Produtos prodSelected;
    private int codProd;
    
    public List<Produtos> pesquisaTodos(){
        if(listProdutos == null){
            listProdutos = prodService.getProdutos();
        }else{
            listProdutos = null;
            listProdutos = prodService.getProdutos();
        }
        return listProdutos;
    }
    
    public List<Produtos> pesquisaNome(){
        if(listProdutos == null){
            listProdutos = prodService.getProdutosByName(nomeProd);
        }else{
            listProdutos = null;
            listProdutos = prodService.getProdutosByName(nomeProd);
        }
        return listProdutos;
    }
    
    public void esconde(){
        showAdd = false;
        showEdit = false;
    }
    
    public void addNovo(){
        prodSelected = new Produtos();
        showAdd = true;
        showEdit = false;
    }
    
    public void editar(){
        showAdd = false;
        showEdit = true;
    }
    
    public ServicoProduto getProdService() {
        return prodService;
    }

    public void setProdService(ServicoProduto prodService) {
        this.prodService = prodService;
    }

    public String getNomeProd() {
        return nomeProd;
    }

    public void setNomeProd(String nomeProd) {
        this.nomeProd = nomeProd;
    }

    public List<Produtos> getListProdutos() {
        return listProdutos;
    }

    public void setListProdutos(List<Produtos> listProdutos) {
        this.listProdutos = listProdutos;
    }

    public boolean isShowAdd() {
        return showAdd;
    }

    public void setShowAdd(boolean showAdd) {
        this.showAdd = showAdd;
    }

    public boolean isShowEdit() {
        return showEdit;
    }

    public void setShowEdit(boolean showEdit) {
        this.showEdit = showEdit;
    }

    public Produtos getProdSelected() {
        return prodSelected;
    }

    public void setProdSelected(Produtos prodSelected) {
        this.prodSelected = prodSelected;
    }

    public int getCodProd() {
        return codProd;
    }

    public void setCodProd(int codProd) {
        this.codProd = codProd;
    }

   
}
