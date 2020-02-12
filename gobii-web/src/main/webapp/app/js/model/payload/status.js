System.register(["./../dto-header-status-message"], function (exports_1, context_1) {
    "use strict";
    var dto_header_status_message_1, Status;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (dto_header_status_message_1_1) {
                dto_header_status_message_1 = dto_header_status_message_1_1;
            }
        ],
        execute: function () {
            Status = class Status {
                constructor(succeeded, statusMessages) {
                    this.succeeded = succeeded;
                    this.statusMessages = statusMessages;
                }
                static fromJSON(json) {
                    let succeeded = json.succeeded;
                    let statusMessages = [];
                    json.statusMessages.forEach(m => {
                        statusMessages.push(dto_header_status_message_1.HeaderStatusMessage.fromJSON(m));
                    });
                    return new Status(succeeded, statusMessages); // new
                } // fromJson()
            };
            exports_1("Status", Status);
        }
    };
});
//# sourceMappingURL=status.js.map