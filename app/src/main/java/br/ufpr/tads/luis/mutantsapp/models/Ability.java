
package br.ufpr.tads.luis.mutantsapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Ability implements Serializable {

    @SerializedName("ability")
    @Expose
    private String ability;
    private final static long serialVersionUID = 6533214229199856384L;

    /**
     * No args constructor for use in serialization
     */
    public Ability() {
    }

    /**
     * @param ability
     */
    public Ability(String ability) {
        super();
        this.ability = ability;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Ability.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("ability");
        sb.append('=');
        sb.append(((this.ability == null) ? "<null>" : this.ability));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
