package com.OurInternfactory.Models;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "UserData")
@NoArgsConstructor
@Getter
@Setter
public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "firstname", nullable = false)
    private String firstname;
    @Column(name = "lastname")
    private String lastname;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "otp")
    private Integer otp;
    @Column(name  = "otpRequestedTime")
    private Date otpRequestedTime;
    @Column(name = "active")
    private boolean active;
    @Column(name = "gender")
    private String gender;
    @Column(name = "mobile")
    private String phoneNumber;
    @Column(name = "profilePhoto")
    private String profilePhoto;
    private Boolean twoStepVerification;
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "user_internships", joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "internship_id", referencedColumnName = "id"))
    private Set<Internships> interships;
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user", referencedColumnName = "id"), inverseJoinColumns =  @JoinColumn(name = "role", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_submission", joinColumns = @JoinColumn(name = "user", referencedColumnName = "id"), inverseJoinColumns =  @JoinColumn(name = "submission_id", referencedColumnName = "id"))
    private Set<Submission> submission;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Resume resume;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map((role)-> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.email;
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
}
