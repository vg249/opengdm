System.register([], function (exports_1, context_1) {
    "use strict";
    var Contact;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [],
        execute: function () {
            Contact = class Contact {
                constructor(contactId, lastName, firstName, code, email, 
                //    public List<Integer> roles = new ArrayList<>(),
                //    public Integer createdBy,
                //    public Date createdDate,
                //    public Integer modifiedBy,
                //    public Date modifiedDate,
                organizationId, userName) {
                    this.contactId = contactId;
                    this.lastName = lastName;
                    this.firstName = firstName;
                    this.code = code;
                    this.email = email;
                    this.organizationId = organizationId;
                    this.userName = userName;
                }
            };
            exports_1("Contact", Contact);
        }
    };
});
//# sourceMappingURL=contact.js.map