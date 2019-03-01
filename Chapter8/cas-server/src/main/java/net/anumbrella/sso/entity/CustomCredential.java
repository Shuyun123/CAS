
package net.anumbrella.sso.entity;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apereo.cas.authentication.UsernamePasswordCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.Size;

/**
 * @author Anumbrella
 */
public class CustomCredential extends UsernamePasswordCredential {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomCredential.class);

    private static final long serialVersionUID = -4166149641561667276L;

    @Size(min = 1, message = "required.email")
    private String email;


    @Size(min = 1, message = "required.telephone")
    private String telephone;


    @Size(min = 6, max = 6, message = "required.capcha")
    private String capcha;

    public String getCapcha() {
        return capcha;
    }

    public void setCapcha(String capcha) {
        this.capcha = capcha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(final String telephone) {
        this.telephone = telephone;
    }

    public CustomCredential() {
    }


    public CustomCredential(final String email, final String telephone) {
        this.email = email;
        this.telephone = telephone;
    }


    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof CustomCredential)) {
            return false;
        } else {
            CustomCredential other = (CustomCredential) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$email = this.email;
                Object other$email = other.email;
                if (this$email == null) {
                    if (other$email != null) {
                        return false;
                    }
                } else if (!this$email.equals(other$email)) {
                    return false;
                }

                Object this$telephone = this.telephone;
                Object other$telephone = other.telephone;
                if (this$telephone == null) {
                    if (other$telephone != null) {
                        return false;
                    }
                } else if (!this$telephone.equals(other$telephone)) {
                    return false;
                }

                Object this$capcha = this.capcha;
                Object other$capcha = other.capcha;
                if (this$capcha == null) {
                    if (other$capcha != null) {
                        return false;
                    }
                } else if (!this$capcha.equals(other$capcha)) {
                    return false;
                }

                return true;
            }
        }
    }


    protected boolean canEqual(final Object other) {
        return other instanceof CustomCredential;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(this.email)
                .append(this.telephone)
                .toHashCode();
    }


}
