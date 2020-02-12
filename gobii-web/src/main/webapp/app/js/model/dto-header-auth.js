System.register([], function (exports_1, context_1) {
    "use strict";
    var DtoHeaderAuth;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [],
        execute: function () {
            DtoHeaderAuth = class DtoHeaderAuth {
                constructor(userName, password, token, gobiiCropType) {
                    this.userName = userName;
                    this.password = password;
                    this.token = token;
                    this.gobiiCropType = gobiiCropType;
                    this.userName = userName;
                    this.password = password;
                    this.token = token;
                    this.gobiiCropType = gobiiCropType;
                }
                getToken() {
                    return this.token;
                }
                getGobiiCropType() {
                    return this.gobiiCropType;
                }
                static fromJSON(json) {
                    return new DtoHeaderAuth(json['userName'], json['password'], json['token'], json['gobiiCropType']);
                }
            };
            exports_1("DtoHeaderAuth", DtoHeaderAuth);
        }
    };
});
//# sourceMappingURL=dto-header-auth.js.map