package br.com.locacao.controle;

import br.com.locacao.entidades.Clientes;
import br.com.locacao.servicos.ServicoCliente;
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
public class ClientesControl extends BasicControl implements java.io.Serializable {

    @EJB
    private ServicoCliente cliService;
    private String localizarNome;
    private int localizarCpf;
    private List<Clientes> listClientes;
    private boolean showAdd = false;
    private boolean showEdit = false;
    private Clientes cliSelected;
    
    @PostConstruct
    public void init() {
        esconde();
        pesquisaTodos();
    }

    public void pesquisaCliente() {
        if (localizarNome == null) {
            pesquisaTodos();
            localizarNome= null;
        } else {
            listClientes = null;
            listClientes = cliService.getClientesByParam(localizarNome);
            localizarNome= null;
        }
    }

    public List<Clientes> pesquisaTodos() {
        if (listClientes == null) {
            listClientes = cliService.getClientes();
        } else {
            listClientes = null;
            listClientes = cliService.getClientes();
        }
        return listClientes;
    }

    public void addNovo() {
        cliSelected = new Clientes();
        showAdd = true;
        showEdit = false;
    }
    
    public void salvarNovo() {
        try {
            cliService.addCliente(cliSelected);
            showAdd = false;
            setListClientes(null);
            pesquisaTodos();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Salvo com sucesso!", null));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro ao salvar!", null));
        }
    }
    
    public void doStartAlterar(){
        showAdd = false;
        showEdit = true;
    }
    
    public void doFinishAlterar() {
        cliService.setCliente(cliSelected);
        showEdit = false;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Alterado com sucesso!", null));
        
    }
    
    public void doFinishExcluir(){
        cliService.removeCliente(cliSelected);
        showEdit = false;
        showAdd = false;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Excluido com sucesso!", null));
    }
    
    public void esconde() {
        pesquisaTodos();
        showAdd = false;
        showEdit = false;
    }

    public ServicoCliente getCliService() {
        return cliService;
    }

    public void setCliService(ServicoCliente cliService) {
        this.cliService = cliService;
    }

    public String getLocalizarNome() {
        return localizarNome;
    }

    public void setLocalizarNome(String localizarNome) {
        this.localizarNome = localizarNome;
    }

    public int getLocalizarCpf() {
        return localizarCpf;
    }

    public void setLocalizarCpf(int localizarCpf) {
        this.localizarCpf = localizarCpf;
    }

    public List<Clientes> getListClientes() {
        return listClientes;
    }

    public void setListClientes(List<Clientes> listClientes) {
        this.listClientes = listClientes;
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

    public Clientes getCliSelected() {
        return cliSelected;
    }

    public void setCliSelected(Clientes cliSelected) {
        this.cliSelected = cliSelected;
    }

}
