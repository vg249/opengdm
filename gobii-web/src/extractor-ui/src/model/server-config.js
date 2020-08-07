System.register([], function (exports_1, context_1) {
    "use strict";
    var ServerConfig;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [],
        execute: function () {
            ServerConfig = /** @class */ (function () {
                function ServerConfig(crop, domain, contextRoot, port, confidentialityNotice) {
                    this.crop = crop;
                    this.domain = domain;
                    this.contextRoot = contextRoot;
                    this.port = port;
                    this.confidentialityNotice = confidentialityNotice;
                    this.crop = crop;
                    this.domain = domain;
                    this.port = port;
                    this.confidentialityNotice = confidentialityNotice;
                }
                return ServerConfig;
            }());
            exports_1("ServerConfig", ServerConfig);
        }
    };
});
//# sourceMappingURL=server-config.js.map