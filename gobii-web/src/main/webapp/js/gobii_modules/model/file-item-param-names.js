System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var FileItemParamNames;
    return {
        setters: [],
        execute: function () {
            FileItemParamNames = (function () {
                // The problem with using an enum is that in the debugger
                // you cannot readily see what the enum type value means
                // Knowing the nameidfilterparam type on debugger inspection is
                // particularly important because of the generic way in which nameids
                // are treated in the components and services
                function FileItemParamNames() {
                }
                FileItemParamNames.UNKNOWN = "UNKNOWN";
                FileItemParamNames.CONTACT_PI = "Contact-PI";
                FileItemParamNames.EXPERIMENTS = "Experiments";
                FileItemParamNames.EXPERIMENTS_BY_PROJECT = "Experiments-by-project";
                FileItemParamNames.CV_DATATYPE = "Cv-DataType";
                FileItemParamNames.MAPSETS = "Mapsets";
                FileItemParamNames.PLATFORMS = "Platforms";
                FileItemParamNames.DATASETS_BY_EXPERIMENT = "Datasets-by-experiment";
                FileItemParamNames.DATASETS = "Datasets";
                FileItemParamNames.PROJECTS_BY_CONTACT = "Projects-by-contact";
                FileItemParamNames.PROJECTS = "Project";
                FileItemParamNames.MARKER_GROUPS = "Marker Groups";
                return FileItemParamNames;
            }());
            exports_1("FileItemParamNames", FileItemParamNames);
        }
    };
});
//# sourceMappingURL=file-item-param-names.js.map