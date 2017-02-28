System.register(["./guid"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var guid_1, FileItem;
    return {
        setters: [
            function (guid_1_1) {
                guid_1 = guid_1_1;
            }
        ],
        execute: function () {
            FileItem = (function () {
                function FileItem(gobiiExtractFilterType, processType, entityType, cvFilterType, itemId, itemName, checked, required) {
                    this.gobiiExtractFilterType = gobiiExtractFilterType;
                    this.processType = processType;
                    this.entityType = entityType;
                    this.cvFilterType = cvFilterType;
                    this.itemId = itemId;
                    this.itemName = itemName;
                    this.checked = checked;
                    this.required = required;
                    this.processType = processType;
                    this.itemId = itemId;
                    this.itemName = itemName;
                    this.required = required;
                    this.fileItemUniqueId = guid_1.Guid.generateUUID();
                }
                //OnChange does not see the FileItemEvent as being a new event unless it's
                //a branch new instance, even if any of the property values are different.
                //I'm sure there's a better way to do this. For example, the tree component should
                //subscribe to an observer that is fed by the root component?
                FileItem.newFileItemEvent = function (fileItem, gobiiExtractFilterType) {
                    var existingUniqueId = fileItem.fileItemUniqueId;
                    var returnVal = new FileItem(gobiiExtractFilterType, fileItem.processType, fileItem.entityType, fileItem.cvFilterType, fileItem.itemId, fileItem.itemName, fileItem.checked, fileItem.required);
                    returnVal.fileItemUniqueId = existingUniqueId;
                    return returnVal;
                };
                return FileItem;
            }()); // FileItem()
            exports_1("FileItem", FileItem);
        }
    };
});
//# sourceMappingURL=file-item.js.map