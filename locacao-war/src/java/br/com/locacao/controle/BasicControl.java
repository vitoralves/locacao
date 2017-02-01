package br.com.locacao.controle;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author vitor
 */
public abstract class BasicControl implements java.io.Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");
    
    protected void createFacesErrorMessage(String msg){
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, fm);
    }
    
    protected Set<ConstraintViolation<Serializable>> getViolations(Serializable entidade){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Serializable>> toReturn = validator.validate(entidade);
        return toReturn;
    }
    
    protected boolean existViolationsForJSF(Serializable entidade){
        Set<ConstraintViolation<Serializable>> toReturn = getViolations(entidade);
        if(toReturn.isEmpty()) return false;
        for (ConstraintViolation<Serializable> constraintViolation : toReturn){
            createFacesErrorMessage(constraintViolation.getMessage());
        }
        return true;
    }

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public void setSdf(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }
    
    
}
