package br.com.locacao.controle;

import br.com.locacao.entidades.Categorias;
import br.com.locacao.servicos.ServicoCategoria;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author vitor
 */
@Named
@ViewScoped
public class CategoriaControl extends BasicControl implements java.io.Serializable {

    @EJB
    private ServicoCategoria catService;
    private String catname;
    private List<Categorias> listCategorias;
    private boolean showAdd = false;
    private boolean showEdit = false;
    private Categorias catSelected;
    private String localizarNome;

    @PostConstruct
    public void init() {
        esconde();
        pesquisaTodos();
    }
    
    public void onRowSelect(SelectEvent event) {
        showAdd = false;
        showEdit = true;
    }
    
    public List<Categorias> pesquisaTodos() {
        if (listCategorias == null) {
            listCategorias = catService.getCategorias();
        } else {
            listCategorias = null;
            listCategorias = catService.getCategorias();
        }
        return listCategorias;
    }

    public void pesquisaCategoria() {
        if (localizarNome == null) {
            pesquisaTodos();
        } else {
            listCategorias = null;
            listCategorias = catService.getCategoriaParametro(localizarNome);
        }
    }

    public void esconde() {
        showAdd = false;
        showEdit = false;
    }

    public void addNovo() {
        catSelected = new Categorias();
        showAdd = true;
        showEdit = false;
    }

    public void editar() {
        showAdd = false;
        showEdit = true;
    }

    public void doStartAlterar() {
        showAdd = false;
        showEdit = true;
    }

    public void doFinishExcluir() {
        catService.removeCategoria(catSelected);
        showEdit = false;
        showAdd = false;
        pesquisaTodos();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Excluido com sucesso!", null));
    }

    public void salvarEditado() {
        catService.setCategoria(catSelected);
        esconde();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Alterado com sucesso!", null));
    }

    public void salvarNovo() {
        try {
            catService.addCategoria(catSelected);
            showAdd = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Inserido com sucesso!", null));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro ao salvar! "+e, null));
        }
    }

    public String getUsername() {
        return catname;
    }

    public void setUsername(String catname) {
        this.catname = catname;
    }

    public List<Categorias> getListCategorias() {
        return listCategorias;
    }

    public void setListCategorias(List<Categorias> listCategorias) {
        this.listCategorias = listCategorias;
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

    public Categorias getCatSelected() {
        return catSelected;
    }

    public void setCatSelected(Categorias catSelected) {
        this.catSelected = catSelected;
    }

    public String getLocalizarNome() {
        return localizarNome;
    }

    public void setLocalizarNome(String localizarNome) {
        this.localizarNome = localizarNome;
    }

}
