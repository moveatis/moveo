package com.moveatis.lotas.user;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Entity
@NamedQueries(
        @NamedQuery(
                name="findUserByName",
                query="SELECT user FROM UserEntity user WHERE user.firstName=:firstName AND user.lastName=:lastName"
        )
)
public class UserEntity extends AbstractUser implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String firstName;
    private String lastName;
    
    

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
