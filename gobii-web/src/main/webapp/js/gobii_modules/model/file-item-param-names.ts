// The problem with using an enum is that in the debugger
// you cannot readily see what the enum type value means
// Knowing the nameidfilterparam type on debugger inspection is
// particularly important because of the generic way in which nameids
// are treated in the components and services
export class FilterParamNames {

    public static UNKNOWN: string = "UNKNOWN";
    public static CONTACT_PI: string = "Contact-PI";
    public static EXPERIMENTS: string = "Experiments";
    public static EXPERIMENTS_BY_PROJECT: string = "Experiments-by-project";
    public static CV_DATATYPE: string = "Cv-DataType";
    public static CV_JOB_STATUS: string = "Cv-JobStatus";
    public static MAPSETS: string = "Mapsets";
    public static PLATFORMS: string = "Platforms";
    public static DATASETS_BY_EXPERIMENT: string = "Datasets-by-experiment";
    public static DATASET_LIST: string = "Dataset-List";
    public static DATASET_BY_DATASET_ID: string = "Dataset-List-By-DatasetId";
    public static ANALYSES_BY_DATASET_ID: string = "Analysis-List-By-DatasetId";
    public static PROJECTS_BY_CONTACT: string = "Projects-by-contact";
    public static PROJECTS: string = "Project";
    public static MARKER_GROUPS: string = "Marker Groups";
}

