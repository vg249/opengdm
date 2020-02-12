System.register([], function (exports_1, context_1) {
    "use strict";
    var Link;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [],
        execute: function () {
            Link = class Link {
                constructor(href, description, allowedMethods) {
                    this.href = href;
                    this.description = description;
                    this.allowedMethods = allowedMethods;
                }
                static fromJSON(json) {
                    let href = json.href;
                    let description = json.description;
                    let allowedMethods = [];
                    json.methods.forEach(m => { allowedMethods.push(m); });
                    return new Link(href, description, allowedMethods);
                } // fromJson()
            };
            exports_1("Link", Link);
        }
    };
});
//# sourceMappingURL=link.js.map