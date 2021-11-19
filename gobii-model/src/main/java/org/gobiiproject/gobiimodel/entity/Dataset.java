package org.gobiiproject.gobiimodel.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.JsonNode;

import org.hibernate.annotations.Type;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Model for Dataset Entity.ß
 * Represents the database table dataset.
 *
 * props - is a jsonb column. It is converted to jackson.fasterxml JsonNode using a
 * user defined hibernate converter class.
 */
@Entity
@Table(name = "dataset")
@Data
@EqualsAndHashCode(callSuper = false)
@NamedEntityGraph(name = "graph.dataset",
    attributeNodes = {
        @NamedAttributeNode(value = "experiment", subgraph = "graph.dataset.experiment"),
        @NamedAttributeNode(value = "callingAnalysis"),
        @NamedAttributeNode(value = "status"),
        @NamedAttributeNode(value = "type")
    },
    subgraphs = {
        @NamedSubgraph(
            name = "graph.dataset.experiment",
            attributeNodes = {
                @NamedAttributeNode(value = "project", subgraph = "graph.dataset.experiment.project"),
                @NamedAttributeNode(value = "vendorProtocol", subgraph = "graph.dataset.experiment.vendorProtocol")
            }
        ),
        @NamedSubgraph(
            name = "graph.dataset.experiment.project",
            attributeNodes = {
                @NamedAttributeNode(value = "contact")
            }
        ),
        @NamedSubgraph(
            name = "graph.dataset.experiment.vendorProtocol",
            attributeNodes = {
                @NamedAttributeNode(value = "protocol", subgraph = "graph.dataset.experiment.vendorProtocol.protocol")
            }
        ),
        @NamedSubgraph(
            name = "graph.dataset.experiment.vendorProtocol.protocol",
            attributeNodes = {
                @NamedAttributeNode(value = "platform")
            }
        )
    }
)
public class Dataset extends BaseEntity {

    @Id
    @Column(name="dataset_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer datasetId;

    @Column(name="name")
    private String datasetName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experiment_id")
    private Experiment experiment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "callinganalysis_id", referencedColumnName = "analysis_id")
    private Analysis callingAnalysis;

    @Column(name = "analyses")
    @Type(type = "IntArrayType")
    private Integer[] analyses;

    @Column(name="data_table")
    private String dataTable;

    @Column(name="data_file")
    private String dataFile;

    @Column(name="quality_table")
    private String qualityTable;

    @Column(name="quality_file")
    private String qualityFile;

    @Column(name="scores")
    @Type(type = "JsonNodeType")
    private JsonNode scores;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status", referencedColumnName = "cv_id")
    private Cv status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", referencedColumnName = "cv_id")
    private Cv type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", referencedColumnName = "job_id")
    private Job job;

    @OneToOne(mappedBy="dataset")
    private DatasetStats datasetStats;

}
