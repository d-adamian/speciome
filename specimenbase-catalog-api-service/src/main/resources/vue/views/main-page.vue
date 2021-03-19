<template id="main-page">
    <div>
        <h1 class="greeting-header">Welcome to Specimen Base!</h1>
        <h2>Your email is {{user_email}}</h2>

        <button class="btn btn-dark" @click.prevent="logOut">
            Log Out
        </button>
    </div>
</template>
<script>
    Vue.component("main-page", {
        template: "#main-page",
        data: () => ({
            user_email: ""
        }),
        methods: {
            logOut() {
                fetch("/logout", {
                    method: "post"
                }).then(res => {
                    window.location.href = "/static/sign-up";
                });
            }
        },
        created() {
            fetch("/user-details")
                .then(response => {
                    if (response.status == 200) {
                        return response.json();
                    } else if (response.status == 401) {
                        window.location.href = "/static/sign-up";
                    } else {
                        alert("Error while fetching user email");
                    }
                 })
                .then(res => this.user_email = res)
                .catch(() => alert("Error while fetching user email"));
        }
    });
</script>

<style>
    .greeting-header {
        color: goldenrod;
    }
</style>