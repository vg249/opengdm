package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;

import java.util.List;

/**
 * Created by VCalaminos on 5/2/2019.
 */
public interface DnaSampleService<T> {

    List<T> createSamples(List<T> sampleListDTO) throws GobiiDomainException;

}
