package br.com.locacao.controle;

import br.com.locacao.entidades.Clientes;
import br.com.locacao.entidades.Eventos;
import br.com.locacao.entidades.ItensOrcamento;
import br.com.locacao.entidades.Orcamento;
import br.com.locacao.servicos.ServicoCliente;
import br.com.locacao.servicos.ServicoEvento;
import br.com.locacao.servicos.ServicoItensOrcamento;
import br.com.locacao.servicos.ServicoOrcamento;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author vitor
 */
@Named
@ViewScoped
public class EventoControl extends BasicControl implements java.io.Serializable {

    @EJB
    private ServicoEvento evtService;
    @EJB
    private ServicoCliente cliService;
    @EJB
    private ServicoItensOrcamento itemOrcService;
    @EJB
    private ServicoOrcamento orcService;

    @Inject
    private OrcamentoControl evtSelectedOrc;
    @Inject
    private ComumControl comumControl;
    @Inject
    private UsuarioControl userControl;

    private List<Eventos> listEventos;
    private List<Eventos> listEventosFinalizados;
    private List<Clientes> listClientes;
    private List<ItensOrcamento> listItensOrcamento;

    private Clientes cliSelected;
    private Eventos evtSelected;
    private Eventos evento;
    private ItensOrcamento itemEvento;
    private ItensOrcamento itemEventoTemporario;
    private Orcamento orcSelected;

    private String localizarNome;
    private String localizarCli;
    private String existente = "";
    private ArrayList<String> selectedCheck;
    private ArrayList<SelectItem> listTipo;

    private boolean showAdd = false;
    private boolean showEdit = false;
    private boolean eventoSalvo;
    private boolean entrega;
    private boolean cpf = true;
    private boolean cpfReq = false;
    private boolean cnpj = true;
    private boolean cnpjReq = false;

    private int quantidade = 0;
    private int dataInt;

    @PostConstruct
    public void init() {
        esconde();
        pesquisaTodos();
        selectedCheck = null;
//        pesquisaTodosProdutos();
        pesquisaTodosClientes();
        String dataString = comumControl.anoAtual();
        dataInt = Integer.parseInt(dataString.trim());
    }

    public void onRowSelect(SelectEvent event) {
        showAdd = false;
        showEdit = true;
    }

