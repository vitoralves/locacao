package br.com.locacao.controle;

import br.com.locacao.entidades.Eventos;
import br.com.locacao.servicos.ServicoEvento;
import br.com.locacao.servicos.ServicoEvento;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.primefaces.model.UploadedFile;
import sun.font.FontUtilities;

/**
 *
 * @author vitor
 */
@Named
@SessionScoped
public class EventoControl extends BasicControl implements java.io.Serializable {

    @EJB
    private ServicoEvento evtService;
    private Eventos loggedUser;
    @NotNull(message = "Digite um usuário")
    private String username;
    @NotNull(message = "Digite uma senha")
    private String senha;
    private List<Eventos> listEventos;
    private boolean showAdd = false;
    private boolean showEdit = false;
    private Eventos evtSelected;
    private String localizarNome;

    @PostConstruct
    public void init() {
        esconde();
        pesquisaTodos();
    }

    public List<Eventos> pesquisaTodos() {
        if (listEventos == null) {
            listEventos = evtService.getEventos();
        } else {
            listEventos = null;
            listEventos = evtService.getEventos();
        }
        return listEventos;
    }
    

    public void pesquisaEvento() {
        if (localizarNome.isEmpty() || localizarNome == null) {
            pesquisaTodos();
        } else {
            listEventos = evtService.getEventos();
        }
    }

    public void esconde() {
        showAdd = false;
        showEdit = false;
    }

    public void addNovo() {
        evtSelected = new Eventos();
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
        evtService.removeEvento(evtSelected);
        showEdit = false;
        showAdd = false;
        pesquisaTodos();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Excluido com sucesso!", null));
    }

    public void salvarEditado() {
        evtService.setEvento(evtSelected);
        esconde();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Alterado com sucesso!", null));
    }

    public void salvarNovo() {
        try {
            evtService.addEvento(evtSelected);
            showAdd = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Inserido com sucesso!", null));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro ao salvar!", null));
        }
    }

    public Eventos getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(Eventos loggedUser) {
        this.loggedUser = loggedUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<Eventos> getListEventos() {
        return listEventos;
    }

    public void setListEventos(List<Eventos> listEventos) {
        this.listEventos = listEventos;
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

    public Eventos getEvtSelected() {
        return evtSelected;
    }

    public void setEvtSelected(Eventos evtSelected) {
        this.evtSelected = evtSelected;
    }

    public String getLocalizarNome() {
        return localizarNome;
    }

    public void setLocalizarNome(String localizarNome) {
        this.localizarNome = localizarNome;
    }

}
