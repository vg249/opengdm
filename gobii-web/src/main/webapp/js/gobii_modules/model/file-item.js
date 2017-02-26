System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var FileItem;
    return {
        setters: [],
        execute: function () {
            FileItem = (function () {
                function FileItem(processType, entityType, itemId, itemName, checked, required) {
                    this.processType = processType;
                    this.entityType = entityType;
                    this.itemId = itemId;
                    this.itemName = itemName;
                    this.checked = checked;
                    this.required = required;
                    this.processType = processType;
                    this.itemId = itemId;
                    this.itemName = itemName;
                    this.required = required;
                    //        this.uniqueId = Guid.generateUUID();
                }
                //OnChange does not see the FileItemEvent as being a new event unless it's
                //a branch new instance, even if any of the property values are different.
                //I'm sure there's a better way to do this. For example, the tree component should
                //subscribe to an observer that is fed by the root component?
                FileItem.newFileItemEvent = function (fileItemEvent) {
                    //        let existingUniqueId = fileItemEvent.uniqueId;
                    var returnVal = new FileItem(fileItemEvent.processType, fileItemEvent.entityType, fileItemEvent.itemId, fileItemEvent.itemName, fileItemEvent.checked, fileItemEvent.required);
                    //        returnVal.uniqueId = existingUniqueId;
                    return returnVal;
                };
                return FileItem;
            }()); // FileItem()
            exports_1("FileItem", FileItem);
        }
    };
});
//# sourceMappingURL=file-item.js.map