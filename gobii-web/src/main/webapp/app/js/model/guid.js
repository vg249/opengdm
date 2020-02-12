System.register([], function (exports_1, context_1) {
    "use strict";
    var Guid;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [],
        execute: function () {
            Guid = class Guid {
                static generateUUID() {
                    let date = new Date().getTime();
                    let uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                        let random = (date + Math.random() * 16) % 16 | 0;
                        date = Math.floor(date / 16);
                        return (c == 'x' ? random : (random & 0x3 | 0x8)).toString(16);
                    });
                    return uuid;
                }
                ;
            };
            exports_1("Guid", Guid);
        }
    };
});
//# sourceMappingURL=guid.js.map