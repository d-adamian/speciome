<template>
    <nav class="navbar">
        <a class="navbar-brand" href="#">Speciome</a>
        <div class="navbar-nav navbar-right">
            <div class="dropdown">
                <a class="btn dropdown-toggle" role="button" id="navbarUserDropdown"
                   data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    {{user_email}}
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarUserDropdown">
                    <a class="dropdown-item" @click.prevent="logOut">Log Out</a>
                </div>
            </div>
        </div>
    </nav>
</template>

<script>
export default {
    name: 'NavBar',
    data: () => ({
        user_email: ""
    }),
    methods: {
        logOut() {
            fetch("/logout", {
                method: "post"
            }).then(() => {
                window.location.href = "/#sign-up";
            });
        }
    },
    created() {
        fetch("/user-details")
            .then(response => {
                if (response.status == 200) {
                    return response.text();
                } else if (response.status == 401) {
                    window.location.href = "/#sign-up";
                } else {
                    alert("Error while fetching user email");
                }
             })
            .then(res => this.user_email = res)
            .catch(() => alert("Error while fetching user email"));
    }
}
</script>

<style>
</style>