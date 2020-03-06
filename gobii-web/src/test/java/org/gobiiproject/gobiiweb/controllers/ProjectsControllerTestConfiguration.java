package org.gobiiproject.gobiiweb.controllers;

import org.gobiiproject.gobiiweb.DataSourceSelector;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("projectsController-test")
@Configuration
public class ProjectsControllerTestConfiguration {
    @Bean
    @Primary
    public DataSourceSelector dataSourceMulti() {
        return Mockito.mock(DataSourceSelector.class);
        
    }
}