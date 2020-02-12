System.register([], function (exports_1, context_1) {
    "use strict";
    var HeaderStatusMessage;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [],
        execute: function () {
            HeaderStatusMessage = class HeaderStatusMessage {
                constructor(message, statusLevel, validationStatusType) {
                    this.message = message;
                    this.statusLevel = statusLevel;
                    this.validationStatusType = validationStatusType;
                }
                static fromJSON(json) {
                    return new HeaderStatusMessage(json.message, json.statusLevel, json.validationStatusType);
                } // fromJSON
            };
            exports_1("HeaderStatusMessage", HeaderStatusMessage);
        }
    };
});
//# sourceMappingURL=dto-header-status-message.js.map