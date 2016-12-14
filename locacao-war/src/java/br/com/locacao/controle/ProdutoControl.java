package br.com.locacao.controle;

import br.com.locacao.entidades.Categorias;
import br.com.locacao.entidades.Produtos;
import br.com.locacao.servicos.ServicoCategoria;
import br.com.locacao.servicos.ServicoProduto;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.imageio.stream.FileImageOutputStream;
import javax.inject.Named;
import javax.servlet.ServletContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author vitor
 */
@Named
@ViewScoped
public class ProdutoControl extends BasicControl implements java.io.Serializable {

    @EJB
    private ServicoProduto prodService;
    @EJB
    private ServicoCategoria catService;
    private Produtos prodSelected;
    private List<Produtos> prodFiltrado;
    private List<Produtos> listProdutos;
    private boolean showAdd = false;
    private boolean showEdit = false;
    private String localizarNome;
    private int quantidade;
    private List<Categorias> listCategorias;
    private UploadedFile file;
    private Double valor = 10.0;

    @PostConstruct
    public void init() {
        esconde();
        pesquisaTodos();
        getListCategorias();
    }

    public void onRowSelect(SelectEvent event) {
        showAdd = false;
        showEdit = true;
    }

    public void upload() {
        if (file != null) {
            byte[] photo = file.getContents();
            prodSelected.setImagem(photo);
            prodService.setProduto(prodSelected);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Imagem salva com sucesso!", null));
        }
    }

    public String getPathImage() {
        String pathImage = "";
        if (prodSelected != null) {
            try {
                FacesContext context = FacesContext.getCurrentInstance();
                ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
                String imageUsers = servletContext.getRealPath("resources/dist/img/");
                File dirImageUsers = new File(imageUsers);
                if (!dirImageUsers.exists()) {
                    dirImageUsers.createNewFile();
                }

                if (prodSelected.getImagem() != null) {
                    byte[] bytes = prodSelected.getImagem();
                    FileImageOutputStream imageOutput = new FileImageOutputStream(new File(dirImageUsers, prodSelected.getIdProduto() + ".png"));
                    imageOutput.write(bytes, 0, bytes.length);
                    imageOutput.flush();
                    imageOutput.close();

                    pathImage = "../../resources/dist/img/" + prodSelected.getIdProduto() + ".png";
                } else {
                    pathImage = "../../resources/dist/img/imgPadrao.png";
                }

            } catch (FileNotFoundException ex) {
                Logger.getLogger(UsuarioControl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {

            }
        } else {
            pathImage = "../../resources/dist/img/imgPadrao.png";
        }
        return pathImage;
    }

    public List<Produtos> pesquisaTodos() {
        if (listProdutos == null) {
            listProdutos = prodService.getProdutos();
        } else {
            listProdutos = null;
            listProdutos = prodService.getProdutos();
        }
        return listProdutos;
    }

    public int totalProdutos() {
        return prodService.getQuantidadeEstoque();
    }
    
    public void excluirImagem(){
        prodSelected.setImagem(null);
        prodService.setProduto(prodSelected);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Imagem excluída com sucesso!", null));
    }
    
    public void pesquisaProduto() {
        if (localizarNome == null || localizarNome.isEmpty()) {
            pesquisaTodos();
        } else {
            listProdutos = null;
            listProdutos = prodService.getProdutosByName(localizarNome);
        }
    }

    public void esconde() {
        showAdd = false;
        showEdit = false;
    }

    public void addNovo() {
        prodSelected = new Produtos();
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
        prodService.removeProduto(prodSelected);
        showEdit = false;
        showAdd = false;
        pesquisaTodos();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Excluido com sucesso!", null));
    }

    public void salvarEditado() {
        if (file != null) {
            byte[] photo = file.getContents();
            prodSelected.setImagem(photo);
        }
        prodService.setProduto(prodSelected);
        esconde();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Alterado com sucesso!", null));
    }

    public void salvarNovo() {
        try {
            if (file != null) {
                byte[] photo = file.getContents();
                prodSelected.setImagem(photo);
            }
            prodService.addProduto(prodSelected);
            showAdd = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Inserido com sucesso!", null));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro ao salvar!", null));
        }
    }

    public String getLocalizarNome() {
        return localizarNome;
    }

    public void setLocalizarNome(String localizarNome) {
        this.localizarNome = localizarNome;
    }

    public List<Produtos> getListProdutos() {
        if (listProdutos == null) {
            pesquisaProduto();
            return listProdutos;
        } else {
            return listProdutos;
        }
    }

    public void setListProdutos(List<Produtos> listProdutos) {
        this.listProdutos = listProdutos;
    }

    public Produtos getProdSelected() {
        return prodSelected;
    }

    public void setProdSelected(Produtos prodSelected) {
        this.prodSelected = prodSelected;
    }

    public List<Produtos> getProdFiltrado() {
        return prodFiltrado;
    }

    public void setProdFiltrado(List<Produtos> prodFiltrado) {
        this.prodFiltrado = prodFiltrado;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public List<Categorias> getListCategorias() {
        if (listCategorias == null) {
            listCategorias = catService.getCategorias();
        } else {
            listCategorias = null;
            listCategorias = catService.getCategorias();
        }
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

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

}
