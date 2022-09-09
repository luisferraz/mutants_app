
package br.ufpr.tads.luis.mutantsapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class Mutant implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("imageType")
    @Expose
    private String imageType;
    @SerializedName("imageName")
    @Expose
    private String imageName;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("abilities")
    @Expose
    private List<Ability> abilities = null;
    private final static long serialVersionUID = 5296077689655956484L;

    /**
     * No args constructor for use in serialization
     */
    public Mutant() {
    }

    /**
     * @param abilities
     * @param createdAt
     * @param imageName
     * @param name
     * @param id
     * @param imageType
     * @param updatedAt
     */
    public Mutant(Integer id, String name, String imageType, String imageName, String createdAt, String updatedAt, List<Ability> abilities) {
        super();
        this.id = id;
        this.name = name;
        this.imageType = imageType;
        this.imageName = imageName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.abilities = abilities;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<Ability> abilities) {
        this.abilities = abilities;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Mutant.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null) ? "<null>" : this.id));
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null) ? "<null>" : this.name));
        sb.append(',');
        sb.append("imageType");
        sb.append('=');
        sb.append(((this.imageType == null) ? "<null>" : this.imageType));
        sb.append(',');
        sb.append("imageName");
        sb.append('=');
        sb.append(((this.imageName == null) ? "<null>" : this.imageName));
        sb.append(',');
        sb.append("createdAt");
        sb.append('=');
        sb.append(((this.createdAt == null) ? "<null>" : this.createdAt));
        sb.append(',');
        sb.append("updatedAt");
        sb.append('=');
        sb.append(((this.updatedAt == null) ? "<null>" : this.updatedAt));
        sb.append(',');
        sb.append("abilities");
        sb.append('=');
        sb.append(((this.abilities == null) ? "<null>" : this.abilities));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
