System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var FileModelState, FileModelTreeEvent;
    return {
        setters: [],
        execute: function () {
            (function (FileModelState) {
                FileModelState[FileModelState["UNKNOWN"] = 0] = "UNKNOWN";
                FileModelState[FileModelState["NOT_COMPLETE"] = 1] = "NOT_COMPLETE";
                FileModelState[FileModelState["ERROR"] = 2] = "ERROR";
                FileModelState[FileModelState["COMPLETE"] = 3] = "COMPLETE";
            })(FileModelState || (FileModelState = {}));
            exports_1("FileModelState", FileModelState);
            FileModelTreeEvent = (function () {
                function FileModelTreeEvent(fileItem, statusTreeTemplate, fileModelState, message) {
                    this.fileItem = fileItem;
                    this.statusTreeTemplate = statusTreeTemplate;
                    this.fileModelState = fileModelState;
                    this.message = message;
                    this.fileItem = fileItem;
                    this.statusTreeTemplate = statusTreeTemplate;
                    this.fileModelState = fileModelState;
                }
                return FileModelTreeEvent;
            }());
            exports_1("FileModelTreeEvent", FileModelTreeEvent);
        }
    };
});
//# sourceMappingURL=file-model-tree-event.js.map