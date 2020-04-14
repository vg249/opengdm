package org.gobiiproject.gobiimodel.entity;

import com.fasterxml.jackson.databind.JsonNode;
import org.gobiiproject.gobiimodel.entity.JpaConverters.JsonbConverter;
import org.hibernate.annotations.Type;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

/**
 * Model for Analysis Entity.
 * Represents the database table dataset.
 *
 */
@Entity
@Table(name = "analysis")
@Data
public class Analysis extends BaseEntity {

    @Id
    @Column(name="analysis_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer analysisId;

    @Column(name = "name")
    private String analysisName;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", referencedColumnName = "cv_id")
    private Cv type = new Cv();

    @Column(name = "program")
    private String program;

    @Column(name = "sourcename")
    private String sourceName;

    @Column(name = "sourceversion")
    private String sourceVersion;

    @Column(name = "sourceuri")
    private String sourceUri;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_id", referencedColumnName = "reference_id")
    private Reference reference = new Reference();

    //@Column(name="parameters", columnDefinition = "jsonb")
    //@Convert(converter = JsonbConverter.class)
    //private JsonNode parameters;
    @Column(name="parameters", columnDefinition = "jsonb")
    @Type(type = "CvPropertiesType")
    private Map<String, String> parameters;
    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status", referencedColumnName = "cv_id")
    private Cv status = new Cv();

    @Column(name = "timeexecuted")
    private Date timeExecuted;
}
