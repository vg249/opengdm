
Brapi CallSets corresponds to Dna Run in GDM system.


<a name="callsetresource">**Resource Description**</a>

Field | Type | Description
------|------|------------
callSetDbId | String | Id of the CallSet. Corresponds to Dna Run.
callSetName | String | Name of the Dna Run
studyDbId | String | Genotyping Experiment under dna run belongs to.
sampleDbId | String | Dna Sample for the Genotype calls were generated
sampleName | String | Dna Sample Name  
variantSetIds | Array | List of datasets to which the dnarun belongs to.
germplasmDbId | String | Germplasm to which dna sample of the dna run belongs to.
germplasmName | String | Name of the germplasm to which dna sample of dna run belongs to.
additionalInfo | String | User defined additional properties.
createdBy | Integer | Contact Id of the user who created the VariantSet.
created | Date String | Created date string in UTC
updatedBy | Integer | Contact Id of the user who modified the VariantSet.
updated | Date String | Modified date string in UTC

<a name="callsetresourceexample">**Resource Example**</a>

```

    {
        "callSetDbId": "6",
        "callSetName": "WL18PVSD000001",
        "studyDbId": "2",
        "sampleDbId": "6",
        "sampleName": "WL18PVSD000001",
        "variantSetIds": [
            2
        ],
        "germplasmDbId": "6",
        "germplasmName": "7694609",
        "additionalInfo": {}
    }

```


