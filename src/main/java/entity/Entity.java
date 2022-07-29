package entity;

import java.io.Serializable;

public abstract class Entity implements Serializable {

    private Integer id = null;

    protected Entity(){}

    protected Entity(int id){
        this.id = id;
    }

    public Integer getId(){
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null){
            return false;
        }
        if (this == obj){
            return true;
        }
        if (getClass() != obj.getClass()){
            return false;
        }
        return ((Entity) obj).getId().equals(id);
    }

    @Override
    public int hashCode(){
        return id.hashCode();
    }

}
