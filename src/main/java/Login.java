
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author sammarju
 */
@Named(value = "login")
@RequestScoped
public class Login {
    
    private String etunimi = "Testi"; 

    /**
     * Creates a new instance of Login
     */
    public Login() {
        
    }

    public String getEtunimi() {
        return etunimi;
    }

    public void setEtunimi(String etunimi) {
        this.etunimi = etunimi;
    }
    
    
    
}
