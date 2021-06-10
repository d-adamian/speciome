<template id="nav-bar">
    <nav class="navbar">
        <a class="navbar-brand" href="#">Specimen Base</a>
        <div class="navbar-nav navbar-right">
            <div class="dropdown">
                <a class="btn dropdown-toggle" href="#" role="button" id="navbarUserDropdown"
                   data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    {{user_email}}
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarUserDropdown">
                    <a class="dropdown-item" href="#" @click.prevent="logOut">Log Out</a>
                </div>
            </div>
        </div>
    </nav>
</template>
<script>
    Vue.component("nav-bar", {
        template: "#nav-bar",
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
</style>