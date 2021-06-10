<template id="sign-up">
    <div>
        <form>
            <h3>Welcome!</h3>
            <div class="form-group">
                <label>New User?</label>
                <input type="checkbox" name="newUser" v-model="newUser">
            </div>
            <div class="form-group">
                <label>E-mail</label>
                <input type="text" name="email" v-model="email" placeholder="E-mail"/>
            </div>
            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" v-model="password" placeholder="Password"/>
            </div>

            <div v-if="newUser">
                <button
                        type="submit"
                        class="btn btn-primary"
                        @click.prevent="signUp"
                        :disabled="buttonDisabled"
                >
                    Sign Up
                </button>
            </div>
            <div v-else>
                <button
                        type="submit"
                        class="btn btn-secondary"
                        @click.prevent="logIn"
                        :disabled="buttonDisabled"
                >
                    Log In
                </button>
            </div>
        </form>
    </div>
</template>

<script>
    Vue.component("sign-up", {
        template: "#sign-up",
        data: () => ({
            email: "",
            newUser: false,
            password: ""
        }),
        computed: {
            buttonDisabled() {
                return !this.isInputCorrect();
            }
        },
        methods: {
            signUp() {
                if (!this.isInputCorrect()) {
                    alert("Input is incorrect");
                } else {
                    fetch("/register-user", {
                        method: "post",
                        body: JSON.stringify({
                            email: this.email,
                            password: this.password
                        })
                    }).then(response => {
                        if (response.status == 201) {
                            this.newUser = false;
                        } else {
                            alert("Unable to register user");
                        }
                    }).catch(() => alert("Failed to register user"));
                }
            },
            logIn() {
                if (!this.isInputCorrect()) {
                    alert("Input is incorrect");
                } else {
                    fetch("/login", {
                        method: "post",
                        body: JSON.stringify({
                            email: this.email,
                            password: this.password
                        })
                    }).then(response => {
                        if (response.status == 401) {
                            alert("Incorrect credentials");
                        } else if (response.status == 200) {
                            window.location.href = "/";
                        } else {
                            alert("Failed to log in");
                        }
                    }).catch(() => alert("Failed to log in"));
                }
            },
            isInputCorrect() {
                const emailFormat = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
                const isEmailCorrect = this.email.match(emailFormat);
                const isPasswordCorrect = this.password != "";
                return isEmailCorrect && isPasswordCorrect;
            }
        }
    });

</script>
