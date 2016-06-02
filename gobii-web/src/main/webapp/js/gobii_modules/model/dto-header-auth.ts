export class DtoHeaderAuth {

    public constructor(public userName:string,
                       public password:string,
                       public token:string) {

        this.userName = userName;
        this.password = password;
        this.token = token;
    }


    public getToken():string {
        return this.token;
    }


    public static fromJSON(json:JSON):DtoHeaderAuth {

        return new DtoHeaderAuth(
            json['userName'],
            json['password'],
            json['token']
        );

    }

    //
    // public toJSON(): JSON {
    //     return {
    //         userName : this.userName,
    //         password: this.password,
    //         token: this.token
    //     };
    // }
}