    public void inicializaCliente() {
        cliSelected = new Clientes();
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

    public List<Eventos> listaEntregar() {
        listEventos = null;
        listEventos = evtService.getEventosEntregar();
        return listEventos;
    }

    public List<Eventos> listaRecolher() {
        listEventos = null;
        listEventos = evtService.getEventosRecolher();
        return listEventos;
    }

    public void imprimirCheckList(boolean entrega, int id) {
        String e;
        if (entrega) {
            e = "Entrega";
            evtService.imprimirCheckList(e, id, userControl.getLoggedUser().getEmpresa().getIdEmpresa());
        } else {
            e = "Devolução";
            evtService.imprimirCheckList(e, id, userControl.getLoggedUser().getEmpresa().getIdEmpresa());
        }
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

    public List<Clientes> pesquisaTodosClientes() {
        if (listClientes == null) {
            listClientes = cliService.getClientes();
        } else {
            listClientes = null;
            listClientes = cliService.getClientes();
        }
        return listClientes;
    }

    public void removeProdutoLista(int p) {
        itemEvento = null;

    }

    public void finalizarEvento() {
        Date date = new Date();
        if (evtSelected.getDataDevolucao().after(date)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Você não pode encerrar esse evento antes de sua data de devolução!", null));
        } else {
            evtService.finalizaEvento(evtSelected.getIdEvento(), evtSelected.getFinalizado());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Evento Alterado!", null));
        }
    }

    public void cancelarEvento() {
        evtSelected.setFgAtivo(0);
        evtService.setEvento(evtSelected);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Evento cancelado!", null));
    }

    public String iniciarOrcamento() {
        //verificar se já existe orçamento para esse evento
        orcSelected = orcService.getOrcamentoPorEvento(evtSelected.getIdEvento());

        if (orcSelected == null) {
            evtSelectedOrc.novoOrcamento(evtSelected);
            return "/restrito/admin/orcamentos.faces?faces-redirect=true";
        } else {
            evtSelectedOrc.setOrcSelected(orcSelected);
            return "/restrito/admin/orcamentos.faces?faces-redirect=true";
        }

    }

    public String salvarNovoOrcamento() {
        try {
            Calendar dataAtual = DateToCalendar(new Date(), true);
            Calendar dataEvento = Calendar.getInstance();
            dataEvento.setTime(evtSelected.getDataEvento());

            Calendar dataEntrega = Calendar.getInstance();
            dataEntrega.setTime(evtSelected.getDataEntrega());

            Calendar dataDev = Calendar.getInstance();
            dataDev.setTime(evtSelected.getDataDevolucao());

            if (dataEvento.compareTo(dataAtual) < 0 || dataEntrega.compareTo(dataAtual) < 0 || dataDev.compareTo(dataAtual) < 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Data inferior a data atual!", null));
                return "";
            } else {
                evtSelected.setFinalizado(false);
                evtSelected.setConfirmado(false);
                evtSelected.setFgAtivo(1);
                evtService.addEvento(evtSelected);
                showAdd = false;
                evtSelectedOrc.novoOrcamento(evtSelected);
                return "/restrito/admin/orcamentos.faces?faces-redirect=true";

            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro ao salvar!", null));
            return "";
        }
    }

    public void insereEvento() {
        evtService.addEvento(evtSelected);
    }

    public List<Clientes> autoComplete(String query) {
        List<Clientes> allCli = cliService.getClientes();
        List<Clientes> filteredCli = new ArrayList<Clientes>();

        for (int i = 0; i < allCli.size(); i++) {
            Clientes c = allCli.get(i);
            if (c.getNome().toLowerCase().startsWith(query.toLowerCase())) {
                filteredCli.add(c);
            }
        }

        return filteredCli;
    }

    public void pesquisaCliente() {
        if (localizarCli == null) {
            pesquisaTodosClientes();
            localizarCli = null;
        } else {
            listClientes = null;
            listClientes = cliService.getClientesByParam(localizarCli);
            localizarCli = null;
        }
    }

    public void pesquisaEvento() {
        if (localizarNome == null || localizarNome.isEmpty()) {
            pesquisaTodos();
        } else if (localizarNome.matches("[0-9]*")) {
            Eventos e = new Eventos();
            e = evtService.getEvento(Integer.parseInt(localizarNome));
            listEventos.clear();
            if (e != null) {
                listEventos.add(e);
            }
        } else {
            listEventos = evtService.getEventoPorCliente(localizarNome);
        }
    }

    public void esconde() {
        showAdd = false;
        showEdit = false;
    }

    public void addNovo() {
        evtSelected = null;
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

    public void salvarCliente() {
        existente = cliService.verificaExistente(cliSelected.getCpf() == null ? "" : cliSelected.getCpf(),
                cliSelected.getCnpj() == null ? "" : cliSelected.getCnpj(),
                cliSelected.getRg() == null ? "" : cliSelected.getRg());

        if (existente.length() == 0) {
            cliService.addCliente(cliSelected);
            Clientes c = cliService.getClienteRg(cliSelected.getRg());
            evtSelected.setCliente(c);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Cliente salvo com sucesso!!", null));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "" + existente, null));
        }

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
//        Date dataAtual = new Date();
//        if (evtSelected.getDataEvento().compareTo(dataAtual) || evtSelected.getDataEntrega().compareTo(dataAtual) || evtSelected.getDataDevolucao().compareTo(dataAtual)) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                    "Data inferior a data atual!", null));
//        } else {
        evtService.setEvento(evtSelected);
        esconde();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Alterado com sucesso!", null));
//        }
    }

    public static Calendar DateToCalendar(Date date, boolean setTimeToZero) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(date);
        if (setTimeToZero) {
            calendario.set(Calendar.HOUR_OF_DAY, 0);
            calendario.set(Calendar.MINUTE, 0);
            calendario.set(Calendar.SECOND, 0);
            calendario.set(Calendar.MILLISECOND, 0);
        }
        return calendario;
    }

    public void salvarNovo() {
        try {
            Calendar dataAtual = DateToCalendar(new Date(), true);
            Calendar dataEvento = Calendar.getInstance();
            dataEvento.setTime(evtSelected.getDataEvento());

            Calendar dataEntrega = Calendar.getInstance();
            dataEntrega.setTime(evtSelected.getDataEntrega());

            Calendar dataDev = Calendar.getInstance();
            dataDev.setTime(evtSelected.getDataDevolucao());

            if (dataEvento.compareTo(dataAtual) < 0 || dataEntrega.compareTo(dataAtual) < 0 || dataDev.compareTo(dataAtual) < 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Data inferior a data atual!", null));
            } else {
                evtSelected.setFinalizado(false);
                evtSelected.setConfirmado(false);
                evtSelected.setFgAtivo(1);
                evtService.addEvento(evtSelected);
                showAdd = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Inserido com sucesso!", null));

            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro ao salvar!", null));
        }
    }

    public int totalEventosFinalizados() {
        int totalFinalizados = 0;
        totalFinalizados = evtService.totalFinalizados(dataInt);
        return totalFinalizados;
    }

    public int totalEventosPorAno() {
        int totalPorAno = 0;
        totalPorAno = evtService.totalPorAno(dataInt);
        return totalPorAno;
    }

    /**
     * ********************************************
     ********************************
     * *******************************
     */
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

    public List<Clientes> getListClientes() {
        return listClientes;
    }

    public void setListClientes(List<Clientes> listClientes) {
        this.listClientes = listClientes;
    }

    public Clientes getCliSelected() {
        return cliSelected;
    }

    public void setCliSelected(Clientes cliSelected) {
        this.cliSelected = cliSelected;
    }

    public String getLocalizarCli() {
        return localizarCli;
    }

    public void setLocalizarCli(String localizarCli) {
        this.localizarCli = localizarCli;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Eventos getEvento() {
        return evento;
    }

    public void setEvento(Eventos evento) {
        this.evento = evento;
    }

//    public List<ItensOrcamento> getListItensOrcamento() {
//        if (listItensOrcamento != null) {
//            listItensOrcamento = null;
//        }
//        if (evtSelected.getIdEvento() == null) {
//            listItensOrcamento = itemOrcService.getItemEventosParametro(9999999);
//        } else {
//            listItensOrcamento = itemOrcService.getItemEventosParametro(evtSelected.getIdEvento());
//        }
//        return listItensOrcamento;
//    }
    public void setListItensOrcamento(List<ItensOrcamento> listItensOrcamento) {
        this.listItensOrcamento = listItensOrcamento;
    }

    public ItensOrcamento getItemEvento() {
        return itemEvento;
    }

    public void setItemEvento(ItensOrcamento itemEvento) {
        this.itemEvento = itemEvento;
    }

    public ServicoItensOrcamento getItemEvtService() {
        return itemOrcService;
    }

    public void setItemEvtService(ServicoItensOrcamento itemOrcService) {
        this.itemOrcService = itemOrcService;
    }

    public ItensOrcamento getItemEventoTemporario() {
        return itemEventoTemporario;
    }

    public void setItemEventoTemporario(ItensOrcamento itemEventoTemporario) {
        this.itemEventoTemporario = itemEventoTemporario;
    }

    public boolean isEventoSalvo() {
        return eventoSalvo;
    }

    public void setEventoSalvo(boolean eventoSalvo) {
        this.eventoSalvo = eventoSalvo;
    }

    public OrcamentoControl getEvtSelectedOrc() {
        return evtSelectedOrc;
    }

    public void setEvtSelectedOrc(OrcamentoControl evtSelectedOrc) {
        this.evtSelectedOrc = evtSelectedOrc;
    }

    public ArrayList<String> getSelectedCheck() {
        return selectedCheck;
    }

    public void setSelectedCheck(ArrayList<String> selectedCheck) {
        this.selectedCheck = selectedCheck;
    }

    public boolean isEntrega() {
        return entrega;
    }

    public void setEntrega(boolean entrega) {
        this.entrega = entrega;
    }

    public UsuarioControl getUserControl() {
        return userControl;
    }

    public ArrayList<SelectItem> getListTipo() {
        if (listTipo == null || listTipo.size() == 0) {
            listTipo = new ArrayList<SelectItem>();
            listTipo.add(new SelectItem("F", "Física"));
            listTipo.add(new SelectItem("J", "Jurídica"));
        }
        return listTipo;
    }

    public void setListTipo(ArrayList<SelectItem> listTipo) {
        this.listTipo = listTipo;
    }

    public boolean isCpf() {
        return cpf;
    }

    public void setCpf(boolean cpf) {
        this.cpf = cpf;
    }

    public boolean isCpfReq() {
        return cpfReq;
    }

    public void setCpfReq(boolean cpfReq) {
        this.cpfReq = cpfReq;
    }

    public boolean isCnpj() {
        return cnpj;
    }

    public void setCnpj(boolean cnpj) {
        this.cnpj = cnpj;
    }

    public boolean isCnpjReq() {
        return cnpjReq;
    }

    public void setCnpjReq(boolean cnpjReq) {
        this.cnpjReq = cnpjReq;
    }

}
