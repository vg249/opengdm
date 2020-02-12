System.register(["./link"], function (exports_1, context_1) {
    "use strict";
    var link_1, Links;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (link_1_1) {
                link_1 = link_1_1;
            }
        ],
        execute: function () {
            Links = class Links {
                constructor(exploreLinksPerDataItem, linksPerDataItem) {
                    this.exploreLinksPerDataItem = exploreLinksPerDataItem;
                    this.linksPerDataItem = linksPerDataItem;
                }
                static fromJSON(json) {
                    let exploreLinksPerDataItem = [];
                    json.exploreLinksPerDataItem.forEach(l => {
                        exploreLinksPerDataItem.push(link_1.Link.fromJSON(l));
                    });
                    let linksPerDataItem = [];
                    json.linksPerDataItem.forEach(l => {
                        linksPerDataItem.push(link_1.Link.fromJSON(l));
                    });
                    return new Links(exploreLinksPerDataItem, linksPerDataItem); // new
                } // fromJson()
            };
            exports_1("Links", Links);
        }
    };
});
//# sourceMappingURL=links.js.map