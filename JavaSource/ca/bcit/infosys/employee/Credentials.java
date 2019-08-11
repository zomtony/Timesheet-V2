package ca.bcit.infosys.employee;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;

/**
 * Login Credentials.
 * @author blink
 * @version 1.1
 */
@ConversationScoped
public class Credentials implements Serializable {

    private static final long serialVersionUID = 11L;
    /** The login ID. */
    private String userName;
    /** The password. */
    private String password;
    /**
     * The no-argument constructor. Used to create new employees from within the
     * application.
     */
    public Credentials() {
    }
    /**
     * userName getter.
     * @return the loginID
     */
    public String getUserName() {
        return userName;
    }
    /**
     * userName setter.
     * @param id the loginID to set
     */
    public void setUserName(final String id) {
        userName = id;
    }
    /**
     * password getter.
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    /**
     * password setter.
     * @param pw the password to set
     */
    public void setPassword(final String pw) {
        password = pw;
    }

}
