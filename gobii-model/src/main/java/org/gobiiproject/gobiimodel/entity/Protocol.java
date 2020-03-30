/**
 * Protocol.java
 * 
 * Protocol entity class
 * @auhtor Rodolfo N. Duldulao, Jr.
 * 
 */

package org.gobiiproject.gobiimodel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="protocol")
@NoArgsConstructor
public class Protocol extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "protocol_id")
    private Integer protocolId;

    @Column(name = "name")
    private String name;

    @Column(name="description")
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private Cv type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_id")
    private Platform platform;

    @Column(name="props")
    @Type(type = "CvPropertiesType")
    private java.util.Map<String, String> properties;

    @Column(name="status")
    private Integer status;

}