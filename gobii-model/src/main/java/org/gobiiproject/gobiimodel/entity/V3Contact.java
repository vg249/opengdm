package org.gobiiproject.gobiimodel.entity;

import javax.persistence.*;

@Entity
@Table(name="contact")
public class V3Contact extends BaseEntity {
    @Id
    @Column(name="contact_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer contactId;

    @Column(name="lastname")
    private String lastName;
    
    @Column(name="firstname")
	private String firstName;	
    
    @Column(name="code")
    private String code;	
    
    @Column(name="email")
    private String email;
    
    @Column(name="roles")
    private Integer[] roles;
    
	@Column(name="organization_id")	
	private Integer organizationId;	
    
    @Column(name="username")
    private String username;

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer[] getRoles() {
        return roles;
    }

    public void setRoles(Integer[] roles) {
        this.roles = roles;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
}