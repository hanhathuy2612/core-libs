package com.hnh.enterprise.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * An Authority.
 */
@Entity
@Table(name = "authority")
@JsonIgnoreProperties(value = {"new", "id"})
@Getter
@Setter
@NoArgsConstructor
public class Authority implements Serializable, Persistable<String> {
    
    @Serial
    private static final long serialVersionUID = 1L;
    
    @NotNull
    @Size(max = 50)
    @Id
    @Column(name = "name", length = 50, nullable = false)
    private String name;
    
    @Transient
    private boolean isPersisted;

    public Authority(String name) {
        this.name = name;
    }
    
    @PostLoad
    @PostPersist
    public void updateEntityState() {
        this.setIsPersisted();
    }
    
    @Override
    public String getId() {
        return this.name;
    }
    
    @org.springframework.data.annotation.Transient
    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }
    
    public void setIsPersisted() {
        this.isPersisted = true;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Authority)) {
            return false;
        }
        return getName() != null && getName().equals(((Authority) o).getName());
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }
    
    // prettier-ignore
    @Override
    public String toString() {
        return "Authority{" +
                "name=" + getName() +
                "}";
    }
}
