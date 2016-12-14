package br.com.locacao.controle;

import br.com.locacao.entidades.Categorias;
import br.com.locacao.entidades.Eventos;
import br.com.locacao.servicos.ServicoCategoria;
import br.com.locacao.servicos.ServicoEvento;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
public class ChecklistControl extends BasicControl implements java.io.Serializable {

    @EJB
    private ServicoEvento evtService;
    @Inject
    private UsuarioControl userControl;
    private List<Eventos> listEventos;
    private Eventos evento;
    private String localizarNome;
    private String titulo;    


    @PostConstruct
    public void init() {
        pesquisaTodos();
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
    
    public void pesquisaEvento(){
        if (localizarNome == null){
            pesquisaTodos();
        }
        else if(!localizarNome.matches("^[a-zA-ZÁÂÃÀÇÉÊÍÓÔÕÚÜáâãàçéêíóôõúü]*$")){
            evento = evtService.getEvento(Integer.parseInt(localizarNome));
            listEventos.clear();
            listEventos.add(evento);
        }else {
            listEventos.clear();
            listEventos = evtService.getEventoPorCliente(localizarNome);
        }
    }       
    
    public void imprimir(){
        evtService.imprimirCheckList(titulo == "E" ? "Entrada" : "Saída", evento.getIdEvento() ,userControl.getLoggedUser().getEmpresa().getIdEmpresa() );
    }
    
    //******** GETTERS E SETTER **************
    public String getLocalizarNome() {
        return localizarNome;
    }

    public void setLocalizarNome(String localizarNome) {
        this.localizarNome = localizarNome;
    }


    public List<Eventos> getListEventos() {
        return listEventos;
    }

    public void setListEventos(List<Eventos> listEventos) {
        this.listEventos = listEventos;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Eventos getEvento() {
        return evento;
    }

    public void setEvento(Eventos evento) {
        this.evento = evento;
    }
    
    
}
