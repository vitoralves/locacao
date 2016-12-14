package br.com.locacao.controle;

import br.com.locacao.entidades.Empresa;
import br.com.locacao.servicos.ServicoEmpresa;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.imageio.stream.FileImageOutputStream;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author vitor
 */
@Named
@ViewScoped
public class EmpresaControl extends BasicControl implements java.io.Serializable {

    @EJB
    private ServicoEmpresa empService;
    private Empresa empresa;

    @Inject
    private UsuarioControl usuControl;

    private UploadedFile file;

    @PostConstruct
    public void init() {
        pesquisaEmpresa();
    }

    public void editarEmpresa() {
        empService.setEmpresa(empresa);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Alterado com sucesso!", null));
    }

    public void pesquisaEmpresa() {
        empresa = empService.getEmpresa(usuControl.getLoggedUser().getEmpresa().getIdEmpresa());
    }

    public String getPathImage() {
        String pathImage = "";
        if (empresa.getIdEmpresa() != null) {
            try {
                FacesContext context = FacesContext.getCurrentInstance();
                ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
                String imageUsers = servletContext.getRealPath("resources/dist/img/");
                File dirImageUsers = new File(imageUsers);
                if (!dirImageUsers.exists()) {
                    dirImageUsers.createNewFile();
                }

                if (empresa.getLogo() != null) {
                    byte[] bytes = empresa.getLogo();
                    FileImageOutputStream imageOutput = new FileImageOutputStream(new File(dirImageUsers, empresa.getNome()+ ".png"));
                    imageOutput.write(bytes, 0, bytes.length);
                    imageOutput.flush();
                    imageOutput.close();

                    pathImage = "../../resources/dist/img/" + empresa.getNome() + ".png";
                } else {
                    pathImage = "";
                }

            } catch (FileNotFoundException ex) {
                Logger.getLogger(EmpresaControl.class.getName()).log(Level.SEVERE, null, ex);
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
            empresa.setLogo(photo);
            empService.setEmpresa(empresa);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Imagem salva com sucesso!", null));
        }
    }

    public UsuarioControl getUsuControl() {
        return usuControl;
    }

    public void setUsuControl(UsuarioControl usuControl) {
        this.usuControl = usuControl;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

}
