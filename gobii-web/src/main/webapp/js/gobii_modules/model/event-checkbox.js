System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var CheckBoxEvent;
    return {
        setters: [],
        execute: function () {
            CheckBoxEvent = (function () {
                function CheckBoxEvent(processType, entityType, id, name, checked, required) {
                    this.processType = processType;
                    this.entityType = entityType;
                    this.id = id;
                    this.name = name;
                    this.checked = checked;
                    this.required = required;
                    this.processType = processType;
                    this.id = id;
                    this.name = name;
                    this.required = required;
                    //        this.uniqueId = Guid.generateUUID();
                }
                //OnChange does not see the CheckboxEvent as being a new event unless it's
                //a branch new instance, even if any of the property values are different.
                //I'm sure there's a better way to do this. For example, the tree component should
                //subscribe to an observer that is fed by the root component?
                CheckBoxEvent.newCheckboxEvent = function (checkboxEvent) {
                    //        let existingUniqueId = checkboxEvent.uniqueId;
                    var returnVal = new CheckBoxEvent(checkboxEvent.processType, checkboxEvent.entityType, checkboxEvent.id, checkboxEvent.name, checkboxEvent.checked, checkboxEvent.required);
                    //        returnVal.uniqueId = existingUniqueId;
                    return returnVal;
                };
                return CheckBoxEvent;
            }()); // CheckBoxEvent()
            exports_1("CheckBoxEvent", CheckBoxEvent);
        }
    };
});
//# sourceMappingURL=event-checkbox.js.map