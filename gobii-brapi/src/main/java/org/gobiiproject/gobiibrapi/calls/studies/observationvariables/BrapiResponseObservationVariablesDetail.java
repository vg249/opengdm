package org.gobiiproject.gobiibrapi.calls.studies.observationvariables;

/**
 * Created by Phil on 12/18/2016.
 */
public class BrapiResponseObservationVariablesDetail {
//    {
//        "observationVariableDbId": "CO_334:0100622",
//            "name": "caro_spectro",
//            "ontologyDbId": "CO_334",
//            "ontologyName": "Cassava",
//            "synonyms": ["Carotenoid content by spectro"],
//        "contextOfUse": ["Trial evaluation", "Nursery evaluation"],
//        "growthStage": "mature",
//            "status": "recommended",
//            "xref": "TL_455:0003001",
//            "institution": "",
//            "scientist": "",
//            "date": "2016-05-13",
//            "language": "EN",
//            "crop": "Cassava",
//            "trait": {
//        "traitDbId": "CO_334:0100620",
//                "name": "Carotenoid content",
//                "class": "physiological trait",
//                "description": "Cassava storage root pulp carotenoid content",
//                "synonyms": ["carotenoid content measure"],
//        "mainAbbreviation": "CC",
//                "alternativeAbbreviations": ["CCS"],
//        "entity": "root",
//                "attribute": "carotenoid",
//                "status": "recommended",
//                "xref": "TL_455:0003023"
//    },
//        "method": {
//        "methodDbId": "CO_334:0010320",
//                "name": "Visual Rating:total carotenoid by chart_method",
//                "class": "Estimation",
//                "description": "Assessment of the level of yellowness in cassava storage root pulp using the tc chart",
//                "formula": null,
//                "reference": null
//    },
//        "scale": {
//        "scaleDbId": "CO_334:0100526",
//                "name": "ug/g",
//                "datatype": "Numeric",
//                "decimalPlaces": 2,
//                "xref": null,
//                "validValues": {
//            "min": 1,
//                    "max": 3,
//                    "categories": ["1=low", "2=medium", "3=high"]
//        }
//    },
//        "defaultValue": nullw

    public class MyPojo
    {
        private ObservationScale scale;

        private String status;

        private String ontologyName;

        private String ontologyDbId;

        private String[] contextOfUse;

        private String[] synonyms;

        private String scientist;

        private String date;

        private String crop;

        private String observationVariableDbId;

        private String growthStage;

        private String name;

        private String xref;

        private ObservationMethhod method;

        private String language;

        private String defaultValue;

        private ObservationTrait trait;

        private String institution;

        public ObservationScale getScale ()
        {
            return scale;
        }

        public void setScale (ObservationScale scale)
        {
            this.scale = scale;
        }

        public String getStatus ()
        {
            return status;
        }

        public void setStatus (String status)
        {
            this.status = status;
        }

        public String getOntologyName ()
        {
            return ontologyName;
        }

        public void setOntologyName (String ontologyName)
        {
            this.ontologyName = ontologyName;
        }

        public String getOntologyDbId ()
        {
            return ontologyDbId;
        }

        public void setOntologyDbId (String ontologyDbId)
        {
            this.ontologyDbId = ontologyDbId;
        }

        public String[] getContextOfUse ()
        {
            return contextOfUse;
        }

        public void setContextOfUse (String[] contextOfUse)
        {
            this.contextOfUse = contextOfUse;
        }

        public String[] getSynonyms ()
        {
            return synonyms;
        }

        public void setSynonyms (String[] synonyms)
        {
            this.synonyms = synonyms;
        }

        public String getScientist ()
        {
            return scientist;
        }

        public void setScientist (String scientist)
        {
            this.scientist = scientist;
        }

        public String getDate ()
        {
            return date;
        }

        public void setDate (String date)
        {
            this.date = date;
        }

        public String getCrop ()
        {
            return crop;
        }

        public void setCrop (String crop)
        {
            this.crop = crop;
        }

        public String getObservationVariableDbId ()
        {
            return observationVariableDbId;
        }

        public void setObservationVariableDbId (String observationVariableDbId)
        {
            this.observationVariableDbId = observationVariableDbId;
        }

        public String getGrowthStage ()
        {
            return growthStage;
        }

        public void setGrowthStage (String growthStage)
        {
            this.growthStage = growthStage;
        }

        public String getName ()
        {
            return name;
        }

        public void setName (String name)
        {
            this.name = name;
        }

        public String getXref ()
        {
            return xref;
        }

        public void setXref (String xref)
        {
            this.xref = xref;
        }

        public ObservationMethhod getMethod ()
        {
            return method;
        }

        public void setMethod (ObservationMethhod method)
        {
            this.method = method;
        }

        public String getLanguage ()
        {
            return language;
        }

        public void setLanguage (String language)
        {
            this.language = language;
        }

        public String getDefaultValue ()
        {
            return defaultValue;
        }

        public void setDefaultValue (String defaultValue)
        {
            this.defaultValue = defaultValue;
        }

        public ObservationTrait getTrait ()
        {
            return trait;
        }

        public void setTrait (ObservationTrait trait)
        {
            this.trait = trait;
        }

        public String getInstitution ()
        {
            return institution;
        }

        public void setInstitution (String institution)
        {
            this.institution = institution;
        }
    }

}
