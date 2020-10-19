package org.gobiiproject.gobiimodel.entity;

import javax.persistence.Entity;

@Entity
@Table(name = "project_stats")
public class ProjectStats {

    @Id
    @Column(name = "project_id")
    private Integer projectId;

    @OneToOne
    @JoinColumn(name = "project_id")
    @MapsId
    private Project project;
}
