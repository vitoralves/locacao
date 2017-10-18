package br.com.locacao.controle;

import br.com.locacao.entidades.Eventos;
import br.com.locacao.entidades.ItensOrcamento;
import br.com.locacao.entidades.Orcamento;
import br.com.locacao.entidades.Produtos;
import br.com.locacao.servicos.ServicoItensOrcamento;
import br.com.locacao.servicos.ServicoOrcamento;
import br.com.locacao.servicos.ServicoProduto;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author vitor
 */
@Named
@SessionScoped
public class OrcamentoControl extends BasicControl implements java.io.Serializable {

    @EJB
    private ServicoOrcamento orcService;
    @EJB
    private ServicoProduto prodService;
    @EJB
    private ServicoItensOrcamento itensOrcService;

    @Inject
    private ComumControl comumControl;
    @Inject
    private UsuarioControl usuControl;

    private List<Produtos> listProdutos;
    private List<ItensOrcamento> listProdutosPorOrcamento;

    private Eventos evtSelectedOrc;
    private Produtos prodSelected;
    private Orcamento orcSelected;
    private ItensOrcamento itensOrcSelected;
    private ItensOrcamento itemRemover;

    private String localizarNome;
    private int quantidade;
    private int totalItens = 0;
    private double totalOrcamento;
    private int nroDiarias;
    private double frete;
    private double desconto;

    @PostConstruct
    public void init() {
        pesquisaTodosProdutos();
        itensOrcSelected = new ItensOrcamento();
    }

    public String esconde() {
        return "/restrito/admin/eventos.faces?faces-redirect=true";
    }

    public void inicializarVariaveis() {
        if (orcSelected != null) {
            totalOrcamento = orcSelected.getValTotal() == null ? 0 : orcSelected.getValTotal();
            nroDiarias = orcSelected.getDiarias() == null ? 1 : orcSelected.getDiarias();
            frete = orcSelected.getValFrete() == null ? 0 : orcSelected.getValFrete();
            desconto = orcSelected.getDesconto() == null ? 0 : orcSelected.getDesconto();
        }
        quantidade = 0;
        localizarNome = "";
        listProdutos = null;
    }

    public void novoOrcamento(Eventos evt) {
        orcSelected = new Orcamento();
        orcSelected.setEvento(evt);
        orcService.addOrcamento(orcSelected);

    }

    public String voltar() {
        evtSelectedOrc = null;
        return "/restrito/admin/eventos.faces?faces-redirect=true";
    }

    public void pesquisaProduto() {
        if (localizarNome == null || localizarNome.isEmpty()) {
            pesquisaTodosProdutos();
        } else if (localizarNome.length() > 2) {
            listProdutos = null;
            listProdutos = prodService.getProdutosByName(localizarNome);
        }
    }

    public List<Produtos> pesquisaTodosProdutos() {
        if (listProdutos == null) {
            listProdutos = prodService.get10Produtos();
        } else {
            listProdutos = null;
            listProdutos = prodService.get10Produtos();
        }
        return listProdutos;
    }

