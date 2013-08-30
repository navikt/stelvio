package no.nav.esso;

import java.io.Serializable;
import java.security.Principal;

public class OneDayPwPrincipal implements Principal, Serializable {
    private String name;

    public OneDayPwPrincipal(String name) {
        if (name == null) {
            throw new NullPointerException("illegal null input");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return("SamplePrincipal:  " + name);
    }

    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof OneDayPwPrincipal)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        OneDayPwPrincipal that = (OneDayPwPrincipal)o;
        return this.getName().equals(that.getName());
    }

    public int hashCode() {
        return name.hashCode();
    }
}