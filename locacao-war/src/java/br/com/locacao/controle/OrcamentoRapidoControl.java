package br.com.locacao.controle;

import br.com.locacao.entidades.Eventos;
import br.com.locacao.entidades.ItensOrcamento;
import br.com.locacao.entidades.Orcamento;
import br.com.locacao.entidades.OrcamentoRapidoEntity;
import br.com.locacao.entidades.Produtos;
import br.com.locacao.servicos.ServicoItensOrcamento;
import br.com.locacao.servicos.ServicoOrcamento;
import br.com.locacao.servicos.ServicoProduto;
import java.util.ArrayList;
import java.util.Date;
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
@ViewScoped
public class OrcamentoRapidoControl extends BasicControl implements java.io.Serializable {

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
    private List<OrcamentoRapidoEntity> listProdutosPorOrcamento = new ArrayList<OrcamentoRapidoEntity>();

    private Eventos evtSelectedOrc;
    private Produtos prodSelected;
    private Orcamento orcSelected;
    private ItensOrcamento itemRemover;

    private Date dataEntrega;
    private Date dataDevolucao;

    private String localizarNome;
    private String observacao;
    private String formaPagto;
    
    private int quantidade;
    private int totalItens = 0;
    private double totalOrcamento;
    private double subtotal;
    private int nroDiarias;
    private double frete;
    private double desconto;
    

    @PostConstruct
    public void init() {
        pesquisaTodosProdutos();
    }

    public void pesquisaProduto() {
        if (localizarNome == null || localizarNome.isEmpty()) {
            pesquisaTodosProdutos();
        } else if (localizarNome.length() > 2) {
            listProdutos = null;
            listProdutos = prodService.getProdutosByName(localizarNome);
        }
    }
    
    public void novo(){
        dataEntrega = null;
        dataDevolucao = null;
        quantidade = 0;
        localizarNome = "";
        pesquisaTodosProdutos();
        listProdutosPorOrcamento = new ArrayList<OrcamentoRapidoEntity>();
        nroDiarias = 0;
        frete = 0;
        desconto = 0;
        observacao = "";
        totalItens = 0;
        totalOrcamento = 0;
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
        } else if (dataEntrega == null || dataDevolucao == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Intervalo de datas incorreto!", null));
        } else {
            prodSelected = ((Produtos) event.getObject());
            //verificar disponibilidade do produto
            if (quantidade > prodSelected.getQuantidade()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Quantidade em estoque insuficiente", null));
            } else {
                int quantidadeLocada = 0;
                int quantidadeParaEvento = 0;

                quantidadeLocada = itensOrcService.getQuantidadeProdutoPorData(dataEntrega, dataDevolucao, prodSelected.getIdProduto());
                quantidadeParaEvento = quantidadeLocada + quantidade;
                int quantidadePossivel = prodSelected.getQuantidade() - quantidadeLocada;

                if (quantidadeParaEvento <= prodSelected.getQuantidade()) {
                    OrcamentoRapidoEntity o = new OrcamentoRapidoEntity();
                    o.setProduto(prodSelected);
                    o.setQuantidade(quantidade);

                    listProdutosPorOrcamento.add(o);
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Já existem " + quantidadeLocada + " " + prodSelected.getNome() + "(s) locadados entre as datas "
                            + comumControl.formataData(dataEntrega) + " e "
                            + comumControl.formataData(dataDevolucao) + ". "
                            + "Você pode locar no máximo " + quantidadePossivel + " quantidade(s) deste produto!", null));
                }

            }
        }
    }

    public void onRowUnselect(UnselectEvent event) {
        prodSelected = null;
    }

    public void removeProdLista(OrcamentoRapidoEntity itemOrc) {
        listProdutosPorOrcamento.remove(itemOrc);
    }

    public void imprimeOrcamento() {
        orcService.imprimeOrcamentoRapido(usuControl.getLoggedUser().getNome(), usuControl.getLoggedUser().getEmpresa().getIdEmpresa(), dataEntrega, dataDevolucao, desconto, frete, totalOrcamento,
        formaPagto, observacao, subtotal, listProdutosPorOrcamento);
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
        return orcSelected;
    }

    public void setOrcSelected(Orcamento orcSelected) {
        this.orcSelected = orcSelected;
    }

    public List<OrcamentoRapidoEntity> getListProdutosPorOrcamento() {
        return listProdutosPorOrcamento;
    }

    public void setListProdutosPorOrcamento(List<OrcamentoRapidoEntity> listProdutosPorOrcamento) {
        this.listProdutosPorOrcamento = listProdutosPorOrcamento;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getTotalItens() {
        totalItens = listProdutosPorOrcamento == null ? 0 : listProdutosPorOrcamento.size();
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
        if (listProdutosPorOrcamento != null) {
            for (i = 0; i < listProdutosPorOrcamento.size(); i++) {
                quantidadeItem = listProdutosPorOrcamento.get(i).getQuantidade();
                valDiaria = listProdutosPorOrcamento.get(i).getProduto().getValDiaria();
                soma = quantidadeItem * valDiaria;
                totalOrcamento += soma;
            }
        }
        freteTotal = frete;
        valTotal = ((totalOrcamento * nroDiarias) - desconto) + freteTotal;
        subtotal = (totalOrcamento * nroDiarias) + desconto;
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

    public Date getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(Date dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public Date getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(Date dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getFormaPagto() {
        return formaPagto;
    }

    public void setFormaPagto(String formaPagto) {
        this.formaPagto = formaPagto;
    }
    
}
