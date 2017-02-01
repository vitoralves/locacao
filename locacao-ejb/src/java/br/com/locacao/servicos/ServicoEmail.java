package br.com.locacao.servicos;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 *
 * @author vitor
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ServicoEmail extends ServicoBasico {

    private static final long serialVersionUID = 1L;
    private static final String HOSTNAME = "smtp.gmail.com";
    private static final String USERNAME = "vitor.diogo.live@gmail.com";
    private static final String PASSWORD = "vitim2009";

    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    @PostActivate
    private void postConstruct() {

    }

    public ServicoEmail() {

    }

    public static HtmlEmail conectaEmail() throws EmailException {
        HtmlEmail email = new HtmlEmail();
        email.setHostName(HOSTNAME);
        email.setSmtpPort(465);
        email.setAuthentication(USERNAME, PASSWORD);
        email.setTLS(true);
        email.setSSL(true);
        email.setFrom(USERNAME);
        return email;
    }

    public void enviaEmail(String assunto, String mensagem, String destinatario, String caminho) throws EmailException {
        HtmlEmail email = new HtmlEmail();
        email = conectaEmail();
        email.setSubject(assunto);
        email.setMsg(mensagem);
        email.addTo(destinatario);
        //anexo
        if (caminho != null) {
            EmailAttachment anexo = new EmailAttachment();
            anexo.setPath(caminho);
            anexo.setDisposition(EmailAttachment.ATTACHMENT);
            anexo.setName("orcamento.pdf");
            email.attach(anexo);
        }
        String resposta = email.send();
        System.out.println(resposta);
    }
}
