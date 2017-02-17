System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var CheckBoxEvent;
    return {
        setters: [],
        execute: function () {
            CheckBoxEvent = (function () {
                function CheckBoxEvent(processType, id, name, checked, required) {
                    this.processType = processType;
                    this.id = id;
                    this.name = name;
                    this.checked = checked;
                    this.required = required;
                    this.processType = processType;
                    this.id = id;
                    this.name = name;
                    this.required = required;
                }
                return CheckBoxEvent;
            }()); // CheckBoxEvent()
            exports_1("CheckBoxEvent", CheckBoxEvent);
        }
    };
});
//# sourceMappingURL=event-checkbox.js.map