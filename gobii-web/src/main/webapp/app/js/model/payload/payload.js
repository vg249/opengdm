System.register(["./links"], function (exports_1, context_1) {
    "use strict";
    var links_1, Payload;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (links_1_1) {
                links_1 = links_1_1;
            }
        ],
        execute: function () {
            Payload = class Payload {
                constructor(data, links) {
                    this.data = data;
                    this.links = links;
                }
                static fromJSON(json) {
                    let data = [];
                    json.data.forEach(d => {
                        data.push(d);
                    });
                    let links = links_1.Links.fromJSON(json.linkCollection);
                    return new Payload(data, links); // new
                } // fromJson()
            };
            exports_1("Payload", Payload);
        }
    };
});
//# sourceMappingURL=payload.js.map