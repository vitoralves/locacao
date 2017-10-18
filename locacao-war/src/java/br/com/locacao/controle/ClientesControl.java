package br.com.locacao.controle;

import br.com.locacao.entidades.Clientes;
import br.com.locacao.servicos.ServicoCliente;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author vitor
 */
@Named
@ViewScoped
public class ClientesControl extends BasicControl implements java.io.Serializable {

    @EJB
    private ServicoCliente cliService;
    private String localizarNome;
    private int localizarCpf;
    private List<Clientes> listClientes;
    private boolean showAdd = false;
    private boolean showEdit = false;
    private Clientes cliSelected;
    private boolean mostrarTipo = false;
    private ArrayList<SelectItem> listTipo;
    private String valueTipo = "";
    private String maskTipo = "999.999.99";
    private String tipoSelecionado = "";
    private boolean cpf = true;
    private boolean cpfReq = false;
    private boolean cnpj = true;
    private boolean cnpjReq = false;
    
    @Inject
    private EventoControl evtControl;
    
    @PostConstruct
    public void init() {
        esconde();
        pesquisaTodos();
    }

    public void mudaVisible() {
        if (cliSelected.getTipo() != null && cliSelected.getTipo().equals("F")) {
            cpf = false;
            cpfReq = true;
            cnpj = true;
            cnpjReq = false;
            getCliSelected().setCnpj(null);
        } else if (cliSelected.getTipo() != null && cliSelected.getTipo().equals("J")) {
            cpf = true;
            cpfReq = false;
            cnpj = false;
            cnpjReq = true;
            getCliSelected().setCpf(null);
        }
    }

    public void onRowSelect(SelectEvent event) {
        showAdd = false;
        showEdit = true;
    }

    public void pesquisaCliente() {
        if (localizarNome == null) {
            pesquisaTodos();
            localizarNome = null;
        } else {
            listClientes = null;
            listClientes = cliService.getClientesByParam(localizarNome);
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

    public int totalClientes() {
        int i = 0;
        int totalClientes = 0;
        pesquisaTodos();
        for (i = 0; i < listClientes.size(); i++) {
            totalClientes += 1;
        }
        return totalClientes;
    }

    public void addNovo() {
        cliSelected = new Clientes();
        showAdd = true;
        showEdit = false;
    }

    public void salvarSetarEvento() {
        
        try {
            cliService.addCliente(cliSelected);
            evtControl.setCliSelected(cliSelected);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Cliente salvo com sucesso!", null));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro ao salvar cliente! -> "+ e, null));
        }
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

    public void doStartAlterar() {
        showAdd = false;
        showEdit = true;
    }

    public void doFinishAlterar() {
        cliService.setCliente(cliSelected);
        showEdit = false;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Alterado com sucesso!", null));

    }

    public void doFinishExcluir() {
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

    public boolean isMostrarTipo() {
        return mostrarTipo;
    }

    public void setMostrarTipo(boolean mostrarTipo) {
        this.mostrarTipo = mostrarTipo;
    }

    public String getValueTipo() {
        return valueTipo;
    }

    public void setValueTipo(String valueTipo) {
        this.valueTipo = valueTipo;
    }

    public String getMaskTipo() {
        return maskTipo;
    }

    public void setMaskTipo(String maskTipo) {
        this.maskTipo = maskTipo;
    }

    public String getTipoSelecionado() {
        return tipoSelecionado;
    }

    public void setTipoSelecionado(String tipoSelecionado) {
        this.tipoSelecionado = tipoSelecionado;
    }

    public ArrayList<SelectItem> getListTipo() {
        if (listTipo == null || listTipo.size() == 0) {
            listTipo = new ArrayList<SelectItem>();
            listTipo.add(new SelectItem("F", "Física"));
            listTipo.add(new SelectItem("J", "Jurídica"));
        }
        return listTipo;
    }

    public boolean isCpf() {
        return cpf;
    }

    public void setCpf(boolean cpf) {
        this.cpf = cpf;
    }

    public boolean isCnpj() {
        return cnpj;
    }

    public void setCnpj(boolean cnpj) {
        this.cnpj = cnpj;
    }

    public void setListTipo(ArrayList<SelectItem> listTipo) {
        this.listTipo = listTipo;
    }

    public boolean isCpfReq() {
        return cpfReq;
    }

    public void setCpfReq(boolean cpfReq) {
        this.cpfReq = cpfReq;
    }

    public boolean isCnpjReq() {
        return cnpjReq;
    }

    public void setCnpjReq(boolean cnpjReq) {
        this.cnpjReq = cnpjReq;
    }

}