    public void onRowSelect(SelectEvent event) {
        if (quantidade <= 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Selecione uma quantidade!", null));
        } else {
            prodSelected = ((Produtos) event.getObject());
            //verificar disponibilidade do produto
            if (quantidade > prodSelected.getQuantidade()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Quantidade em estoque insuficiente", null));
            } else {
                int quantidadeLocada = 0;
                int quantidadeParaEvento = 0;

                quantidadeLocada = itensOrcService.getQuantidadeProdutoPorData(orcSelected.getEvento().getDataEntrega(), orcSelected.getEvento().getDataDevolucao(), prodSelected.getIdProduto());
                quantidadeParaEvento = quantidadeLocada + quantidade;
                int quantidadePossivel = prodSelected.getQuantidade() - quantidadeLocada;

                if (quantidadeParaEvento <= prodSelected.getQuantidade()) {
                    itensOrcSelected.setOrcamento(orcSelected);
                    itensOrcSelected.setProduto(prodSelected);
                    itensOrcSelected.setQuantidade(quantidade);

                    itensOrcService.addItemOrcamento(itensOrcSelected);
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Já existem " + quantidadeLocada + " " + prodSelected.getNome() + "(s) locadados entre as datas "
                            + comumControl.formataData(orcSelected.getEvento().getDataEntrega()) + " e "
                            + comumControl.formataData(orcSelected.getEvento().getDataDevolucao()) + ". "
                            + "Você pode locar no máximo " + quantidadePossivel + " quantidade(s) deste produto!", null));
                }

            }
        }
    }

    public void onRowUnselect(UnselectEvent event) {
        prodSelected = null;
    }

    public void removeProdLista(ItensOrcamento itemOrc) {
        itensOrcService.removeItemOrcamento(itemOrc);
    }

    public String salvarOrcamento() {
        if (orcSelected.getValTotal() < 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Orçamento está com valor negativo!", null));
            return "";
        } else {
            orcService.setOrcamento(orcSelected);
            return "/restrito/admin/eventos.faces?faces-redirect=true";
        }
    }

    public void imprimeOrcamento() {
        if (orcSelected.getValTotal() < 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Orçamento está com valor negativo!", null));
        } else {
            orcService.setOrcamento(orcSelected);
            orcService.imprimeOrcamento(orcSelected.getIdOrcamento(), orcSelected.getEvento().getIdEvento(), usuControl.getLoggedUser().getNome(), usuControl.getLoggedUser().getEmpresa().getIdEmpresa());
        }
    }

    public void imprimeContrato() throws InterruptedException {
        if (orcSelected.getValTotal() < 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Orçamento está com valor negativo!", null));
        } else {
            orcService.confirma(orcSelected.getEvento().getIdEvento());
            orcService.setOrcamento(orcSelected);
            orcService.imprimeContrato(orcSelected.getEvento().getIdEvento(), usuControl.getLoggedUser().getEmpresa().getIdEmpresa());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Contrato emitido e evento confirmado!", null));
        }
    }

    /**
     * **************************************
     ***********
     * ***********
     */
    /**
     * ***********
     * @return
     */
    public Eventos getEvtSelectedOrc() {
        return evtSelectedOrc;
    }

    public void setEvtSelectedOrc(Eventos evtSelectedOrc) {
        this.evtSelectedOrc = evtSelectedOrc;
    }

    public List<Produtos> getListProdutos() {
        //pesquisaTodosProdutos();
        return listProdutos;
    }

    public void setListProdutos(List<Produtos> listProdutos) {
        this.listProdutos = listProdutos;
    }

    public String getLocalizarNome() {
        return localizarNome;
    }

    public void setLocalizarNome(String localizarNome) {
        this.localizarNome = localizarNome;
    }

    public Produtos getProdSelected() {
        return prodSelected;
    }

    public void setProdSelected(Produtos prodSelected) {
        this.prodSelected = prodSelected;
    }

    public Orcamento getOrcSelected() {
        inicializarVariaveis();
        return orcSelected;
    }

    public void setOrcSelected(Orcamento orcSelected) {
        this.orcSelected = orcSelected;
    }

    public List<ItensOrcamento> getListProdutosPorOrcamento() {
        listProdutosPorOrcamento = null;
        listProdutosPorOrcamento = itensOrcService.getItemOrcamentosPorIdOrcamento(orcSelected.getIdOrcamento());
        return listProdutosPorOrcamento;
    }

    public void setListProdutosPorOrcamento(List<ItensOrcamento> listProdutosPorOrcamento) {
        this.listProdutosPorOrcamento = listProdutosPorOrcamento;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getTotalItens() {
        totalItens = listProdutosPorOrcamento.size();
        return totalItens;
    }

    public void setTotalItens(int totalItens) {
        this.totalItens = totalItens;
    }

    public double getTotalOrcamento() {
        int i;
        int quantidadeItem = 0;
        double valDiaria = 0;
        double soma = 0;
        double freteTotal = 0;
        double valTotal = 0;
        totalOrcamento = 0;
        for (i = 0; i < listProdutosPorOrcamento.size(); i++) {
            quantidadeItem = listProdutosPorOrcamento.get(i).getQuantidade();
            valDiaria = listProdutosPorOrcamento.get(i).getProduto().getValDiaria();
            soma = quantidadeItem * valDiaria;
            totalOrcamento += soma;
        }
        freteTotal = orcSelected.getValFrete() == null ? 0 : orcSelected.getValFrete();
        valTotal = ((totalOrcamento * nroDiarias) - desconto) + freteTotal;

        orcSelected.setDesconto(desconto);
        orcSelected.setDiarias(nroDiarias);
        orcSelected.setValFrete(frete);
        orcSelected.setValTotal(valTotal);

        return valTotal;
    }

    public void setTotalOrcamento(double totalOrcamento) {
        this.totalOrcamento = totalOrcamento;
    }

    public int getNroDiarias() {
        return nroDiarias;
    }

    public void setNroDiarias(int nroDiarias) {
        this.nroDiarias = nroDiarias;
        getTotalOrcamento();
    }

    public ItensOrcamento getItemRemover() {
        return itemRemover;
    }

    public void setItemRemover(ItensOrcamento itemRemover) {
        this.itemRemover = itemRemover;
    }

    public double getFrete() {
        return frete;
    }

    public void setFrete(double frete) {
        this.frete = frete;
        getTotalOrcamento();
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
        getTotalOrcamento();
    }
}
