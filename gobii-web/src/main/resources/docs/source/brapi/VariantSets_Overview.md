
VariantSet corresponds to Dataset in GDM system.
A dataset belongs to an experiment (study in BrAPI) and describes the suite of analyses applied to the vendor-delivered data to generate the dataset, as well as the dataset type.

<a name="variantsetresource">**Resource Description**</a>

Field | Type | Description
------|------|------------
variantSetDbId | Integer | Id of the VariantSet. Corresponds to dataset Id.
variantSetName | String | The name of the dataset. VariantSet name is unique under a study.
studyDbId | Integer | Study under which the VariantSet is created. Corresponds to the experiment Id.
analyses | Array | List of Analysis Resource. Lists all the analyses associated with the dataset.
availableFormats | Array | List of Formats in which the data can be extracted.
referenceSetDbId | String | The ID of the reference set that describes the sequences used by the variants in this set.
variantCount | Integer | Number of Variants(Markers or SNPs) in corresponding VariantSet
callSetCount | Integer | Number of CallSets (Dnaruns) in a corresponding VariantSet
extractReady | Boolean | Genotypes are ready to be extracted, if True.
dataFormat | String | Structure of the data within a file (ie DartSeq, VCF, Hapmap, tabular, etc)
fileFormat | String | 
createdBy | Integer | Contact Id of the user who created the VariantSet.
created | String | Created date string in UTC
modifiedBy | Integer | Contact Id of the user who modified the VariantSet.
modified | Integer | Modified date string in UTC

<a name="variantsetresourceexample">**Resource Example**</a>

```json

        {
            "createdBy": 1,
            "modifiedBy": 4,
            "variantSetDbId": "2",
            "variantSetName": "CIMMYT Wheat - M51STIBWSN_2",
            "studyDbId": "2",
            "fileUrl": "/gobii-dev/variantsets/2/calls/download",
            "fileFormat": "text/csv",
            "dataFormat": "tabular",
            "analyses": [
                {
                    "analysisDbId": "2",
                    "analysisName": "None",
                    "description": "",
                    "created": "2019-12-19T05:00:00"
                }
            ],
            "extractReady": true,
            "callSetCount": 710,
            "variantCount": 54,
            "created": "2019-12-19T05:00:00",
            "updated": "2019-12-19T05:00:00"
        } 

```


