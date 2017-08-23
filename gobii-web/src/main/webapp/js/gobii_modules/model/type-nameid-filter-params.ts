// The problem with using an enum is that in the debugger
// you cannot readily see what the enum type value means
// Knowing the nameidfilterparam type on debugger inspection is
// particularly important because of the generic way in which nameids
// are treated in the components and services
export class NameIdFilterParamTypes {

    public static CONTACT_PI: string = "Contact-PI";
    public static EXPERIMENTS: string = "Experiments";
    public static CV_DATATYPE: string = "Cv-DataType";
    public static MAPSETS: string = "Mapsets";
    public static PLATFORMS: string = "Platforms";
    public static DATASETS_BY_EXPERIUMENT: string = "Datasets-by-experiment";
    public static PROJECTS: string = "Projects";
    public static MARKER_GROUPS: string = "Marker Groups";
}

