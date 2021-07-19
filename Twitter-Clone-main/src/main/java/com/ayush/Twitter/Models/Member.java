package com.ayush.Twitter.Models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Name can not be null!!")
    @NotEmpty(message = "Name can not be empty!!")
    private String name;

    @NotNull(message = "Email can not be null!!")
    @NotEmpty(message = "Email can not be empty!!")
    @Column(unique = true)
    private String email;

    private String role;

    @NotNull(message = "Password can not be null!!")
    @NotEmpty(message = "Password can not be empty!!")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}$", message = "Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character.")
    private String password;

    private String image;
    private String token;
    private Boolean is_verified;

    @Override

    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        return gson.toJson(this).toString();
    }

    public Member() {
    }

    public Member(String name, String email, String role, String password, String image, String token, Boolean is_verified) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.password = password;
        this.image = image;
        this.token = token;
        this.is_verified = is_verified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        String[] auth_array = this.role.split(":");
        for (String i : auth_array) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(i);
            authorityList.add(grantedAuthority);
        }
        return authorityList;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(Boolean is_verified) {
        this.is_verified = is_verified;
    }
}
