package org.gobiiproject.gobiisampletrackingdao;

import org.apache.commons.lang.RandomStringUtils;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.entity.*;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DaoTestSetUp {

    private EntityManager em;
    private CvDao cvDao;

    public DaoTestSetUp(EntityManager em, CvDao cvDao) {
        this.em = em;
        this.cvDao = cvDao;

    }

    final int testPageSize = 10;

    Random random = new Random();

    private List<DnaRun> createdDnaRuns = new ArrayList<>();
    private List<Germplasm> createdGermplasms = new ArrayList<>();
    private List<Experiment> createdExperiments = new ArrayList<>();


    public List<DnaRun> getCreatedDnaRuns() {
        return createdDnaRuns;
    }

    public void setCreatedDnaRuns(List<DnaRun> createdDnaRuns) {
        this.createdDnaRuns = createdDnaRuns;
    }

    public List<Germplasm> getCreatedGermplasms() {
        return createdGermplasms;
    }

    public void setCreatedGermplasms(List<Germplasm> createdGermplasms) {
        this.createdGermplasms = createdGermplasms;
    }

    public List<Experiment> getCreatedExperiments() {
        return createdExperiments;
    }

    public void setCreatedExperiments(List<Experiment> createdExperiments) {
        this.createdExperiments = createdExperiments;
    }

    public void createTestDnaRuns() {

        List<Germplasm> germplasms = new ArrayList<>();

        List<DnaSample> dnaSamples = new ArrayList<>();

        List<Experiment> experiments = new ArrayList<>();

        Cv newStatus = cvDao.getCvs(
            "new",
            CvGroup.CVGROUP_STATUS.getCvGroupName(),
            GobiiCvGroupType.GROUP_TYPE_SYSTEM).get(0);

        //Add PI contact
        Contact contact = new Contact();
        contact.setFirstName(RandomStringUtils.random(7, true, true));
        contact.setLastName(RandomStringUtils.random(7, true, true));
        contact.setCode(RandomStringUtils.random(7, true, true));
        contact.setEmail(RandomStringUtils.random(7, true, true));
        em.persist(contact);

        //Add Project
        Project project = new Project();;
        project.setContact(contact);
        project.setProjectName(RandomStringUtils.random(7, true, true));
        project.setStatus(newStatus);
        em.persist(project);

        //Add Germplasm
        for(int i = 0; i < (testPageSize/5); i++) {
            Germplasm germplasm = new Germplasm();
            germplasm.setGermplasmName(
                RandomStringUtils.random(7, true, true));
            germplasm.setStatus(newStatus);
            em.persist(germplasm);

            createdGermplasms.add(germplasm);

            germplasms.add(germplasm);
        }


        //Add DnaSample
        for(int i = 0; i < (testPageSize/2); i++) {
            DnaSample dnaSample = new DnaSample();
            dnaSample.setDnaSampleName(
                RandomStringUtils.random(7, true, true));
            dnaSample.setProjectId(project.getProjectId());
            dnaSample.setGermplasm(
                germplasms.get(random.nextInt(germplasms.size())));
            dnaSample.setStatus(newStatus);
            dnaSample.setDnaSampleUuid(
                RandomStringUtils.random(10, true, true));
            em.persist(dnaSample);
            dnaSamples.add(dnaSample);
        }

        //Add Experiment
        for(int i = 0; i < (testPageSize/5); i++){

            Experiment experiment = new Experiment();

            experiment.setExperimentName(
                RandomStringUtils.random(7, true, true));
            experiment.setExperimentCode(
                RandomStringUtils.random(7, true, true));
            experiment.setProject(project);
            experiment.setStatus(newStatus);

            VendorProtocol vendorProtocol = new VendorProtocol();

            Organization vendor = new Organization();
            vendor.setName(RandomStringUtils.random(7, true, true));
            vendor.setStatus(newStatus);

            Protocol protocol = new Protocol();
            protocol.setName(RandomStringUtils.random(7, true, true));

            em.persist(vendor);
            em.persist(protocol);

            vendorProtocol.setProtocol(protocol);
            vendorProtocol.setVendor(vendor);
            em.persist(vendorProtocol);

            experiment.setVendorProtocol(vendorProtocol);
            em.persist(experiment);

            createdExperiments.add(experiment);

            experiments.add(experiment);
        }

        for(int i = 0; i < testPageSize; i++) {
            DnaRun dnaRun = new DnaRun();

            dnaRun.setDnaRunName(RandomStringUtils.random(7, true, true));
            dnaRun.setExperiment(
                experiments.get(random.nextInt(experiments.size())));
            dnaRun.setDnaSample(
                dnaSamples.get(random.nextInt(dnaSamples.size())));
            em.persist(dnaRun);

            createdDnaRuns.add(dnaRun);
        }
        em.flush();

    }

}
