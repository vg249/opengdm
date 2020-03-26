/**
 * Organization.java
 * 
 * Entity class
 * @author Rodolfo N. Duldulao, Jr.
 * @since 2020-03-26
 */

package org.gobiiproject.gobiimodel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "organization")
@Data
@NoArgsConstructor
public class Organization extends BaseEntity {

    @Id
    @Column(name="organization_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer organizationId;


    @Column(name="name")
    private String name;

    @Column(name="address")
    private String address;

    @Column(name="website")
    private String website;
    
    @Column(name="status")
    private Integer status;

}