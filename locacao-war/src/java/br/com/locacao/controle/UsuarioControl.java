package br.com.locacao.controle;

import br.com.locacao.entidades.Usuarios;
import br.com.locacao.servicos.ServicoUsuario;
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
public class UsuarioControl extends BasicControl implements java.io.Serializable {

    @EJB
    private ServicoUsuario userService;
    private Usuarios loggedUser;
    @NotNull(message = "Digite um usuário")
    private String username;
    @NotNull(message = "Digite uma senha")
    private String senha;
    private List<Usuarios> listUsuarios;
    private boolean showAdd = false;
    private boolean showEdit = false;
    private Usuarios usuSelected;
    private String localizarNome;

    @PostConstruct
    public void init() {
        esconde();
        pesquisaTodos();
    }

    public String doLogin() {
        loggedUser = null;
        loggedUser = userService.getUsuarioByLoginSenha(username, senha);
        if (loggedUser == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Usuário ou Senha incorretos!", null));
            return null;
        } else if (loggedUser.getAdmin() == true) {
            return "/restrito/admin/index.faces?faces-redirect=true";
        } else if (loggedUser.getAdmin() == false) {
            return "/restrito/index_f.faces?faces-redirect=true";
        }
        return null;
    }

    public List<Usuarios> pesquisaTodos() {
        if (listUsuarios == null) {
            listUsuarios = userService.getUsuarios();
        } else {
            listUsuarios = null;
            listUsuarios = userService.getUsuarios();
        }
        return listUsuarios;
    }

    public void pesquisaUsuario() {
        if (localizarNome.isEmpty() || localizarNome == null) {
            pesquisaTodos();
        } else {
            listUsuarios = null;
            listUsuarios = userService.getUsuarioParametro(localizarNome);
        }
    }

    public void esconde() {
        showAdd = false;
        showEdit = false;
    }

    public void addNovo() {
        usuSelected = new Usuarios();
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
        userService.removeUsuario(usuSelected);
        showEdit = false;
        showAdd = false;
        pesquisaTodos();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Excluido com sucesso!", null));
    }

    public void salvarEditado() {
        userService.setUsuario(usuSelected);
        esconde();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Alterado com sucesso!", null));
    }

    public void salvarNovo() {
        try {
            userService.addUsuario(usuSelected);
            showAdd = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Inserido com sucesso!", null));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro ao salvar!", null));
        }
    }

    public Usuarios getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(Usuarios loggedUser) {
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

    public List<Usuarios> getListUsuarios() {
        return listUsuarios;
    }

    public void setListUsuarios(List<Usuarios> listUsuarios) {
        this.listUsuarios = listUsuarios;
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

    public Usuarios getUsuSelected() {
        return usuSelected;
    }

    public void setUsuSelected(Usuarios usuSelected) {
        this.usuSelected = usuSelected;
    }

    public String getLocalizarNome() {
        return localizarNome;
    }

    public void setLocalizarNome(String localizarNome) {
        this.localizarNome = localizarNome;
    }

}
