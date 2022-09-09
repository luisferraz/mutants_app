
package br.ufpr.tads.luis.mutantsapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class TopAbility implements Serializable {

    @SerializedName("ability")
    @Expose
    private String ability;

    @SerializedName("count")
    @Expose
    private Integer count;
    private final static long serialVersionUID = 6533214229199856384L;

    /**
     * No args constructor for use in serialization
     */
    public TopAbility() {
    }

    /**
     * @param ability
     * @param count
     */
    public TopAbility(String ability, Integer count) {
        super();
        this.ability = ability;
        this.count = count;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "TopAbility{" +
                "ability='" + ability + '\'' +
                ", count=" + count +
                '}';
    }

}
