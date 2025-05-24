package it.m4.spring.ticket_platform.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username; 
    private String email;
    private String password;
    private Boolean disponibile = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> role = new ArrayList<>();

    @OneToMany(mappedBy = "admin")
    private List<Ticket> ticketsCreati;

    @OneToMany(mappedBy = "operatore")
    private List<Ticket> ticketsAssegnati; 

    @OneToMany(mappedBy = "user")
    private List<Nota> nota;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getDisponibile() {
        return disponibile;
    }

    public void setDisponibile(Boolean disponibile) {
        this.disponibile = disponibile;
    }

    public List<Role> getRole() {
        return role;
    }

    public void setRole(List<Role> role) {
        this.role = role;
    }

    public List<Ticket> getTicketsCreati() {
        return ticketsCreati;
    }

    public void setTicketsCreati(List<Ticket> ticketsCreati) {
        this.ticketsCreati = ticketsCreati;
    }

    public List<Ticket> getTicketsAssegnati() {
        return ticketsAssegnati;
    }

    public void setTicketsAssegnati(List<Ticket> ticketsAssegnati) {
        this.ticketsAssegnati = ticketsAssegnati;
    }

    public List<Nota> getNota() {
        return nota;
    }

    public void setNota(List<Nota> nota) {
        this.nota = nota;
    }

    public boolean hasRole(String roleName) {
    for (Role r : getRole()) {
        if (r.getNome().equalsIgnoreCase(roleName)) {
            return true;
        }
    }
    return false;
    }

}

