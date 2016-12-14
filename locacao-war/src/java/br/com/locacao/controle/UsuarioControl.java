package br.com.locacao.controle;

import br.com.locacao.entidades.Usuarios;
import br.com.locacao.servicos.ServicoUsuario;
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
import javax.imageio.stream.FileImageOutputStream;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.validation.constraints.NotNull;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartSeries;


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
    private String novaSenha;
    private String novaSenhaConfirma;
    private List<Usuarios> listUsuarios;
    private boolean showAdd = false;
    private boolean showEdit = false;
    private boolean alterarSenha;
    private Usuarios usuSelected;
    private String localizarNome;
    private UploadedFile file;
    private BarChartModel animatedModel2;
    
    @PostConstruct
    public void init() {
        esconde();
        pesquisaTodos();
        createAnimatedModels();
    }
    
    private void createAnimatedModels() {     
        animatedModel2 = initBarModel();
        animatedModel2.setTitle("Bar Charts");
        animatedModel2.setAnimate(true);
        animatedModel2.setLegendPosition("ne");
        Axis yAxis = animatedModel2.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(200);
    }
    
    private BarChartModel initBarModel() {
        BarChartModel model = new BarChartModel();
 
        ChartSeries boys = new ChartSeries();
        boys.setLabel("Boys");
        boys.set("2004", 120);
        boys.set("2005", 100);
        boys.set("2006", 44);
        boys.set("2007", 150);
        boys.set("2008", 25);
 
        ChartSeries girls = new ChartSeries();
        girls.setLabel("Girls");
        girls.set("2004", 52);
        girls.set("2005", 60);
        girls.set("2006", 110);
        girls.set("2007", 135);
        girls.set("2008", 120);
 
        model.addSeries(boys);
        model.addSeries(girls);
         
        return model;
    }
    
    public void onRowSelect(SelectEvent event) {
        showAdd = false;
        showEdit = true;
    }
    
    public void alterarSenha(){
        alterarSenha = true;
    }
    
    public void salvarSenha(){
        if (novaSenha.equals(novaSenhaConfirma)){
            usuSelected.setSenha(novaSenha);
            userService.setUsuario(usuSelected);
            alterarSenha = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Senha de usuário "+usuSelected.getNome()+" alterada com sucesso!", null));
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Senhas não coincidem!", null));
        }
    }

    public String getPathImage() {
        String pathImage = "";
        if (loggedUser.getIdUsuario() != null) {
            try {
                FacesContext context = FacesContext.getCurrentInstance();
                ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
                String imageUsers = servletContext.getRealPath("resources/dist/img/");
                File dirImageUsers = new File(imageUsers);
                if (!dirImageUsers.exists()) {
                    dirImageUsers.createNewFile();
                }

                if (loggedUser.getImagem() != null) {
                    byte[] bytes = loggedUser.getImagem();
                    FileImageOutputStream imageOutput = new FileImageOutputStream(new File(dirImageUsers, loggedUser.getIdUsuario()+ ".png"));
                    imageOutput.write(bytes, 0, bytes.length);
                    imageOutput.flush();
                    imageOutput.close();

                    pathImage = "../../resources/dist/img/" + loggedUser.getIdUsuario()+ ".png";
                } else {
                    pathImage = "../../resources/dist/img/avatar5.png";
                }

            } catch (FileNotFoundException ex) {
                Logger.getLogger(UsuarioControl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {

            }
        }
        return pathImage;
    }
    

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void upload() {
        if (file != null) {
            byte[] photo = file.getContents();
            loggedUser.setImagem(photo);
            userService.setUsuario(loggedUser);
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Imagem salva com sucesso!", null));
        }
    }

    public String doLogin() {
        loggedUser = null;
        loggedUser = userService.getUsuarioByLoginSenha(username, senha);
        if (loggedUser == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Email ou senha incorretos!", null));
            return null;
        } else if (loggedUser.getAdmin() == true) {
            return "/restrito/admin/index.faces?faces-redirect=true";
        } else if (loggedUser.getAdmin() == false) {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "O sistema foi configarado para apenas administradores terem acesso!", null));
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
        if (localizarNome == null) {
            pesquisaTodos();
        } else {
            listUsuarios = null;
            listUsuarios = userService.getUsuarioParametro(localizarNome);
        }
    }

    public void esconde() {
        showAdd = false;
        showEdit = false;
        alterarSenha = false;
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

    public String editarUsuario() {
        userService.alterUsuario(loggedUser);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Alterado com sucesso!", null));
        return "/restrito/admin/index.faces?faces-redirect=true";
    }

    public void salvarEditado() {
        userService.alterUsuario(usuSelected);
        esconde();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Alterado com sucesso!", null));
    }

    public void salvarNovo() {
        try {
            usuSelected.setEmpresa(loggedUser.getEmpresa());
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

    public boolean isAlterarSenha() {
        return alterarSenha;
    }

    public void setAlterarSenha(boolean alterarSenha) {
        this.alterarSenha = alterarSenha;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getNovaSenhaConfirma() {
        return novaSenhaConfirma;
    }

    public void setNovaSenhaConfirma(String novaSenhaConfirma) {
        this.novaSenhaConfirma = novaSenhaConfirma;
    }

    public BarChartModel getAnimatedModel2() {
        return animatedModel2;
    }

    public void setAnimatedModel2(BarChartModel animatedModel2) {
        this.animatedModel2 = animatedModel2;
    }
    
    
}
