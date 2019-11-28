package baseGeneral;

/**
 * 
 * @author marco
 */

public class ErrorEntity {

    private Boolean error;
    private String tipo;
    private Integer numError;
    
    public ErrorEntity() {
        this.error = true;
        this.tipo = "Error";
    }
    
    public ErrorEntity(Boolean error) {
        this.error = error;
        this.tipo = "Error";
    }
    
    public ErrorEntity(Boolean error, String tipo) {
        this.error = error;
        this.tipo = tipo;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getNumError() {
        return numError;
    }

    public void setNumError(Integer numError) {
        this.numError = numError;
    }
    
}
