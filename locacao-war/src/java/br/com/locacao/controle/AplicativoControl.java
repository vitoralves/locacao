package br.com.locacao.controle;

import br.com.locacao.entidades.ItensOrcamentoApp;
import br.com.locacao.entidades.MensagemApp;
import br.com.locacao.entidades.OrcamentoApp;
import br.com.locacao.servicos.ServicoClienteApp;
import br.com.locacao.servicos.ServicoEmail;
import br.com.locacao.servicos.ServicoItensOrcamentoApp;
import br.com.locacao.servicos.ServicoMensagemApp;
import br.com.locacao.servicos.ServicoOrcamentoApp;
import com.google.common.io.ByteStreams;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import org.apache.commons.mail.EmailException;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author vitor
 */
@Named
@ViewScoped
public class AplicativoControl extends BasicControl implements java.io.Serializable {

    @EJB
    ServicoClienteApp service;
    @EJB
    ServicoOrcamentoApp orcService;
    @EJB
    ServicoMensagemApp msgService;
    @EJB
    ServicoEmail emailService;
    @EJB
    ServicoItensOrcamentoApp itemService;

    @Inject
    private UsuarioControl usuControl;

    private List<OrcamentoApp> listOrcamento;
    private List<MensagemApp> listMensagem;
    private List<ItensOrcamentoApp> listagemItens;

    private String destinatario;
    private String assunto;
    private String loadingEmail = "none";
    private String menssagem;

    private double total;
    private double frete;
    private double desconto;
    private double totalGeral;

    private int diarias;

    private boolean painelPrincipal = true;
    private boolean painelOrcamento = false;

    private OrcamentoApp orcSelected;
    private MensagemApp msgSelected;

    private Part anexoEmail;

    @PostConstruct
    public void init() {

    }

    public long totalClientes() {
        return service.getQuantidadeClientes();
    }

    public void pesquisaOrcamentos() {
        listOrcamento = null;
        listOrcamento = orcService.getTodosOrcamentosAtivos();
    }

    public long totalOrcamentos() {
        return orcService.getQuantidadeOrcamentos();
    }

    public long totalMensagens() {
        return msgService.getQuantidadeMensagens();
    }

    public void enviaEmail() throws FileNotFoundException {
        String caminho = null;
        //resgato o caminho da pasta anexo que salvará o orçamento temporariamente
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
        String pathRel = servletContext.getRealPath("/resources/anexo/");
        File file = null;
        try {
            //cria o arquivo
            file = File.createTempFile("orcamento", ".pdf", new File(pathRel));
            //resgata os dados do aquivo inserido
            InputStream input = anexoEmail.getInputStream();
            //resgata os bytes do arquivo inserido
            byte[] bytes = ByteStreams.toByteArray(input);
            //insere os bytes resgatados no arquivo criado
            OutputStream outStream = new FileOutputStream(file);
            outStream.write(bytes);
            caminho = bytes.length > 0 ? file.getPath().toString() : null;
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Falha ao ler anexo "+ex, null));
        }
        try {
            emailService.enviaEmail(assunto, menssagem, destinatario, caminho);
        } catch (EmailException ex) {
            Logger.getLogger(AplicativoControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        assunto = null;
        menssagem = null;
        destinatario = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "E-mail enviado com sucesso!", null));
    }

    public void marcarEnviado() {
        orcSelected.setSituacao("ENVIADO");
        orcSelected.setEnviado(true);

        orcService.marcarEnviado(orcSelected);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Orçamento " + orcSelected.getIdOrcamento() + " marcado como enviado.", null));
    }

    public void marcarMsgEnviado() {
        msgSelected.setRespondido(true);
        msgService.marcarEnviado(msgSelected);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Mensagem "+msgSelected.getIdMensagem()+" marcada como enviada.", null));
    }

    public void onRowSelect(SelectEvent event) {
        painelPrincipal = false;
        painelOrcamento = true;
        listagemItens = itemService.getItemOrcamentoAppPorIdOrcamento(orcSelected.getIdOrcamento());
        total = itemService.calculaTotalPorIdOrcamento(orcSelected.getIdOrcamento());
        calcularTotal();
    }

    public double calcularTotal() {
        frete = orcSelected.getFrete() == null ? 0 : orcSelected.getFrete();
        desconto = orcSelected.getDesconto() == null ? 0 : orcSelected.getDesconto();
        diarias = orcSelected.getDiarias() == null ? 1 : orcSelected.getDiarias();
        totalGeral = ((total - desconto) * diarias) + frete;
        orcSelected.setTotal(totalGeral);
        return totalGeral;
    }

    public void voltar() {
        painelPrincipal = true;
        painelOrcamento = false;
    }

    public void imprimeOrcamento() {
        //salvar orçamento
        orcService.salvar(orcSelected);
        orcService.imprimeOrcamento(orcSelected, usuControl.getLoggedUser().getNome(), usuControl.getLoggedUser().getEmpresa().getIdEmpresa());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Orçamento " + orcSelected.getIdOrcamento() + " salvo.", null));
    }

    ///*************************************GET SET ****************************************//
    public List<OrcamentoApp> getListOrcamento() {
        if (listOrcamento == null) {
            listOrcamento = orcService.getTodosOrcamentosAtivos();
            return listOrcamento;
        } else {
            return listOrcamento;
        }
    }

    public void setListOrcamento(List<OrcamentoApp> listOrcamento) {
        this.listOrcamento = listOrcamento;
    }

    public List<MensagemApp> getListMensagem() {
        if (listMensagem == null) {
            listMensagem = msgService.getTodasMensagens();
            return listMensagem;
        } else {
            return listMensagem;
        }
    }

    public void setListMensagem(List<MensagemApp> listMensagem) {
        this.listMensagem = listMensagem;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getMenssagem() {
        return menssagem;
    }

    public void setMenssagem(String menssagem) {
        this.menssagem = menssagem;
    }

    public OrcamentoApp getOrcSelected() {
        return orcSelected;
    }

    public void setOrcSelected(OrcamentoApp orcSelected) {
        this.orcSelected = orcSelected;
    }

    public MensagemApp getMsgSelected() {
        return msgSelected;
    }

    public void setMsgSelected(MensagemApp msgSelected) {
        this.msgSelected = msgSelected;
    }

    public boolean isPainelPrincipal() {
        return painelPrincipal;
    }

    public void setPainelPrincipal(boolean painelPrincipal) {
        this.painelPrincipal = painelPrincipal;
    }

    public boolean isPainelOrcamento() {
        return painelOrcamento;
    }

    public void setPainelOrcamento(boolean painelOrcamento) {
        this.painelOrcamento = painelOrcamento;
    }

    public List<ItensOrcamentoApp> getListagemItens() {
        return listagemItens;
    }

    public void setListagemItens(List<ItensOrcamentoApp> listagemItens) {
        this.listagemItens = listagemItens;
    }

    public String getLoadingEmail() {
        return loadingEmail;
    }

    public void setLoadingEmail(String loadingEmail) {
        this.loadingEmail = loadingEmail;
    }

    public Part getAnexoEmail() {
        return anexoEmail;
    }

    public void setAnexoEmail(Part anexoEmail) {
        this.anexoEmail = anexoEmail;
    }

}
